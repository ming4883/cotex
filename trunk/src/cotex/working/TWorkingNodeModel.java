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
    }
    
    public static class TCommitParagraphCmd extends TNodeCommand {
    }
    
    public static class TInsertParagraphCmd extends TNodeCommand {
    }
    
    public static class TEraseParagraphCmd extends TNodeCommand {
    }
    
    /** dispatching interface **/
    private interface ICmdInvoke {
        
        public abstract void invoke( TNodeCommand cmd );
        
    }
    
    /** private variables **/
    HashMap<Class, ICmdInvoke> mCmdDispatcher;
    TNode mNode;
    TWorkingNodeData mData;
    
    private final String CMD_PASSIVE = "CmdP";
    private final String CMD_ACTIVE = "CmdA";
    
    private Integer mCmdPort;
    
    /** Creates a new instance of TWorkingNodeModel */
    public TWorkingNodeModel(TNode node) {
        
        mNode = node;
        mData = new TWorkingNodeData();
        initDispatcher();
        
    }
    
    //----------------------------------
    public void startUp() throws TException {
        
        startupConnections();
        
        acquireSessionsFromRegistry();
    }

    //----------------------------------
    private void startupConnections() throws TException, NumberFormatException, TException {
        
        mCmdPort = Integer.parseInt( mNode.getConfig().getSetting("General", "WorkingCmdPort") );
        
        // passive listener
        mNode.getConnectionManager().addConnection(CMD_PASSIVE).addListener(
            new IConnectionListener() {
            public void notifyRemoteConnected(IConnection conn, TConnectionInfo info) {
                notifyCmdPassiveRemoteConnected(info);
            }
            
            public void notifyObjectReceived(IConnection conn, Object obj) {
                notifyCmdPassiveObjectReceived(obj);
            }
        });
        
        // active listener
        mNode.getConnectionManager().addConnection(CMD_ACTIVE).addListener(
            new IConnectionListener() {
            public void notifyRemoteConnected(IConnection conn, TConnectionInfo info) {
                notifyCmdActiveRemoteConnected(info);
            }
            
            public void notifyObjectReceived(IConnection conn, Object obj) {
                notifyCmdActiveObjectReceived(obj);
            }
        });
        
        try {
        
            getConnection(CMD_PASSIVE).open(
                IConnection.Mode.PASSIVE, 
                java.net.InetAddress.getLocalHost(),
                mCmdPort );
        }
        catch(Exception e) {
            throw new TException("TWorkingNodeModel.startUp", "failed to open passive cmd connection");
        }
        
        TLogManager.logMessage("Start listening for cmd connection");
    }
    
    //----------------------------------
    public void shutDown() {
    }
    
    //-------------------------------------------
    public TWorkingNodeData getData() {
        return mData;
    }
    
    //----------------------------------
    private void sleepImpl(int time) {
        
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    //----------------------------------
    private IConnection getConnection(String name) throws TException {
        return mNode.getConnectionManager().getConnection(name);
    }
    
    //----------------------------------
    private void sendObject(String connectionName, Object obj) {
       
        try{
            getConnection(connectionName).sendObject( obj );
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private void acquireSessionsFromRegistry() {
        
        TLogManager.logMessage("Acquring sessions from registry");
        
        // todo: ask the registry node for sessions in step of hardcode
        TSessionInfo session = new TSessionInfo( mNode.getConfig().getSetting("Temp", "DummySessionName" ) );
        
        mData.addSessionInfo( session );

    }
    
    //----------------------------------
    private void acquireSessionWorkerList(TUniqueId sessionId) {
    
        try {
            
            TLogManager.logMessage("Acquring worker list from registry");
        
            // todo: request the work list from the registry node
            mData.setCurrentSession(sessionId);
            
            TSession session = mData.getCurrentSession();
            
            session.AddNode( 
                new cotex.session.TNodeInfo( 
                    mNode.getConfig().getSetting("Temp", "DummyWorkerName"),
                    java.net.InetAddress.getByName( mNode.getConfig().getSetting("Temp", "DummyWorkerAddr") ) ) );
            
            mData.setSelfNodeInfo( session.getNodeAt(0) );
            
        }
        catch(TException e) {
            
            TLogManager.logException(e);
            
        }
        catch(Exception e) {
            
            TLogManager.logError("failed to acquire sessions worker list: " + e.getMessage() );
            
        }
    }
    
    //----------------------------------
    private void establishCmdConnection() {
        
        try {
            
            TNodeInfo rightWorker = mData.getCurrentSession().getRightNode( mData.getSelfNodeInfo() );
            
            getConnection(CMD_ACTIVE).open(
                IConnection.Mode.ACTIVE, 
                rightWorker.getAddr(),
                mCmdPort);
        }
        catch(TException e) {
            TLogManager.logError("failed to establish cmd connection: socket error");
        }
        //catch(java.net.UnknownHostException e) {
        //    TLogManager.logError("failed to establish cmd connection: unknown host");
        //}

    }
    
    //----------------------------------
    private void notifyCmdPassiveRemoteConnected(TConnectionInfo info) {
        
        TLogManager.logMessage("Cmd Passive remote connected: " + info.toString() );
        
        sendObject(CMD_PASSIVE, new TDummyMsg() );
    }
    
    //----------------------------------
    private void notifyCmdPassiveObjectReceived(Object obj) {
        
        TLogManager.logMessage("Cmd Passive object received: " + obj.toString() );
        
        sleepImpl(5000);
        
        sendObject(CMD_PASSIVE, new TDummyMsg() );
    }
    
    //----------------------------------
    private void notifyCmdActiveRemoteConnected(TConnectionInfo info) {
        
        TLogManager.logMessage("Cmd Active remote connected: " + info.toString() );
    }
    
    //----------------------------------
    private void notifyCmdActiveObjectReceived(Object obj) {
        
        TLogManager.logMessage("Cmd Active object received: " + obj.toString() );
        
        sleepImpl(5000);
        
        sendObject(CMD_ACTIVE, new TDummyMsg() );
    }
    
    //----------------------------------
    private void initDispatcher() {
        
        mCmdDispatcher = new HashMap<Class, ICmdInvoke>();
        
        // exit
        mCmdDispatcher.put(
                TExitCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeExit(cmd);}
        } );
        
        // new session
        mCmdDispatcher.put(
                TNewSessionCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeNewSession(cmd);}
        } );
        
        // join session
        mCmdDispatcher.put(
                TJoinSessionCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeJoinSession(cmd);}
        } );
        
        // lock paragraph
        mCmdDispatcher.put(
                TLockParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeLockParagraph(cmd);}
        } );
        
        // commit paragraph
        mCmdDispatcher.put(
                TCommitParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeCommitParagraph(cmd);}
        } );
        
        // insert paragraph
        mCmdDispatcher.put(
                TInsertParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeInsertParagraph(cmd);}
        } );
        
        // erase paragraph
        mCmdDispatcher.put(
                TEraseParagraphCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeEraseParagraph(cmd);}
        } );
    }
    
    //----------------------------------
    public void execute(TNodeCommand cmd) {
        
        if( mCmdDispatcher.containsKey( cmd.getClass() ) )
            mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
    }
    
    //----------------------------------
    private void executeExit(TNodeCommand cmd) {
        
        javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel.executeExit Invoke");
    }
    
    //----------------------------------
    private void executeNewSession(TNodeCommand cmd) {
        
    }
    
    //----------------------------------
    private void executeJoinSession(TNodeCommand cmd) {
     
        try {
            
            TUniqueId sessionId = (TUniqueId)cmd.getArg("sessionId");

            TLogManager.logMessage("connecting to text-editing session: " + sessionId.toString() + " ..." );

            acquireSessionWorkerList(sessionId);
            
            establishCmdConnection();

            TLogManager.logMessage("connected to text-editing session");
            
        }
        catch(TException e) {
            TLogManager.logException(e);
        }
    }
    
    //----------------------------------
    private void executeLockParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: locking paragraph ...");
        
        try {
            
             // simulate the network delay
            Thread t = new Thread() {
                public void run() {
                    sleepImpl(1000);
                    mNode.execute( new TWorkingNodeView.TNotfiyLockResultCmd() );
                    TLogManager.logMessage("TWorkingNodeModel: lock paragraph done");
                }
            };
            
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //----------------------------------
    private void executeCommitParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: committing paragraph");
        
        try {
            
            // simulate the network delay
            Thread t = new Thread() {
                public void run() {
                    sleepImpl(1000);
                    mNode.execute( new TWorkingNodeView.TNotfiyCommitResultCmd() );
                    TLogManager.logMessage("TWorkingNodeModel: commit paragraph done");
                }
            };
            
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //----------------------------------
    private void executeInsertParagraph(TNodeCommand cmd) {
        
    }
    
    //----------------------------------
    private void executeEraseParagraph(TNodeCommand cmd) {
        
    }
    
    //----------------------------------

}
