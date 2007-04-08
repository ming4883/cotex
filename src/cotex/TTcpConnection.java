/*
 * TTcpConnection.java
 *
 * Created on April 3, 2007, 6:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Ming
 */
public class TTcpConnection implements IConnection {
    
    IConnection.Mode mMode;
    private HashMap<String, Object> mSockets;
    private ArrayList<IConnectionListener> mListeners;
    private ArrayList<Thread> mThreads;
    
    private final String LISTEN_SOCKET_NAME = "Listen";
    
    /** Creates a new instance of TTcpConnection */
    public TTcpConnection() {
        mMode = IConnection.Mode.NONE;
        mSockets = new HashMap<String, Object>();
        mListeners = new ArrayList<IConnectionListener>();
        mThreads = new ArrayList<Thread>();
    }
    
    public synchronized void addListener(IConnectionListener listener) {
        mListeners.add(listener);
    }
    
    public synchronized void removeListener(IConnectionListener listener) {
        mListeners.remove(listener);
    }
    
    public synchronized void open(Mode mode, TConnectionInfo info) throws TException{
        
        mMode = mode;
        
        try{
            
            if(mMode == IConnection.Mode.ACTIVE) {
            
                Socket sock = new Socket( info.getAddress(), info.getPort() );
                TSocketReceivingThread receiveThread = new TSocketReceivingThread( this, info.getAddress().toString() );
                
                //mSockets.put(info.getAddress().toString(), mSocket);
                //mThreads.add(receiveThread);
                addSocketAndWorkingThread(info.getAddress().toString(), sock, receiveThread);
                
                receiveThread.start();
            }
            else if(mMode == IConnection.Mode.PASSIVE){
                
                ServerSocket listenSock = new ServerSocket( info.getPort() );
                TSocketListeningThread listenThread = new TSocketListeningThread(this);
                
                //mSockets.put(LISTEN_SOCKET_NAME, listenSock);
                //mThreads.add(listenThread);
                addSocketAndWorkingThread(LISTEN_SOCKET_NAME, listenSock, listenThread);
                
                //listenThread.setDaemon(true);
                listenThread.start();
            }
            
            //TLogManager.logMessage("TTcpConnection: connection made");
            
        }
        catch(IOException e){
            TLogManager.logError("TTcpConnection Error: The socket can not be initialize. error:"+e.toString());
            throw new TException("TTcpConnection.open", "Socket cannot be initialized");
        }
    }
    
    public synchronized void open(Mode mode, java.net.InetAddress addr, Integer port) throws TException {
         
         open( mode, new TConnectionInfo(addr, port) );
     }
    
    public synchronized void close() {
        
        TLogManager.logMessage("TTcpConnection: closing connection...");
        
        // stop all threads    
        for (int i=0; i<mThreads.size(); ++i) {

            Thread tempThread = (Thread)mThreads.get(i);

            if(tempThread != null)
                tempThread.interrupt();
        }
        
        mThreads.clear();
        
        // close all sockets
        Iterator iter = mSockets.values().iterator();
        
        while(iter.hasNext()) {
            
            Object value = iter.next();
            
            try {
                
                if( value.getClass().equals(Socket.class) ) {
                    ((Socket)value).close();
                    
                }
                else if ( value.getClass().equals(ServerSocket.class) ) {
                    ((ServerSocket)value).close();
                }
            }
            catch (IOException e) {
                TLogManager.logError("TTcpConnection: error closing socket. " + e.getMessage() );
            }
            
        }
        
        mMode = Mode.NONE;
    
        TLogManager.logMessage("TTcpConnection: connection closed");
        
    }
    
    public synchronized boolean isOpened() {
        return mMode != IConnection.Mode.NONE;
    }
    
    public synchronized void sendObject(Object obj) throws TException {
        
        if( !isOpened() ) {
             throw new TException("TTcpConnection.sendObject", "Connection is closed" );
        }
        
        Iterator iter = mSockets.values().iterator();
        
        while(iter.hasNext()) {
            
            try {
                Object value = iter.next();
                
                if( value.getClass().equals(Socket.class) ) {
                    
                    synchronized(value) {
                        ObjectOutputStream outStream = new ObjectOutputStream( ((Socket)value).getOutputStream() );

                        outStream.writeObject(obj);
                        outStream.flush();
                        //outStream.close();
                    }
                }
                
            } catch (Exception e) {
                TLogManager.logError("TTcpConnection sendObject Error:" + e.getMessage() );
                throw new TException("TTcpConnection.sendObject", e.getMessage() );
            }
        }
        
        //TLogManager.logMessage("TTcpConnection sendObject success");
    }
    
    public synchronized Mode getMode() {
        return mMode;
    }
    
    public synchronized void addSocketAndWorkingThread(
        String key,
        Object socket,
        Thread thread) {
        
        mSockets.put(key, socket);
        mThreads.add(thread);
    }
    
    public synchronized void removeSocketAndWorkingThread(
        String key,
        Thread thread) {
        
        mSockets.remove(key);
        mThreads.remove(thread);
        
        if(mSockets.size() == 0) {
            TLogManager.logMessage("TTcpConnection all remote sockets closed, auto close Active-Connection");
            mMode = Mode.NONE;
        }
    }
    
    public synchronized ServerSocket getListenSocket() {
        
        if( mSockets.containsKey(LISTEN_SOCKET_NAME) )
            return (ServerSocket)mSockets.get(LISTEN_SOCKET_NAME);
        
        return null;
    }
    
    public synchronized Socket getSocket(String key) {
        
        if( mSockets.containsKey(key) )
            return (Socket)mSockets.get(key);
        
        return null;
        
    }
    
    public synchronized void fireRemoteConnectedNotification(TConnectionInfo info) {
        
        for(int i = 0; i < mListeners.size(); ++i) {
            
            mListeners.get(i).notifyRemoteConnected(this, info);
        }

    }
    
    public synchronized void fireObjectReceivedNotification(Object obj) {
        
        for(int i = 0; i < mListeners.size(); ++i) {
            
            mListeners.get(i).notifyObjectReceived(this, obj);
        }
        
    }
}
