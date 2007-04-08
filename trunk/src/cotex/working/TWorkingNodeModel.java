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
import cotex.session.*;
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
    
    /** Command classes for INodeView **/
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
    
    /** dispatching interface **/
    private interface ICmdInvoke {
        
        public abstract void invoke( TNodeCommand cmd );
        
    }
    
    //----------------------------------
    private final String REG_CONNECTION = "Reg";
    private final String CMD_CONNECTION = "Cmd";
    private final String DATA_CONNECTION = "Data";
    
    //----------------------------------
    /** private variables **/
    private HashMap<Class, ICmdInvoke> mCmdDispatcher;
    private HashMap<String, Object> mStates;
    private TNode mNode;
    private TWorkingNodeData mData;
    
    private Integer mRegPort;
    private Integer mCmdPort;
    private Integer mDataPort;
    
    //----------------------------------
    public TWorkingNodeModel(TNode node) {
        
        mNode = node;
        mData = new TWorkingNodeData();
        
        mStates = new HashMap<String, Object>();
        
        _logic_initDispatcher();
        
    }
    
    //----------------------------------
    public void startUp() throws TException {
        
        _logic_startUpConnections();
        
        _logic_acquireSessionsFromRegistry();
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
        
        if( mCmdDispatcher.containsKey( cmd.getClass() ) )
            mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
    }
    
    //----------------------------------
    private void _util_sleep(int time) {
        
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    //----------------------------------
    private IConnection _util_getConnection(String name) throws TException {
        return mNode.getConnectionManager().getConnection(name);
    }
    
    //----------------------------------
    private void _util_sendObject(String connectionName, InetAddress addr, Object obj) {
       
        try{
            _util_getConnection(connectionName).sendObject( addr, obj );
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private void _util_sendObjectToRightNode(String connectionName, Object obj) {
       
        try{
            _util_getConnection(connectionName).sendObject( mData.nodeGetRight().getAddr(), obj );
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private void _util_sendObjectToLeftNode(String connectionName, Object obj) {
       
        try{
            _util_getConnection(connectionName).sendObject( mData.nodeGetLeft().getAddr(), obj );
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private String _util_getSetting(String section, String key) {
        
        return mNode.getConfig().getSetting(section, key);
    }
    
    //----------------------------------
    private void _logic_startUpConnections() throws NumberFormatException, TException {
        
        mRegPort = Integer.parseInt( _util_getSetting("General", "RegistryPort") );
        mCmdPort = Integer.parseInt( _util_getSetting("General", "WorkingCmdPort") );
        mDataPort = Integer.parseInt( _util_getSetting("General", "WorkingDataPort") );
        
        // setup listeners
        IConnectionListener regListener = new IConnectionListener() {
            public void notifyObjectReceived(IConnection conn, Object obj) {
                _onRegConnectionObjectReceived(obj);
            }
        };
        
        IConnectionListener cmdListener = new IConnectionListener() {
            public void notifyObjectReceived(IConnection conn, Object obj) {
                _onCmdConnectionObjectReceived(obj);
            }
        };
        
        IConnectionListener dataListener = new IConnectionListener() {
            public void notifyObjectReceived(IConnection conn, Object obj) {
                _onDataConnectionObjectReceived(obj);
            }
        };
        
        // add and open connections
        mNode.getConnectionManager().addConnection(REG_CONNECTION).addListener(regListener);
        mNode.getConnectionManager().addConnection(CMD_CONNECTION).addListener(cmdListener);
        mNode.getConnectionManager().addConnection(DATA_CONNECTION).addListener(dataListener);
        
        try {
            
            _util_getConnection(REG_CONNECTION).open(mRegPort);
            TLogManager.logMessage("TWorkingNodeModel: reg connection openned at port " + mRegPort.toString() );
            
            _util_getConnection(CMD_CONNECTION).open(mCmdPort);
            TLogManager.logMessage("TWorkingNodeModel: cmd connection openned at port " + mCmdPort.toString() );
            
            _util_getConnection(DATA_CONNECTION).open(mDataPort);
            TLogManager.logMessage("TWorkingNodeModel: data connection openned at port " + mDataPort.toString());
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
        
    }
    
    //----------------------------------
    private void _logic_acquireSessionsFromRegistry() {
        
        TLogManager.logMessage("TWorkingNodeModel: acquring sessions from registry");
        
        // todo: ask the registry node for sessions in step of hardcode
        TSessionInfo session = new TSessionInfo( _util_getSetting("Temp", "DummySessionName" ) );
        
        mData.sessionAdd( session );

    }
    
    //----------------------------------
    private void _logic_acquireSessionWorkerList(TUniqueId sessionId) {
    
        try {
            
            TLogManager.logMessage("TWorkingNodeModel: acquring worker list from registry");
        
            // todo: request the work list from the registry node
            mData.sessionSetCurrent(sessionId);
            
            TSession session = mData.sessionGetCurrent();
            
            session.AddNode( 
                new cotex.session.TNodeInfo( 
                    _util_getSetting("Temp", "WorkerName1"),
                    java.net.InetAddress.getByName( _util_getSetting("Temp", "WorkerAddr1") ) ) );
            
            session.AddNode( 
                new cotex.session.TNodeInfo( 
                    _util_getSetting("Temp", "WorkerName2"),
                    java.net.InetAddress.getByName( _util_getSetting("Temp", "WorkerAddr2") ) ) );
            
            int selfIdx = Integer.parseInt( _util_getSetting("Temp", "Self") );
            TLogManager.logMessage("TWorkingNodeModel: self = " + Integer.toString(selfIdx) );
            
            mData.nodeSetSelf( session.getNodeAt( selfIdx ) );
            
        }
        catch(TException e) {
            
            TLogManager.logException(e);
            
        }
        catch(Exception e) {
            
            TLogManager.logError("TWorkingNodeModel: failed to acquire sessions worker list: " + e.getMessage() );
            
        }
    }

    //----------------------------------
    private void _logic_acquireCurrentDocument() {
        
        int selfIdx = Integer.parseInt( _util_getSetting("Temp", "Self") );
        
        if(selfIdx == 0) {
            mData.paragraphAdd( new TGap() );
            mData.paragraphAdd( new TContent("This is a test paragraph 1.") );
        }
        else {
            
            _util_sendObjectToLeftNode(
                CMD_CONNECTION,
                new TRequestDocumentMsg( mData.nodeGetSelf() ) );
            
        }

    }
    
    //----------------------------------
    private void _onRegConnectionObjectReceived(Object obj) {
    
        TLogManager.logMessage("TWorkingNodeModel: reg connection object received: " + obj.toString() );
        
    }
    
    //----------------------------------
    private void _onCmdConnectionObjectReceived(Object obj) {
        
        TLogManager.logMessage("TWorkingNodeModel: cmd connection object received: " + obj.toString() );
        
        if(obj.getClass().equals( TLockParagraphMsg.class) )
            _process_LockParagraphMsg( (TLockParagraphMsg)obj );
        
        if(obj.getClass().equals( TCommitParagraphMsg.class) )
            _process_CommitParagraphMsg( (TCommitParagraphMsg)obj );
        
        if(obj.getClass().equals( TRequestDocumentMsg.class) )
            _process_RequestDocumentMsg( (TRequestDocumentMsg)obj );
    }
    
    //----------------------------------
    private void _onDataConnectionObjectReceived(Object obj) {
        
        TLogManager.logMessage("TWorkingNodeModel: data connection object received: " + obj.toString() );
        
        if(obj.getClass().equals( TReplyDocumentMsg.class) )
            _process_ReplyDocumentMsg( (TReplyDocumentMsg)obj );
    }
    
    //----------------------------------
    private void _logic_initDispatcher() {
        
        mCmdDispatcher = new HashMap<Class, ICmdInvoke>();
        
        // exit
        mCmdDispatcher.put(
                TExitCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_Exit(cmd);}
        } );
        
        // new session
        mCmdDispatcher.put(
                TNewSessionCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_NewSession(cmd);}
        } );
        
        // join session
        mCmdDispatcher.put(
                TJoinSessionCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_JoinSession(cmd);}
        } );
        
        // lock paragraph
        mCmdDispatcher.put(
                TLockParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_LockParagraph(cmd);}
        } );
        
        // commit paragraph
        mCmdDispatcher.put(
                TCommitParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_CommitParagraph(cmd);}
        } );
        
        // insert paragraph
        mCmdDispatcher.put(
                TInsertParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_InsertParagraph(cmd);}
        } );
        
        // erase paragraph
        mCmdDispatcher.put(
                TEraseParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {_execute_EraseParagraph(cmd);}
        } );
    }
    
    //----------------------------------
    private void _execute_Exit(TNodeCommand cmd) {
        
        javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel._execute_Exit Invoke");
    }
    
    //----------------------------------
    private void _execute_NewSession(TNodeCommand cmd) {
        
    }
    
    //----------------------------------
    private void _execute_JoinSession(TNodeCommand cmd) {
     
        try {
            
            TUniqueId sessionId = (TUniqueId)cmd.getArg("sessionId");

            TLogManager.logMessage("TWorkingNodeModel: connecting to session: " + sessionId.toString() + " ..." );

            _logic_acquireSessionWorkerList(sessionId);
            
            _logic_acquireCurrentDocument();
            
            TLogManager.logMessage("TWorkingNodeModel: session connected");
            
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private void _execute_LockParagraph(TNodeCommand cmd) {
        
        try {
            
            TLogManager.logMessage("TWorkingNodeModel: locking paragraph ...");
            
            TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
            
            if(paragraph.getState() != TParagraph.State.UNLOCKED) {
                mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(false) );
                return;
            }
            
            paragraph.notifyLocking();
            
            mStates.put("LockingParagraph", paragraph);
            
            _util_sendObjectToRightNode(
                CMD_CONNECTION,
                new TLockParagraphMsg( mData.nodeGetSelf().getId(), paragraph.getId() ) );
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
        
    }
    
    //----------------------------------
    private void _execute_CommitParagraph(TNodeCommand cmd) {
        
        
        try {
            
            TLogManager.logMessage("TWorkingNodeModel: committing paragraph");

            TParagraph paragraph = (TParagraph)cmd.getArg("paragraph");
            
            if(paragraph.getState() != TParagraph.State.LOCKED) {
                mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(false) );
                return;
            }
            
            mStates.put("CommittingParagraph", paragraph);
            
            _util_sendObjectToRightNode(
                CMD_CONNECTION,
                new TCommitParagraphMsg( mData.nodeGetSelf().getId(), paragraph.getId() ) );
            
        }
        catch(TException e) {
            TLogManager.logException(e);
            
        }
    }
    
    //----------------------------------
    private void _execute_InsertParagraph(TNodeCommand cmd) {
        
    }
    
    //----------------------------------
    private void _execute_EraseParagraph(TNodeCommand cmd) {
        
    }
    
    //----------------------------------
    private void _process_LockParagraphMsg(TLockParagraphMsg msg) {
        
        if( msg.InitiateNodeId.equals( mData.nodeGetSelf().getId() ) ) {
            
            TParagraph paragraph = (TParagraph)mStates.get("LockingParagraph");
            
            if( paragraph.getId().equals( msg.ParagraphId ) ) {
                
                // lock success
                try {
                    
                    paragraph.notifyLocked();

                    mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd(true) );
                    
                    mStates.put("LockingParagraph", null);
                    
                    TLogManager.logMessage("TWorkingNodeModel: lock paragraph done");
                    
                }
                catch (TException e) {
                }
            }
        }
        else {
            
            _util_sendObjectToRightNode(CMD_CONNECTION, msg);
        }
        
    }
    
    //----------------------------------
    private void _process_CommitParagraphMsg(TCommitParagraphMsg msg) {
        
        if( msg.InitiateNodeId.equals( mData.nodeGetSelf().getId() ) ) {
            
            TParagraph paragraph = (TParagraph)mStates.get("CommittingParagraph");
            
            if( paragraph.getId().equals( msg.ParagraphId ) ) {
                
                // commit success
                try {
                    
                    paragraph.notifyUnlocked();

                    mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd(true) );
                    
                    mStates.put("CommittingParagraph", null);
                    
                    TLogManager.logMessage("TWorkingNodeModel: commit paragraph done");
                    
                }
                catch (TException e) {
                }
            }
            
        }
        else {
            _util_sendObjectToRightNode(CMD_CONNECTION, msg);
            
        }
        
    }

    //----------------------------------
    private void _process_RequestDocumentMsg(TRequestDocumentMsg msg) {
        
        _util_sendObject(
            DATA_CONNECTION,
            msg.NodeInfo.getAddr(),
            new TReplyDocumentMsg( mData.paragraphGetList() ) );
        
    }
    
    //----------------------------------
    private void _process_ReplyDocumentMsg(TReplyDocumentMsg msg) {
        
        mData.paragraphSetList( msg.ParagraphList );
        
    }
    
    //----------------------------------

}
