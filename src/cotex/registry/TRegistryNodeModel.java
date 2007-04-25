/*
 * TRegistryNodeModel.java
 *
 * Created on April 23, 2007, 10:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.registry;

import cotex.*;
import cotex.msg.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Ming
 */
public class TRegistryNodeModel implements INodeModel {
    
    //----------------------------------
    public class Data {
        
        private java.util.ArrayList<TSession> mSessions;
        public TreeModel treeModel;
        
        public Data() {
            
            mSessions = new java.util.ArrayList<TSession>();
            treeModel = new TreeModel();
        }
        
        public TUniqueId newSession(String sessionName) {
            
            TSession session = new TSession( new TSessionInfo(sessionName) );
            
            mSessions.add(session);
            
            treeModel.notifyContentChanged();
            
            return session.getInfo().getId();
            
        }
        
        public TSession getSessionById(TUniqueId id) {
         
            ArrayList<TSessionInfo> info = new ArrayList<TSessionInfo>();
            
            Iterator<TSession> iter = mSessions.iterator();
            
            while( iter.hasNext() ) {
                
                TSession session = iter.next();
                
                if( session.getInfo().getId().equals(id) )
                    return session;
            }
            
            return null;
        }
        
        public ArrayList<TSessionInfo> getSessionInfoList() {
            
         
            ArrayList<TSessionInfo> info = new ArrayList<TSessionInfo>();
            
            Iterator<TSession> iter = mSessions.iterator();
            
            while( iter.hasNext() ) {
                
                info.add( iter.next().getInfo() );
            }
            
            return info;
        }
        
        class TreeModel implements javax.swing.tree.TreeModel {
            
            private java.util.ArrayList<javax.swing.event.TreeModelListener> mListeners;
            
            public TreeModel() {
                
                mListeners = new java.util.ArrayList<javax.swing.event.TreeModelListener>();
            }
            
            public void addTreeModelListener(
                javax.swing.event.TreeModelListener listener) {
                
                mListeners.add(listener);
            }
            
            public void removeTreeModelListener(
                javax.swing.event.TreeModelListener listener) {
                
                mListeners.remove(listener);
            }
            
            public Object getChild(Object parent, int index) {
                
                if(parent == this) {
                 
                    return mSessions.get(index);
                }
                
                if( parent.getClass().equals( TSession.class ) ) {
                    
                    return ( (TSession)parent ).getNodeAt(index);
                }
                
                return null;
            }
            
            public int getChildCount(Object parent) {
                
                if(parent == this) {
                 
                    return mSessions.size();
                }
                
                if( parent.getClass().equals( TSession.class ) ) {
                    
                    return ( (TSession)parent ).getNodeCount();
                }
                
                return 0;
            }
            
            public int getIndexOfChild(Object parent, Object child) {
                
                if(parent == this) {
                 
                    return mSessions.indexOf(child);
                }
                
                if( parent.getClass().equals( TSession.class ) ) {
                    
                    return ((TSession)parent).indexOf( (TNodeInfo)child );
                }
                
                return -1;
            }
            
            public Object getRoot() {
                
                return this;
            }
            
            public boolean isLeaf(Object node) {
                
                if(node == this) {
                 
                    return false;
                }
                
                if( node.getClass().equals( TSession.class ) ) {
                    
                    return false;
                }
                
                return true;
            }
            
            public void valueForPathChanged(
                javax.swing.tree.TreePath path,
                Object newValue) {
            }
            
            public String toString() {
             
                return "Sessions";
            }
            
            public void notifyContentChanged() {
             
                try{
                Iterator<javax.swing.event.TreeModelListener> iter = mListeners.iterator();
                
                javax.swing.event.TreeModelEvent evt = 
                    new javax.swing.event.TreeModelEvent(this, new javax.swing.tree.TreePath(this));
                //javax.swing.event.TreeModelEvent evt = null;
                while( iter.hasNext() )
                    iter.next().treeStructureChanged(evt);
                
                }catch(Exception e) {
                 
                    TLogManager.logError("Error notifying tree model content changed");
                }
            }
        }
        
    }
    
    //----------------------------------
    private class Protocol {
        
        //------------------------------
        private java.net.ServerSocket mRegSock;
        private Integer mPort;
        private Thread mListenThread;
        
        //------------------------------
        public void startUp() {
            
            try {
                
                mPort = Integer.parseInt( mNode.getConfig().getSetting("General", "RegistryPort") );
                
                mRegSock = new java.net.ServerSocket(mPort);
                
                mListenThread = new ListenThread();
                
                mListenThread.start();
                
                TLogManager.logMessage( "Start accepting request" );
            } catch(java.io.IOException e) {
                
                
            }
        }
        
        //------------------------------
        public void shutDown() {
            
            mListenThread.interrupt();
            
            try {
                mRegSock.close();
            } catch(java.io.IOException e) {
            }
        }
        
        //------------------------------
        private class ListenThread extends Thread {
            
            public void run() {
                
                while(true) {
                    
                    try {
                        
                        java.net.Socket sock = mRegSock.accept();
                        
                        Thread.sleep(1);
                        
                        Thread thread = new ProcessRequestThread(sock);
                        thread.start();
                    } catch(java.io.IOException e) {
                        
                    } catch(InterruptedException e) {
                        
                        break;
                    }
                }
            }
        }
        
        //------------------------------
        private class ProcessRequestThread extends Thread {
            
            private java.net.Socket mSock;
            
            public ProcessRequestThread(java.net.Socket sock) {
                
                mSock = sock;
                
            }
            
            public void run() {
                
                TLogManager.logMessage( "Processing Request from " + mSock.getInetAddress().getHostAddress() );
                
                try {
                    
                    java.io.ObjectInputStream inStream = new java.io.ObjectInputStream(mSock.getInputStream());
                    java.io.ObjectOutputStream outStream = new java.io.ObjectOutputStream(mSock.getOutputStream());
                    
                    Object obj = inStream.readObject();
                    
                    if( obj.getClass().equals(TRequestSessionInfoMsg.class) ) {
                        _processRequestSessionInfoMsg( (TRequestSessionInfoMsg)obj, outStream );
                    }
                    
                    else if( obj.getClass().equals(TNewSessionMsg.class) ) {
                        _processNewSessionMsg( (TNewSessionMsg)obj, outStream );
                    }
                    
                    else if( obj.getClass().equals(TJoinSessionMsg.class) ) {
                        _processJoinSessionMsg( (TJoinSessionMsg)obj, outStream );
                    }
                    
                    else if( obj.getClass().equals(TLeaveSessionMsg.class) ) {
                        _processLeaveSessionMsg( (TLeaveSessionMsg)obj, outStream );
                    }
                    
                } catch(java.io.IOException e) {
                    
                } catch(java.lang.ClassNotFoundException e) {
                    
                }
            }
        }
        
        //------------------------------
        private void _sendReply(
            java.io.ObjectOutputStream outStream,
            Object reply)
            
            throws java.io.IOException {
            
            outStream.writeObject(reply);
            outStream.flush();
            
            TLogManager.logMessage("TRegistryNodeModel: sending reply = " + reply.toString() );
        }
        
        //------------------------------
        private void _notifyNode(
            TNodeInfo nodeInfo,
            Object msg) {
         
            try {
                java.net.Socket sock = new java.net.Socket(nodeInfo.getAddr(), nodeInfo.getCmdPort() );

                java.io.ObjectOutputStream outStream = new java.io.ObjectOutputStream( sock.getOutputStream() );

                outStream.writeObject(msg);
                outStream.flush();

                outStream.close();

                sock.close();
            
            } catch(java.io.IOException e) {
             
                TLogManager.logError("Failed to notify node: " + nodeInfo.toString() + "; msg = " + msg.toString() );
            }
        }
        
        //------------------------------
        private void _notifyAllNodesInSession(
            TSession session,
            Object msg) {
         
            final int cnt = session.getNodeCount();
            
            for(int i=0; i<cnt; ++i) {
                
                _notifyNode( session.getNodeAt(i), msg );
            
            }
        }
        
        //------------------------------
        private void _processRequestSessionInfoMsg(
            TRequestSessionInfoMsg msg,
            java.io.ObjectOutputStream outStream)
                
            throws java.io.IOException {
            
            TLogManager.logMessage("TRegistryNodeModel: handling request session info msg");
            
            ArrayList<TSessionInfo> info = data.getSessionInfoList();
        
            _sendReply( 
                outStream,
                new TReplySessionInfoMsg(info) );
        }
        
        //------------------------------
        private void _processNewSessionMsg(
            TNewSessionMsg msg,
            java.io.ObjectOutputStream outStream)
            
            throws java.io.IOException {
            
            TLogManager.logMessage("TRegistryNodeModel: handling new session msg, name = " + msg.sessionName);
            
            TUniqueId sessionId = data.newSession(msg.sessionName);
            
            _sendReply(outStream, new TReplyNewSessionMsg(sessionId) );
        }
        
        //------------------------------
        private void _processJoinSessionMsg(
            TJoinSessionMsg msg,
            java.io.ObjectOutputStream outStream)
            
            throws java.io.IOException {
            
            TLogManager.logMessage("TRegistryNodeModel: handling join session msg");
            
            TSession session2Join = data.getSessionById(msg.sessionId);
            
            if(null != session2Join) {
                
                _notifyAllNodesInSession( session2Join, new TNotifyAddNodeMsg(msg.workerInfo) );
                
                session2Join.addNode(msg.workerInfo);
                
                data.treeModel.notifyContentChanged();
                
                _sendReply( outStream, new TReplyJoinSessionMsg( true, session2Join.getList() ) );
                
            } else {
                
                _sendReply( outStream, new TReplyJoinSessionMsg(false, null) );
                
            }
        }
        
        //------------------------------
        private void _processLeaveSessionMsg(
            TLeaveSessionMsg msg,
            java.io.ObjectOutputStream outStream)
            
            throws java.io.IOException {
            
             TLogManager.logMessage("TRegistryNodeModel: handling leave session msg");
             
             TSession session2Leave = data.getSessionById(msg.sessionId);
            
            if(null != session2Leave) {
                 
                 session2Leave.removeNode(msg.workerInfo);
                 
                 data.treeModel.notifyContentChanged();
            }
             
             _sendReply( outStream, new TDummyMsg() );
        }
        
        //------------------------------
    }
    
    //----------------------------------
    public Data data = new Data();
    private Protocol protocol = new Protocol();
    
    private TNode mNode = null;
    
    //----------------------------------
    public TRegistryNodeModel(TNode node) {
        
        mNode = node;
    }
    
    //----------------------------------
    public void startUp() throws TException {
        
        protocol.startUp();
    }
    
    //----------------------------------
    public void shutDown() {
        
        protocol.shutDown();
    }
    
    //----------------------------------
    public void execute(TNodeCommand cmd) {
    }
    
}
