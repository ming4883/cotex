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
    
    private ArrayList<IConnectionListener> mListeners;
    private HashMap<String, Socket> mReceiveSocks;
    private ArrayList<Thread> mThreads;
    
    private ServerSocket mListenSock        = null;
    private Thread mListenThread            = null;
    private Integer mPort                   = null;
    
    private Socket mSendSock                = null;
    private ObjectOutputStream mSendOStream = null;
    
    /** Creates a new instance of TTcpConnection */
    public TTcpConnection() {
        
        mReceiveSocks   = new HashMap<String, Socket>();
        mListeners      = new ArrayList<IConnectionListener>();
        mThreads        = new ArrayList<Thread>();
    }
    
    //----------------------------------
    public synchronized void addListener(IConnectionListener listener) {
        mListeners.add(listener);
    }
    
    //----------------------------------
    public synchronized void removeListener(IConnectionListener listener) {
        mListeners.remove(listener);
    }
    
    //----------------------------------
    public synchronized void open(Integer port) throws TException{
        
        mPort = port;
        
        try {
            
            mListenSock = new ServerSocket(mPort);
            mListenThread = new TSocketListeningThread(this);
            mListenThread.start();
        }
        catch(IOException e){
            throw new TException("TTcpConnection.open", "ServerSocket cannot be initialized");
        }
    }
    
    //----------------------------------
    public synchronized void close() {
        
        TLogManager.logMessage("TTcpConnection: closing connection...");
        
        // stop all receiving threads    
        for (int i=0; i<mThreads.size(); ++i) {

            Thread thread = mThreads.get(i);

            if(thread != null)
                thread.interrupt();
        }
        
        mThreads.clear();
        
        // stop listening thread
        mListenThread.interrupt();
        mListenThread = null;
        
        // clear sockets
        mReceiveSocks.clear();
        mListenSock = null;
        mPort = null;
    
        TLogManager.logMessage("TTcpConnection: connection closed");
    }
    
    //----------------------------------
    public synchronized boolean isOpened() {
        return mListenSock != null;
    }
    
    //----------------------------------
    public synchronized void startSending(java.net.InetAddress addr) throws TException {
        
        if( !isOpened() ) {
            throw new TException("TTcpConnection.startSending", "connection is not openned");
        }
        
        if(null != mSendSock) {
            throw new TException("TTcpConnection.startSending", "sending already started");
        }
        
        try {
            mSendSock = new Socket(addr, mPort);
            mSendOStream = new ObjectOutputStream( mSendSock.getOutputStream() );
        }
        catch(IOException e) {
            
            mSendSock = null;
            mSendOStream = null;
            
            throw new TException("TTcpConnection.startSending", "failed to open socket for address " + addr.getHostAddress() );
        }    
    }
    
    //----------------------------------
    public synchronized void send(Object obj) throws TException {
        
        if(null == mSendSock) {
            throw new TException("TTcpConnection.send", "sending have not been started");
        }
        
        try {
            mSendOStream.writeObject(obj);
            mSendOStream.flush();
        }
        catch(IOException e) {
            
            throw new TException("TTcpConnection.send", "failed to send object: " + e.getMessage() );
        }

    }
    
    //----------------------------------
    public synchronized void stopSending(java.net.InetAddress addr) throws TException {
        
        if(null == mSendSock) {
            throw new TException("TTcpConnection.stopSending", "sending have not been started");
        }
        
        try {
            
            mSendOStream.close();
            mSendOStream = null;
        }
        catch(IOException e) {
            
            throw new TException("TTcpConnection.stopSending", "failed to close ostream " + e.getMessage() );
        }    
        
        try {
            
            mSendSock.close();
            mSendSock = null;
        }
        catch(IOException e) {
            
            throw new TException("TTcpConnection.stopSending", "failed to close socket " + e.getMessage() );
        }
    }
    
    //----------------------------------
    public synchronized void sendObject(InetAddress addr, Object obj) throws TException {
        
        startSending(addr);
        send(obj);
        stopSending(addr);
    }
    
    //----------------------------------
    public synchronized void addSocketAndWorkingThread(
        String key,
        Socket socket,
        Thread thread) {
        
        mReceiveSocks.put(key, socket);
        mThreads.add(thread);
    }
    
    //----------------------------------
    public synchronized void removeSocketAndWorkingThread(
        String key,
        Thread thread) {
        
        mReceiveSocks.remove(key);
        mThreads.remove(thread);
    }
    
    //----------------------------------
    public synchronized ServerSocket getListenSocket() {
        
        return mListenSock;
    }
    
    //----------------------------------
    public synchronized Socket getSocket(String key) {
        
        if( mReceiveSocks.containsKey(key) )
            return mReceiveSocks.get(key);
        
        return null;
    }
    
    //----------------------------------
    public synchronized void fireObjectReceivedNotification(Object obj) {
        
        for(int i = 0; i < mListeners.size(); ++i) {
            
            mListeners.get(i).notifyObjectReceived(this, obj);
        }
    }
    
    //----------------------------------
}
