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
import cotex.TSession;
import cotex.TSessionInfo;
import cotex.working.msg.*;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import java.net.InetAddress;

/**
 *
 * @author Ming
 */
public class TWorkingNodeModel implements INodeModel {
    
    //----------------------------------
    // Node Commands
    public static class TNewSessionCmd extends TNodeCommand {
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
    }
    
    public static class TEraseParagraphCmd extends TNodeCommand {
    }
    
    public static class TCancelParagraphCmd extends TNodeCommand {
        public TCancelParagraphCmd(TParagraph paragraph) {
            setArg("paragraph", paragraph);
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
        }
        
        //------------------------------
        public void execute(TNodeCommand cmd) {
            
            if( mCmdDispatcher.containsKey( cmd.getClass() ) )
                mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
        }
        
        //------------------------------
        private void _onExit(TNodeCommand cmd) {
            
            javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel._onExit Invoke");
        }
        
        //------------------------------
        private void _onNewSession(TNodeCommand cmd) {
            
        }
        
        //------------------------------
        private void _onJoinSession(TNodeCommand cmd) {
            
            try {
                
                TUniqueId sessionId = (TUniqueId)cmd.getArg("sessionId");
                
                TLogManager.logMessage("TWorkingNodeModel: connecting to session: " + sessionId.toString() + " ..." );
                
                protocol.acquireSessionWorkerList(sessionId);
                
                protocol.acquireCurrentDocument();
                
                TLogManager.logMessage("TWorkingNodeModel: session connected");
                
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
                        new TLockParagraphMsg( mData.nodes.self().getAddr(), paragraph.getId() ) );
                
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
                protocol.mNodeCommitCheckList.set( mData.nodes.self().getAddr() );
                
                connection.sendObjectToRightNode(
                        connection.CMD,
                        new TCommitParagraphMsg( mData.nodes.self().getAddr(), paragraph.getId() ) );
                
            } catch(TException e) {
                TLogManager.logException(e);
                
            }
        }
        
        //------------------------------
        private void _onInsertParagraph(TNodeCommand cmd) {
            
        }
        
        //------------------------------
        private void _onEraseParagraph(TNodeCommand cmd) {
            
        }
        
        //------------------------------
        private void _onCancelParagraph(TNodeCommand cmd) {
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: cancelling paragraph editing...");
                
                TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
                
                connection.sendObjectToLeftNode(
                    connection.CMD,
                    new TCancelParagraphMsg( mData.nodes.self().getAddr(), paragraph.getId() ) );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
    }
    
    //----------------------------------
    // Connection
    private class Connection {
        
        public final String REG = "Reg";
        public final String CMD = "Cmd";
        public final String DATA = "Data";
        
        private Integer mRegPort;
        private Integer mCmdPort;
        private Integer mDataPort;
        
        //------------------------------
        public void startUp() throws NumberFormatException, TException {
            
            mRegPort = Integer.parseInt( util.getSetting("General", "RegistryPort") );
            mCmdPort = Integer.parseInt( util.getSetting("General", "WorkingCmdPort") );
            mDataPort = Integer.parseInt( util.getSetting("General", "WorkingDataPort") );
            
            // setup listeners
            IConnectionListener regListener = new IConnectionListener() {
                public void notifyObjectReceived(IConnection conn, Object obj) {
                    protocol.onReceivedRegObject(obj);
                }
            };
            
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
            
            // add and open connections
            mNode.getConnectionManager().addConnection(REG).addListener(regListener);
            mNode.getConnectionManager().addConnection(CMD).addListener(cmdListener);
            mNode.getConnectionManager().addConnection(DATA).addListener(dataListener);
            
            try {
                
                get(REG).open(mRegPort);
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
        public IConnection get(String name) throws TException {
            return mNode.getConnectionManager().getConnection(name);
        }
        
        //------------------------------
        public void sendObject(String connectionName, InetAddress addr, Object obj) {
            
            try{
                get(connectionName).sendObject( addr, obj );
            } catch(TException e) {
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        public void sendObjectToRightNode(String connectionName, Object obj) {
            
            try{
                get(connectionName).sendObject( mData.nodes.getRight().getAddr(), obj );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
        }
        
        //------------------------------
        public void sendObjectToLeftNode(String connectionName, Object obj) {
            
            try{
                get(connectionName).sendObject( mData.nodes.getLeft().getAddr(), obj );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
            }
        }
        
    }
    
    //----------------------------------
    // Protocol
    private class Protocol {
        
        private TNodeCheckList mNodeCommitCheckList = null;
        
        //------------------------------
        public void acquireSessionsFromRegistry() {
            
            TLogManager.logMessage("TWorkingNodeModel: acquring sessions from registry");
            
            // todo: ask the registry node for sessions in step of hardcode
            TSessionInfo session = new TSessionInfo( util.getSetting("Temp", "DummySessionName" ) );
            
            mData.sessions.add( session );
            
        }
        
        //------------------------------
        public void acquireSessionWorkerList(TUniqueId sessionId) {
            
            try {
                
                TLogManager.logMessage("TWorkingNodeModel: acquring worker list from registry");
                
                // todo: request the work list from the registry node
                mData.sessions.setCurrent(sessionId);
                
                TSession session = mData.sessions.getCurrent();
                
                int workerCnt = Integer.parseInt( util.getSetting("Temp", "NoOfWorkers") );
                
                for(int i=0; i<workerCnt; ++i) {
                
                    session.AddNode(
                        new cotex.TNodeInfo(
                        util.getSetting("Temp", "WorkerName" + Integer.toString(i) ),
                        InetAddress.getByName( util.getSetting("Temp", "WorkerAddr" + Integer.toString(i) ) ) ) );
                }
                
                int selfIdx = Integer.parseInt( util.getSetting("Temp", "Self") );
                TLogManager.logMessage("TWorkingNodeModel: self = " + Integer.toString(selfIdx) );
                
                mData.nodes.setSelf( session.getNodeAt( selfIdx ) );
                
            } catch(TException e) {
                
                TLogManager.logException(e);
                
            } catch(Exception e) {
                
                TLogManager.logError("TWorkingNodeModel: failed to acquire sessions worker list: " + e.getMessage() );
                
            }
        }
        
        //------------------------------
        public void acquireCurrentDocument() {
            
            try {
            
                int selfIdx = Integer.parseInt( util.getSetting("Temp", "Self") );

                if(selfIdx == 0) {

                    TContent content;

                    mData.paragraphs.append( mData.paragraphs.createGap() );

                    mData.paragraphs.append( content = mData.paragraphs.createContent() );

                    mData.paragraphs.append( mData.paragraphs.createGap() );

                    content.setContent("Testing paragraph line 1\nTesting paragraph line 2");

                } else {

                    connection.sendObjectToLeftNode(
                            connection.DATA,
                            new TRequestDocumentMsg( mData.nodes.self() ) );

                }
            
            }
            catch(TException e) {
                
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        public void onReceivedRegObject(Object obj) {
            
            TLogManager.logMessage("TWorkingNodeModel: reg object received: " + obj.toString() );
            
        }
        
        //------------------------------
        public void onReceivedCmdObject(Object obj) {
            
            TLogManager.logMessage("TWorkingNodeModel: cmd object received: " + obj.toString() );
            
            if(obj.getClass().equals( TLockParagraphMsg.class) )
                _processLockParagraphMsg( (TLockParagraphMsg)obj );
            
            if(obj.getClass().equals( TLockResultMsg.class) )
                _processLockResultMsg( (TLockResultMsg)obj );
            
            if(obj.getClass().equals( TCommitParagraphMsg.class) )
                _processCommitParagraphMsg( (TCommitParagraphMsg)obj );
            
            if(obj.getClass().equals( TCommitResultMsg.class) )
                _processCommitResultMsg( (TCommitResultMsg)obj );
            
            if(obj.getClass().equals( TCancelParagraphMsg.class) )
                _processCancelParagraphMsg( (TCancelParagraphMsg)obj );
        }
        
        //------------------------------
        public void onReceivedDataObject(Object obj) {
            
            TLogManager.logMessage("TWorkingNodeModel: data object received: " + obj.toString() );
             
            if(obj.getClass().equals( TRequestDocumentMsg.class ) )
                _processRequestDocumentMsg( (TRequestDocumentMsg)obj );
            
            if(obj.getClass().equals( TReplyDocumentMsg.class ) )
                _processReplyDocumentMsg( (TReplyDocumentMsg)obj );
            
            if(obj.getClass().equals( TRequestParagraphMsg.class) )
                _processRequestParagraphMsg( (TRequestParagraphMsg)obj );
            
            if(obj.getClass().equals( TReplyParagraphMsg.class) )
                _processReplyParagraphMsg( (TReplyParagraphMsg)obj );
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
                    }
                    else
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
                        mData.nodes.getByAddr(msg.InitiateNodeAddr) );
                    
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
                    
                }
                else {
                    
                    // commit failed, rollback
                    mData.paragraphs.rollback( msg.ParagraphId );
                    
                    if( util.isSelf( msg.InitiateNodeAddr ) )
                        _notifyViewNegativeCommitResult();
                    else
                        _forwardNegativeCommitResultMsg(msg);
                    
                }
                
            }
            catch(TException e) {
                
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
                    msg.RequestorAddr,
                    new TReplyParagraphMsg( msg.ParagraphId, ( (TContent)paragraph ).getPendingContent() ) );
                
                mNodeCommitCheckList.set( msg.RequestorAddr );
            }
            catch(TException e) {
            
                TLogManager.logException(e);
            }
            
        }
        
        //------------------------------
        private void _processReplyParagraphMsg(TReplyParagraphMsg msg) {
            
            try {
                
                TParagraph paragraph = mData.paragraphs.getById(msg.ParagraphId);
                ( (TContent)paragraph ).setPendingContent( msg.Content );
            }
            catch(TException e) {
            
                TLogManager.logException(e);
            }
            //mData.paragraphs.setList( msg.ParagraphList );
        }
        
        //------------------------------
        private void _sendNegativeLockResultMsg(final TLockParagraphMsg msg) {
            
            // already locked, tell left node the failed result
            connection.sendObjectToLeftNode(
                connection.CMD,
                new TLockResultMsg( msg.InitiateNodeAddr, msg.ParagraphId, false ) );
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
                msg.InitiateNodeAddr,
                new TRequestParagraphMsg( mData.nodes.self().getAddr(), msg.ParagraphId ) );
        }

        //------------------------------
        private void _forwardCommitMsg(final TCommitParagraphMsg msg) {
            
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
        public boolean isSelf(InetAddress addr) {
        
            return addr.equals( mData.nodes.self().getAddr() );
        }
        
        //------------------------------
        
    }
    
    //----------------------------------
    // Inner classes
    private Cmd cmd                 = new Cmd();
    private Connection connection   = new Connection();
    private Util util               = new Util();
    private Protocol protocol       = new Protocol();
    
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
        
        protocol.acquireSessionsFromRegistry();
    }
    
    //----------------------------------
    public void shutDown() {
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
