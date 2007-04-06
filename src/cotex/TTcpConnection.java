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
    public HashMap<String, Object> mSockets;
    public ArrayList<IConnectionListener> mListeners;
    public ArrayList<Thread> mThreads;
    
    /** Creates a new instance of TTcpConnection */
    public TTcpConnection() {
        mMode = IConnection.Mode.NONE;
        mSockets = new HashMap();
        mListeners = new ArrayList();
        mThreads = new ArrayList();
    }
    
    public void addListener(IConnectionListener listener) {
        mListeners.add(listener);
    }
    
    public void removeListener(IConnectionListener listener) {
        mListeners.remove(listener);
    }
    
    public void open(Mode mode, TConnectionInfo info) throws TException{
        mMode = mode;
        try{
            if(mMode == IConnection.Mode.ACTIVE) {
                Socket mSocket = new Socket(info.getAddress(),info.getPort());
                mSockets.put(info.getAddress().toString(), mSocket);
                ReceiveThread receiveThread = new ReceiveThread(this, info.getAddress().toString());
                mThreads.add(receiveThread);
                receiveThread.start();
            }else if(mMode == IConnection.Mode.PASSIVE){
                ServerSocket listenSock = new ServerSocket(info.getPort());
                mSockets.put("Listen", listenSock);
                
                ListenerThread listenThread = new ListenerThread(this);
                mThreads.add(listenThread);
                //listenThread.setDaemon(true);
                listenThread.start();
            }
            TLogManager.logMessage("TTcpConnection: connection made");
        }catch(IOException e){
            TLogManager.logError("TTcpConnection Error: The socket can not be initialize. error:"+e.toString());
            throw new TException("TTcpConnection.open", "Socket cannot be initialized");
        }
    }
    
    public void close() {
        try{
            
            for (int i=0; i<mThreads.size(); ++i){
                Thread tempThread = (Thread)mThreads.get(i);
                if(tempThread!=null)
                    tempThread.interrupt();
            }
            mThreads.clear();
            Iterator iter = mSockets.values().iterator();
            
            while(iter.hasNext()) {
                
                Object value = iter.next();
                
                if( value.getClass().equals(Socket.class) ) {
                    ((Socket)value).close();
                }else if ( value.getClass().equals(ServerSocket.class) ) {
                    ((ServerSocket)value).close();
                }
                
            }
            TLogManager.logMessage("TTcpConnection: connection closed");
        }catch(IOException e){
            TLogManager.logError("TTcpConnection Error: The socket can not be closed. error:"+e.toString());
        }
    }
    
    public boolean isOpened() {
        return mMode != IConnection.Mode.NONE;
    }
    
    public void sendObject(Object obj) {
        try {
            // Serialize today's date to a outputstream associated to the socket
            Iterator iter = mSockets.values().iterator();
            
            while(iter.hasNext()) {
                
                Object value = iter.next();
                
                if( value.getClass().equals(Socket.class) ) {
                    OutputStream o = ((Socket)value).getOutputStream();
                    ObjectOutput s = new ObjectOutputStream(o);
                    
                    s.writeObject(obj);
                    s.flush();
                    s.close();
                }
                
            }
            
            TLogManager.logMessage("TTcpConnection sendObject success");
        } catch (Exception e) {
            TLogManager.logError("TTcpConnection sendObject Error:"+e.toString());
        }
    }
    
    public Mode getMode() {
        return mMode;
    }
}
