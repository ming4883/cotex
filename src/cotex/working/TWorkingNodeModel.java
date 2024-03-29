/*
 * TWorkingNodeModel.java
 *
 * Created on April 3, 2007, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;
import cotex.msg.*;
import cotex.signal.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Ming
 */
public class TWorkingNodeModel implements INodeModel {
    
    //----------------------------------
    // Node Commands
    public static class TNewSessionCmd extends TNodeCommand {
        public TNewSessionCmd(String sessionName) {
            setArg("sessionName", sessionName);
        }
    }
    
    public static class TJoinSessionCmd extends TNodeCommand {
        public TJoinSessionCmd(TUniqueId sessionId) {
            setArg("sessionId", sessionId);
        }
    }
    
    public static class TLockParagraphCmd extends TNodeCommand {
        public TLockParagraphCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
        }
    }
    
    public static class TCommitParagraphCmd extends TNodeCommand {
        public TCommitParagraphCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
        }
    }
    
    public static class TInsertParagraphCmd extends TNodeCommand {
        public TInsertParagraphCmd(TParagraph paragraph,TParagraph newParagraph,TParagraph newGap) {
            setArg("paragraph", paragraph);
            setArg("newParagraph", newParagraph);
            setArg("newGap", newGap);
        }
    }
    
    public static class TEraseParagraphCmd extends TNodeCommand {
        public TEraseParagraphCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
        }
    }
    
    public static class TCancelParagraphCmd extends TNodeCommand {
        public TCancelParagraphCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
        }
    }
    public static class TGrantAccessRightCmd extends TNodeCommand {
        public TGrantAccessRightCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
        }
    }
    public static class TRefreshSessionListCmd extends TNodeCommand{
        public TRefreshSessionListCmd(){
        }
    }
    public static class TReplyGrantAccessRightCmd extends TNodeCommand {
        public TReplyGrantAccessRightCmd(TAccessRightQuery query, String result) {
            setArg("query", query);
            setArg("result",result);
        }
    }
    //----------------------------------
    // Cmd
    private interface ICmdInvoke {
        public abstract void invoke( TNodeCommand cmd );
    }
    
    private class Cmd {
        
        private HashMap<Class, ICmdInvoke> mCmdDispatcher;
        
        //------------------------------
        public Cmd() {
            mCmdDispatcher = new HashMap<Class, ICmdInvoke>();
            
            // exit
            mCmdDispatcher.put(
                TExitCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onExit(cmd);}
            } );
            
            // new session
            mCmdDispatcher.put(
                TNewSessionCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onNewSession(cmd);}
            } );
            
            // join session
            mCmdDispatcher.put(
                TJoinSessionCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onJoinSession(cmd);}
            } );
            
            // lock paragraph
            mCmdDispatcher.put(
                TLockParagraphCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onLockParagraph(cmd);}
            } );
            
            // commit paragraph
            mCmdDispatcher.put(
                TCommitParagraphCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onCommitParagraph(cmd);}
            } );
            
            // insert paragraph
            mCmdDispatcher.put(
                TInsertParagraphCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onInsertParagraph(cmd);}
            } );
            
            // erase paragraph
            mCmdDispatcher.put(
                TEraseParagraphCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onEraseParagraph(cmd);}
            } );
            
            // cancel paragraph editing
            mCmdDispatcher.put(
                TCancelParagraphCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onCancelParagraph(cmd);}
            } );
            
            // grant paragraph access right
            mCmdDispatcher.put(
                TGrantAccessRightCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onGrantAccessRight(cmd);}
            } );
            // reply grant paragraph access right
            mCmdDispatcher.put(
                TReplyGrantAccessRightCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onReplyGrantAccessRight(cmd);}
            } );
            
            // refresh session list
            mCmdDispatcher.put(
                TRefreshSessionListCmd.class,
                new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {_onRefreshSessionList();}
            } );
        }
        
        //------------------------------
        public void execute(TNodeCommand cmd) {
            
            if( mCmdDispatcher.containsKey( cmd.getClass() ) )
                mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
        }
        
        //------------------------------
        private void _onExit(TNodeCommand cmd) {
            
        }
        
        //------------------------------
        private void _onNewSession(TNodeCommand cmd) {
            
            try {
                
                String newSessionName = (String)cmd.getArg("sessionName");
                
                TLogManager.logMessage("TWorkingNodeModel: creating session: " + newSessionName );
                
                Object reply = connection.sendRequestToRegistry( new TNewSessionMsg(newSessionName) );
                
                if(reply != null && reply.getClass().equals(TReplyNewSessionMsg.class) ) {
                    
                    protocol.acquireSessionsFromRegistry();
                    
                    execute( new TJoinSessionCmd( ((TReplyNewSessionMsg)reply).sessionId ) );
                    
                } else {
                    
                    TLogManager.logError("TWorkingNodeModel: failed to create session");
                }
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _onJoinSession(TNodeCommand cmd) {
            
            try {
                
                TUniqueId sessionId = (TUniqueId)cmd.getArg("sessionId");
                
                String sessionName = mData.sessions.getById(sessionId).getName();
                
                TLogManager.logMessage("TWorkingNodeModel: connecting to session: " + sessionName + " ..." );
                
                Object reply = connection.sendRequestToRegistry( new TJoinSessionMsg( sessionId, mData.nodes.self() ) );
                
                if( reply != null && reply.getClass().equals(TReplyJoinSessionMsg.class) ) {
                    
                    TReplyJoinSessionMsg msg = (TReplyJoinSessionMsg)reply;
                    
                    if(msg.result) {
                        
                        mData.sessions.setCurrent(sessionId);
                        
                        mData.nodes.setListToCurrentSession(msg.existingNodes);
                        
                        protocol.acquireCurrentDocument();
                        
                        signal.restart();
                        
                        TLogManager.logMessage("TWorkingNodeModel: session connected");
                        
                    } else {
                        
                        TLogManager.logMessage("TWorkingNodeModel: failed to join session " + sessionName);
                        
                    }
                    
                } else {
                    
                    TLogManager.logMessage("TWorkingNodeModel: failed to join session " + sessionName);
                }
                
            } catch(TException e) {
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _onLockParagraph(TNodeCommand cmd) {
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: locking paragraph ...");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                
                if(paragraph.getState() != TParagraph.State.UNLOCKED) {
                    mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(false) );
                    return;
                }
                
                paragraph.notifyLocking();
                mData.paragraphs.notifyGuiUpdate();
                
                //mStates.put("LockingParagraph", paragraph);
                
                connection.sendObjectToRightNode(
                    connection.CMD,
                    new TLockParagraphMsg( mData.nodes.self().getSocketAddr(), paragraph.getId() ) );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _onCommitParagraph(TNodeCommand cmd) {
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: committing paragraph");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                
                if(paragraph.getState() != TParagraph.State.LOCKED) {
                    mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(false) );
                    return;
                }
                
                //mStates.put("CommittingParagraph", paragraph);
                
                // init commit check list
                protocol.mNodeCommitCheckList = new TNodeCheckList( mData.sessions.getCurrent() );
                protocol.mNodeCommitCheckList.set( mData.nodes.self().getSocketAddr() );
                
                connection.sendObjectToRightNode(
                    connection.CMD,
                    new TCommitParagraphMsg( mData.nodes.self().getSocketAddr(), mData.nodes.self().getDataPort(), paragraph.getId() ) );
                
            } catch(TException e) {
                TLogManager.logException(e);
                
            }
        }
        
        //------------------------------
        private void _onInsertParagraph(TNodeCommand cmd) {
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: inserting paragraph");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                TContent newParagraph = (TContent)cmd.getArg("newParagraph");
                TGap newGap = (TGap)cmd.getArg("newGap");
                
                if(paragraph.getState() != TParagraph.State.LOCKED) {
                    mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(false) );
                    return;
                }
                
                // init commit check list
                protocol.mNodeCommitCheckList = new TNodeCheckList( mData.sessions.getCurrent() );
                protocol.mNodeCommitCheckList.set( mData.nodes.self().getSocketAddr() );
                
                connection.sendObjectToRightNode(
                    connection.CMD,
                    new TInsertParagraphMsg( mData.nodes.self().getSocketAddr(), paragraph.getId(), newParagraph, newGap ) );
                
            } catch(TException e) {
                TLogManager.logException(e);
                
            }
        }
        
        //------------------------------
        private void _onEraseParagraph(TNodeCommand cmd) {
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: deleting paragraph");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                
                if(paragraph.getState() != TParagraph.State.LOCKED) {
                    mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(false) );
                    return;
                }
                
                // init commit check list
                protocol.mNodeCommitCheckList = new TNodeCheckList( mData.sessions.getCurrent() );
                protocol.mNodeCommitCheckList.set( mData.nodes.self().getSocketAddr() );
                
                connection.sendObjectToRightNode(
                    connection.CMD,
                    new TEraseParagraphMsg( mData.nodes.self().getSocketAddr(), paragraph.getId() ) );
                
            } catch(TException e) {
                TLogManager.logException(e);
                
            }
        }
        
        //------------------------------
        private void _onCancelParagraph(TNodeCommand cmd) {
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: cancelling paragraph editing...");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                
                connection.sendObjectToLeftNode(
                    connection.CMD,
                    new TCancelParagraphMsg( mData.nodes.self().getSocketAddr(), paragraph.getId() ));
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _onGrantAccessRight(TNodeCommand cmd) {
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: granting paragraph access right");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                connection.sendObject(
                    connection.CMD,
                    paragraph.getCreator().getAddr(),
                    paragraph.getCreator().getCmdPort(),
                    new TRequestParagraphRight(paragraph.getId(), mData.nodes.self()) );
                
            } catch(TException e) {
                TLogManager.logException(e);
            }
        }
        //------------------------------
        private void _onReplyGrantAccessRight(TNodeCommand cmd) {
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: replying grant paragraph access right");
                
                TAccessRightQuery query = (TAccessRightQuery)cmd.getArg("query");
                TNodeInfo requester = query.getRequester();
                boolean result = Boolean.valueOf((String)(cmd.getArg("result")));
                
                connection.sendObject(
                    connection.CMD,
                    requester.getAddr(),
                    requester.getCmdPort(),
                    new TReplyParagraphRight(query.getParagraphId(),result) );
                mData.accessRightList.remove(query);
            } catch(TException e) {
                TLogManager.logException(e);
            }
        }
        //------------------------------
        private void _onRefreshSessionList() {
            TLogManager.logMessage("TWorkingNodeModel: refreshing session list" );
            protocol.acquireSessionsFromRegistry();
        }
        
    }
    
    //----------------------------------
    // Connection
    private class Connection {
        
        public final String CMD = "Cmd";
        public final String DATA = "Data";
        
        private InetAddress mRegAddress;
        private Integer mRegPort;
        private Integer mCmdPort;
        private Integer mDataPort;
        
        //------------------------------
        public void startUp() throws NumberFormatException, TException {
            
            // setup listeners
            IConnectionListener cmdListener = new IConnectionListener() {
                public void notifyObjectReceived(IConnection conn, Object obj) {
                    protocol.onReceivedCmdObject(obj);
                }
            };
            
            IConnectionListener dataListener = new IConnectionListener() {
                public void notifyObjectReceived(IConnection conn, Object obj) {
                    protocol.onReceivedDataObject(obj);
                }
            };
            
            // get ports
            _getRegInfo();
            _getAvailableCmdPort();
            _getAvailableDataPort();
            
            // add and open connections
            mNode.getConnectionManager().addConnection(CMD).addListener(cmdListener);
            mNode.getConnectionManager().addConnection(DATA).addListener(dataListener);
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: reg connection openned at port " + mRegPort.toString() );
                
                get(CMD).open(mCmdPort);
                TLogManager.logMessage("TWorkingNodeModel: cmd connection openned at port " + mCmdPort.toString() );
                
                get(DATA).open(mDataPort);
                TLogManager.logMessage("TWorkingNodeModel: data connection openned at port " + mDataPort.toString());
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        public void shutDown() {
            
            try {
                
                get(CMD).close();
                get(DATA).close();
                
                mNode.getConnectionManager().removeConnection(CMD);
                mNode.getConnectionManager().removeConnection(DATA);
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _getRegInfo() {
            
            try {
                
                mRegPort = Integer.parseInt( util.getSetting("General", "RegistryPort") );
                mRegAddress = InetAddress.getByName( util.getSetting("Working", "RegistryAddress") );
                
            } catch(Exception e) {
                
                TLogManager.logError("Cannot acquire registry info from config file");
            }
            
        }
        
        //------------------------------
        private void _getAvailableCmdPort() {
            
            int currentPort = Integer.parseInt( util.getSetting("Working", "StartingPort") );
            
            while(true) {
                
                try {
                    
                    ServerSocket checkSocket = new ServerSocket(currentPort);
                    
                    mCmdPort = currentPort;
                    
                    checkSocket.close();
                    
                    break;
                    
                } catch (java.io.IOException ex) {
                    currentPort++;
                }
            }
        }
        
        //------------------------------
        private void _getAvailableDataPort() {
            
            int currentPort = mCmdPort.intValue() + 1;
            
            while(true) {
                
                try {
                    
                    ServerSocket checkSocket = new ServerSocket(currentPort);
                    
                    mDataPort = currentPort;
                    
                    checkSocket.close();
                    
                    break;
                    
                } catch (java.io.IOException ex) {
                    currentPort++;
                }
            }
        }
        
        //------------------------------
        public IConnection get(String name) throws TException {
            return mNode.getConnectionManager().getConnection(name);
        }
        
        //------------------------------
        public void sendObject(String connectionName, InetAddress addr, int port, Object obj) {
            
            try{
                
                get(connectionName).sendObject( addr, port, obj );
                
                //TLogManager.logMessage(
                //    "TWorkingNodeModel: sending message to " + addr.getHostAddress() + ":" + Integer.toString(port) + ";" + obj.toString() );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
                
            }
        }
        
        //------------------------------
        public int getNodePort(TNodeInfo nodeInfo, String connectionName) {
            
            if( connectionName.equals(DATA) )
                return nodeInfo.getDataPort();
            
            if( connectionName.equals(CMD) )
                return nodeInfo.getCmdPort();
            
            return 0;
        }
        
        //------------------------------
        public void sendObjectToRightNode(String connectionName, Object obj) {
            
            try{
                
                TNodeInfo nodeInfo = mData.nodes.getRight();
                
                get(connectionName).sendObject(
                    nodeInfo.getAddr(),
                    getNodePort(nodeInfo, connectionName),
                    obj );
                
                //TLogManager.logMessage(
                //   "TWorkingNodeModel: sending message to right node = " + nodeInfo.toString() + ";" + obj.toString() );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        public void sendObjectToLeftNode(String connectionName, Object obj) {
            
            try{
                
                TNodeInfo nodeInfo = mData.nodes.getLeft();
                
                get(connectionName).sendObject(
                    nodeInfo.getAddr(),
                    getNodePort(nodeInfo, connectionName),
                    obj );
                
                //TLogManager.logMessage(
                //    "TWorkingNodeModel: sending message to left node = " + nodeInfo.toString() + ";" + obj.toString() );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        public Object sendRequestToRegistry(Object request) {
            
            Object reply = null;
            
            try{
                
                Socket sessionSock = new Socket(mRegAddress, mRegPort);
                
                ObjectOutputStream outStream = new ObjectOutputStream( sessionSock.getOutputStream() );
                
                ObjectInputStream inStream = new ObjectInputStream( sessionSock.getInputStream() );
                
                outStream.writeObject(request);
                
                outStream.flush();
                
                reply = inStream.readObject();
                
                inStream.close();
                outStream.close();
                
                sessionSock.close();
                
            } catch(java.io.IOException e) {
                
                //TLogManager.logError("Send request to registry failed, request: " + request.toString() );
            } catch(ClassNotFoundException e) {
                
                TLogManager.logError("Unknown reply from registry.");
            }
            
            return reply;
            
        }
    }
    
    //----------------------------------
    // Protocol
    private class Protocol {
        
        private TNodeCheckList mNodeCommitCheckList = null;
        
        //------------------------------
        public void startUp() {
            
        }
        
        //------------------------------
        public void shutDown() {
            
            if(mData.nodes.getNodeCount() == 1) {
                
                // notify the registry if it is the remaining node in the session
                connection.sendRequestToRegistry(
                    new TLeaveSessionMsg(
                    mData.sessions.getCurrent().getInfo().getId(),
                    mData.nodes.self() ) );
                
            }
        }
        
        //------------------------------
        public void acquireSessionsFromRegistry() {
            
            TLogManager.logMessage("TWorkingNodeModel: acquring sessions list from registry");
            
            // todo: ask the registry node for sessions in step of hardcode
            //ArrayList<TSessionInfo> tempList = sendGetSessionInfoMessageToRegistry();
            Object reply = connection.sendRequestToRegistry( new TRequestSessionInfoMsg() );
            
            if(reply != null && reply.getClass().equals( TReplySessionInfoMsg.class ) ) {
                
                mData.sessions.setList( ((TReplySessionInfoMsg)reply).sessionInfo );
                
            } else {
                TLogManager.logError("Cannot acquire sessions, Please make sure the registry is running and the address is correct");
            }
        }
        
        //------------------------------
        public void acquireCurrentDocument() {
            
            mData.paragraphs.clear();
            
            try {
                
                int nodeCount=mData.nodes.getNodeCount();
                
                if( nodeCount == 1) {
                    
                    TContent content;
                    
                    mData.paragraphs.append( mData.paragraphs.createGap() );
                    
                    mData.paragraphs.append( content = mData.paragraphs.createContent() );
                    
                    mData.paragraphs.append( mData.paragraphs.createGap() );
                    
                    content.setContent("New Document\nClick here to start editing");
                    
                } else {
                    
                    connection.sendObjectToLeftNode(
                        connection.DATA,
                        new TRequestDocumentMsg( mData.nodes.self() ) );
                }
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        public void onReceivedCmdObject(Object obj) {
            
            TLogManager.logMessage("TWorkingNodeModel: cmd object received: " + obj.toString() );
            
            if(obj.getClass().equals( TNotifyAddNodeMsg.class) )
                _processNotifyAddNodeMsg( (TNotifyAddNodeMsg)obj );
            
            else if(obj.getClass().equals( TNotifyRemoveNodeMsg.class) )
                _processNotifyRemoveNodeMsg( (TNotifyRemoveNodeMsg)obj );
            
            else if(obj.getClass().equals( TLockParagraphMsg.class) )
                _processLockParagraphMsg( (TLockParagraphMsg)obj );
            
            else if(obj.getClass().equals( TLockResultMsg.class) )
                _processLockResultMsg( (TLockResultMsg)obj );
            
            else if(obj.getClass().equals( TCommitParagraphMsg.class) )
                _processCommitParagraphMsg( (TCommitParagraphMsg)obj );
            
            else if(obj.getClass().equals( TCommitResultMsg.class) )
                _processCommitResultMsg( (TCommitResultMsg)obj );
            
            else if(obj.getClass().equals( TCancelParagraphMsg.class) )
                _processCancelParagraphMsg( (TCancelParagraphMsg)obj );
            
            else if(obj.getClass().equals( TInsertParagraphMsg.class) )
                _processInsertParagraphMsg( (TInsertParagraphMsg)obj );
            
            else if(obj.getClass().equals( TEraseParagraphMsg.class) )
                _processEraseParagraphMsg( (TEraseParagraphMsg)obj );
            
            if(obj.getClass().equals( TRequestParagraphRight.class) )
                _processRequestParagraphRightMsg( (TRequestParagraphRight)obj );
            if(obj.getClass().equals( TReplyParagraphRight.class) )
                _processReplyParagraphRightMsg( (TReplyParagraphRight)obj );
            
        }
        
        //------------------------------
        public void onReceivedDataObject(Object obj) {
            
            TLogManager.logMessage("TWorkingNodeModel: data object received: " + obj.toString() );
            
            if(obj.getClass().equals( TRequestDocumentMsg.class ) )
                _processRequestDocumentMsg( (TRequestDocumentMsg)obj );
            
            else if(obj.getClass().equals( TReplyDocumentMsg.class ) )
                _processReplyDocumentMsg( (TReplyDocumentMsg)obj );
            
            else if(obj.getClass().equals( TRequestParagraphMsg.class) )
                _processRequestParagraphMsg( (TRequestParagraphMsg)obj );
            
            else if(obj.getClass().equals( TReplyParagraphMsg.class) )
                _processReplyParagraphMsg( (TReplyParagraphMsg)obj );
        }
        
        //------------------------------
        public void onHBSignalLost() {
            
            TNodeInfo deadNode = mData.nodes.getLeft();
            
            mData.nodes.removeFromCurrentSession( deadNode );
            
            signal.restart();
            
            connection.sendObjectToRightNode(
                connection.CMD,
                new TNotifyRemoveNodeMsg(
                deadNode,
                mData.nodes.self().getSocketAddr() ) );
            
        }
        
        //------------------------------
        public void onAckSignalLost() {
            
            //TLogManager.logError("Connection to the session is lost");
        }
        
        //------------------------------
        private void _processNotifyAddNodeMsg(TNotifyAddNodeMsg msg) {
            
            try {
                
                TLogManager.logMessage("adding node " + msg.nodeInfo.getName() );
                mData.nodes.addToCurrentSession(msg.nodeInfo);
                
                signal.restart();
                
            } catch(Exception e) {
                
            }
        }
        
        //------------------------------
        private void _processNotifyRemoveNodeMsg(TNotifyRemoveNodeMsg msg) {
            
            try {
                
                mData.paragraphs.setNullCreatorIfMatch(msg.nodeInfo);
                
                if( util.isSelf(msg.initiateNodeAddr) ) {
                    
                    connection.sendRequestToRegistry(
                        new TLeaveSessionMsg(
                        mData.sessions.getCurrent().getInfo().getId(),
                        msg.nodeInfo) );
                    
                } else {
                    
                    mData.nodes.removeFromCurrentSession(msg.nodeInfo);
                    
                    _forwardNotifyRemoveNodeMsg(msg);
                    
                    signal.restart();
                }
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _processLockParagraphMsg(TLockParagraphMsg msg) {
            
            try {
                
                TParagraph paragraph = mData.paragraphs.getById( msg.ParagraphId );
                
                if( util.isSelf( msg.InitiateNodeAddr ) ) {
                    
                    // message from self, lock success and tell the result
                    _sendPositiveLockResultMsg(msg);
                    
                } else {
                    
                    // message from others, check paragraph status
                    if(paragraph.getState() == TParagraph.State.UNLOCKED) {
                        
                        mData.paragraphs.setLocking( msg.ParagraphId );
                        _forwardLockMsg(msg);
                    } else
                        _sendNegativeLockResultMsg(msg);
                }
                
            } catch (TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _processLockResultMsg(TLockResultMsg msg) {
            
            try {
                
                TParagraph paragraph = mData.paragraphs.getById( msg.ParagraphId );
                
                if( true == msg.Result ) {
                    
                    mData.paragraphs.setLocked(
                        msg.ParagraphId,
                        mData.nodes.getBySocketAddr(msg.InitiateNodeAddr) );
                    
                    if( util.isSelf( msg.InitiateNodeAddr ) )
                        _notifyViewLockPositiveResult();
                    else
                        _forwardPositiveLockResultMsg(msg);
                    
                } else {
                    
                    mData.paragraphs.cancelLocked( msg.ParagraphId );
                    
                    if( util.isSelf( msg.InitiateNodeAddr ) )
                        _notifyViewNegativeLockResult();
                    else
                        _forwardNegativeLockResultMsg(msg);
                    
                }
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _processCommitParagraphMsg(TCommitParagraphMsg msg) {
            
            try {
                
                TParagraph paragraph = mData.paragraphs.getById( msg.ParagraphId );
                
                if( util.isSelf( msg.InitiateNodeAddr ) ) {
                    _waitAndSendPositiveCommitResultMsg(msg);
                    
                } else {
                    
                    // message from others
                    if(paragraph.getState() == TParagraph.State.LOCKED) {
                        
                        _forwardCommitMsg(msg);
                        _sendRequestParagraphMsg(msg);
                        
                    } else {
                        _sendNegativeCommitResultMsg(msg);
                    }
                    
                }
                
            } catch (TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        private void _waitAndSendPositiveCommitResultMsg(final TCommitParagraphMsg msg) {
            
            Thread t = new Thread() {
                
                public void run() {
                    
                    // wait until all nodes request paragraph content
                    while( !mNodeCommitCheckList.allSet() ) {
                        Thread.yield();
                    }
                    
                    TLogManager.logMessage("received paragraph reply from all nodes, send +ve commit result");
                    mNodeCommitCheckList = null;
                    
                    _sendPositiveCommitResultMsg(msg);
                }
            };
            
            t.start();
        }
        
        //------------------------------
        private void _processCommitResultMsg(TCommitResultMsg msg) {
            
            try {
                
                TParagraph paragraph = mData.paragraphs.getById( msg.ParagraphId );
                
                if(true == msg.Result) {
                    
                    // commit success, update content and unlock
                    mData.paragraphs.commit( msg.ParagraphId );
                    
                    if( util.isSelf( msg.InitiateNodeAddr ) )
                        _notifyViewPositiveCommitResult();
                    else
                        _forwardPositiveCommitResultMsg(msg);
                    
                } else {
                    
                    // commit failed, rollback
                    mData.paragraphs.rollback( msg.ParagraphId );
                    
                    if( util.isSelf( msg.InitiateNodeAddr ) )
                        _notifyViewNegativeCommitResult();
                    else
                        _forwardNegativeCommitResultMsg(msg);
                    
                }
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _processCancelParagraphMsg(TCancelParagraphMsg msg) {
            
            try {
                
                mData.paragraphs.rollback( msg.ParagraphId );
                
                if( util.isSelf( msg.InitiateNodeAddr ) )
                    _notifyViewCancelLockResult();
                else
                    _forwardCancelLockParagraphMsg(msg);
                
            } catch (TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _processRequestDocumentMsg(TRequestDocumentMsg msg) {
            
            connection.sendObject(
                connection.DATA,
                msg.NodeInfo.getAddr(),
                msg.NodeInfo.getDataPort(),
                new TReplyDocumentMsg( mData.paragraphs.getList() ) );
            
        }
        
        //------------------------------
        private void _processReplyDocumentMsg(TReplyDocumentMsg msg) {
            
            mData.paragraphs.setList( msg.ParagraphList );
        }
        
        //------------------------------
        private void _processRequestParagraphMsg(TRequestParagraphMsg msg) {
            
            try {
                TParagraph paragraph = mData.paragraphs.getById(msg.ParagraphId);
                
                connection.sendObject(
                    connection.DATA,
                    msg.RequestorAddr.getAddress(),
                    msg.DataPort,
                    new TReplyParagraphMsg( msg.ParagraphId, ( (TContent)paragraph ).getPendingContent() ) );
                
                mNodeCommitCheckList.set( msg.RequestorAddr );
            } catch(TException e) {
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _processReplyParagraphMsg(TReplyParagraphMsg msg) {
            
            try {
                TParagraph paragraph = mData.paragraphs.getById(msg.ParagraphId);
                ( (TContent)paragraph ).setPendingContent( msg.Content );
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            //mData.paragraphs.setList( msg.ParagraphList );
        }
        
        //------------------------------
        private void _processInsertParagraphMsg(TInsertParagraphMsg msg) {
            
            try {
                mData.paragraphs.Insert( msg.ParagraphId ,msg.NewParagraph, msg.NewGap);
                
                if( util.isSelf( msg.InitiateNodeAddr ) )
                    _notifyViewCancelLockResult();
                else
                    _forwardInsertMsg(msg);
                TLogManager.logMessage("TWorkingNodeModel: new paragraph added");
            } catch (TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _processEraseParagraphMsg(TEraseParagraphMsg msg) {
            
            try {
                mData.paragraphs.Erase( msg.ParagraphId );
                if( util.isSelf( msg.InitiateNodeAddr ) )
                    _notifyViewCancelLockResult();
                else
                    _forwardEraseMsg(msg);
                TLogManager.logMessage("TWorkingNodeModel: paragraph deleted");
            } catch (TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        private void _processRequestParagraphRightMsg(TRequestParagraphRight msg) {
            mData.accessRightList.add(new TAccessRightQuery(msg.RequestNodeInfo,msg.ParagraphId));
            TLogManager.logMessage("TWorkingNodeModel: new TAccessRightQuery added");
        }
        
        //------------------------------
        private void _processReplyParagraphRightMsg(TReplyParagraphRight msg) {
            if(true == msg.Result){
                mData.paragraphs.addGrantedParagraph(msg.ParagraphId);
                TLogManager.logMessage("TWorkingNodeModel: paragraph access right added");
            }else{
                TLogManager.logMessage("TWorkingNodeModel: grant paragraph access right reject");
            }
        }
        
        //------------------------------
        private void _sendNegativeLockResultMsg(final TLockParagraphMsg msg) {
            
            // already locked, tell left node the failed result
            connection.sendObjectToLeftNode(
                connection.CMD,
                new TLockResultMsg( msg.InitiateNodeAddr, msg.ParagraphId, false ) );
        }
        
        //------------------------------
        private void _forwardNotifyRemoveNodeMsg(final TNotifyRemoveNodeMsg msg) throws TException {
            
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _forwardLockMsg(final TLockParagraphMsg msg) throws TException {
            
            // paragraph is unlocked, _forward the message to right node
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _sendPositiveLockResultMsg(final TLockParagraphMsg msg) {
            
            connection.sendObjectToRightNode(
                connection.CMD,
                new TLockResultMsg( msg.InitiateNodeAddr, msg.ParagraphId, true ) );
        }
        
        //------------------------------
        private void _forwardNegativeLockResultMsg(final TLockResultMsg msg) {
            
            // message from others, _forward to left node
            connection.sendObjectToLeftNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _notifyViewNegativeLockResult() {
            
            // message from self
            mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(false) );
            TLogManager.logMessage("TWorkingNodeModel: lock paragraph failed");
        }
        
        //------------------------------
        private void _forwardPositiveLockResultMsg(final TLockResultMsg msg) {
            
            // message from others, _forward to right node
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _notifyViewLockPositiveResult() {
            
            // message from self
            mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(true) );
            TLogManager.logMessage("TWorkingNodeModel: lock paragraph done");
        }
        
        //------------------------------
        private void _sendNegativeCommitResultMsg(final TCommitParagraphMsg msg) {
            
            // oops, inconsistence locking state, commit failed and tell others
            connection.sendObjectToLeftNode(
                connection.CMD,
                new TCommitResultMsg( msg.InitiateNodeAddr, msg.ParagraphId, false ) );
        }
        
        //------------------------------
        private void _sendRequestParagraphMsg(final TCommitParagraphMsg msg) {
            
            // requet paragraph content from initiator
            connection.sendObject(
                connection.DATA,
                msg.InitiateNodeAddr.getAddress(),
                msg.DataPort,
                new TRequestParagraphMsg( mData.nodes.self().getSocketAddr(), mData.nodes.self().getDataPort(), msg.ParagraphId ) );
        }
        
        //------------------------------
        private void _forwardCommitMsg(final TCommitParagraphMsg msg) {
            
            // correct locking state, _forward the message
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _forwardInsertMsg(final TInsertParagraphMsg msg) {
            
            // correct locking state, _forward the message
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _forwardEraseMsg(final TEraseParagraphMsg msg) {
            
            // correct locking state, _forward the message
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _sendPositiveCommitResultMsg(final TCommitParagraphMsg msg) {
            
            // todo check all node receive the content before
            
            // message from self, commit success, tell others
            connection.sendObjectToRightNode(
                connection.CMD,
                new TCommitResultMsg(msg.InitiateNodeAddr, msg.ParagraphId, true) );
        }
        
        //------------------------------
        private void _forwardNegativeCommitResultMsg(final TCommitResultMsg msg) {
            
            // message from others, _forward to left node
            connection.sendObjectToLeftNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _notifyViewNegativeCommitResult() {
            
            // message from self
            mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(false) );
            TLogManager.logMessage("TWorkingNodeModel: commit paragraph failed");
        }
        
        //------------------------------
        private void _forwardPositiveCommitResultMsg(final TCommitResultMsg msg) {
            
            // message from others, _forward to right node
            connection.sendObjectToRightNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _notifyViewPositiveCommitResult() {
            
            // message from self
            mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(true) );
            TLogManager.logMessage("TWorkingNodeModel: commit paragraph done");
        }
        
        //------------------------------
        private void _forwardCancelLockParagraphMsg(final TCancelParagraphMsg msg) {
            
            // message from others, _forward to left node
            connection.sendObjectToLeftNode(connection.CMD, msg);
        }
        
        //------------------------------
        private void _notifyViewCancelLockResult() {
            
            // message from self
            mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(false) );
            TLogManager.logMessage("TWorkingNodeModel: cancel paragraph editing");
        }
        
        //------------------------------
        
    }
    
    //----------------------------------
    // Util
    private class Util {
        
        //------------------------------
        public void sleep(int time) {
            
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
        
        //------------------------------
        public String getSetting(String section, String key) {
            
            return mNode.getConfig().getSetting(section, key);
        }
        
        //------------------------------
        public boolean isSelf(InetSocketAddress addr) {
            
            return addr.equals( mData.nodes.self().getSocketAddr() );
        }
        
        //------------------------------
        
    }
    
    //----------------------------------
    // Signal
    private class Signal implements ISignalReceiverListener {
        
        private TSignalEmitter mAckEmitter;
        private TSignalEmitter mHBEmitter;
        
        private TSignalReceiver mAckReceiver;
        private TSignalReceiver mHBReceiver;
        
        private int mInterval;
        
        public void onSignalLost(TSignalReceiver receiver) {
            
            
            if(receiver == mAckReceiver) {
                
                TLogManager.logMessage("TWorkingNodeModel: Ack signal from right node is lost");
                protocol.onAckSignalLost();
            }
            
            else if(receiver == mHBReceiver) {
                
                TLogManager.logMessage("TWorkingNodeModel: HB signal from left node is lost");
                protocol.onHBSignalLost();
            }
            
        }
        
        public void startUp() {
            
            int maxHBSignalMiss = 4;
            int maxAckSignalMiss = 8;
            mInterval = 500;
            
            try {
                
                maxHBSignalMiss = Integer.parseInt( util.getSetting("Signal", "MaxHBMiss") );
                maxAckSignalMiss = Integer.parseInt( util.getSetting("Signal", "MaxAckMiss") );
                mInterval = Integer.parseInt( util.getSetting("Signal", "Interval") );
                
            } catch (NumberFormatException ex) {
                
            }
            
            TLogManager.logMessage("TWorkingNodeModel: signal interval = "
                + Integer.toString(mInterval)
                + "; maxHBSignalMiss = " + Integer.toString(maxHBSignalMiss) 
                + "; maxAckSignalMiss = " + Integer.toString(maxAckSignalMiss));
            
            mAckEmitter = new TSignalEmitter();
            mHBEmitter = new TSignalEmitter();
            
            mAckReceiver = new TSignalReceiver(maxAckSignalMiss);
            mHBReceiver = new TSignalReceiver(maxHBSignalMiss);
            
            mAckReceiver.addListener(this);
            mHBReceiver.addListener(this);
            
        }
        
        public void shutDown() {
            
            stop();
        }
        
        public void restart() {
            
            stop();
            start();
        }
        
        public void start() {
            
            TNodeInfo leftNode = mData.nodes.getLeft();
            TNodeInfo rightNode = mData.nodes.getRight();
            TNodeInfo self = mData.nodes.self();
            
            mHBEmitter.startup(
                new InetSocketAddress(rightNode.getAddr(), self.getCmdPort() ),
                mInterval);
            
            mHBReceiver.startup(
                leftNode.getCmdPort(),
                mInterval);
            
            mAckEmitter.startup(
                new InetSocketAddress(leftNode.getAddr(), self.getDataPort() ),
                mInterval);
            
            mAckReceiver.startup(
                rightNode.getDataPort(),
                mInterval);
            
        }
        
        public void stop() {
            
            mAckEmitter.shutdown();
            mHBEmitter.shutdown();
            
            mAckReceiver.shutdown();
            mHBReceiver.shutdown();
        }
        
    }
    
    //----------------------------------
    // Inner classes
    private Cmd cmd                 = new Cmd();
    private Connection connection   = new Connection();
    private Util util               = new Util();
    private Protocol protocol       = new Protocol();
    private Signal signal           = new Signal();
    
    private TNode mNode;
    private TWorkingNodeData mData;
    
    //----------------------------------
    public TWorkingNodeModel(TNode node) {
        
        mNode = node;
        mData = new TWorkingNodeData();
    }
    
    //----------------------------------
    public void startUp() throws TException {
        
        connection.startUp();
        
        signal.startUp();
        
        protocol.startUp();
        
        try {
            
            TNodeInfo selfNodeInfo = new TNodeInfo(
                
                util.getSetting("Working", "Username"),
                InetAddress.getLocalHost(),
                connection.mCmdPort,
                connection.mDataPort
                );
            
            mData.nodes.setSelf( selfNodeInfo );
            
        } catch(java.net.UnknownHostException e) {
            
        }
        
        protocol.acquireSessionsFromRegistry();
    }
    
    //----------------------------------
    public void shutDown() {
        
        protocol.shutDown();
        
        signal.shutDown();
        
        connection.shutDown();
    }
    
    //-------------------------------------------
    public TWorkingNodeData getData() {
        return mData;
    }
    
    //----------------------------------
    public void execute(TNodeCommand cmd) {
        
        this.cmd.execute(cmd);
    }
    
    //----------------------------------
    
}
