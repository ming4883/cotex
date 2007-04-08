/*
 * TSocketListeningThread.java
 *
 * Created on 2007年4月6日, 下午 3:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.net.*;
import java.io.*;

/**
 *
 * @author wing
 */
public class TSocketListeningThread extends Thread{
    
    private TTcpConnection mConnection;
    
    /**
     * Creates a new instance of TSocketListeningThread
     */
    public TSocketListeningThread(TTcpConnection connection){
        this.mConnection = connection;
    }
    
    public void run() {
        
        ServerSocket serverSock = mConnection.getListenSocket();
        
        if(null == serverSock)
            return;
        
        while(true) {
            
            try{
                
                Socket clientSock = serverSock.accept();
                
                String clientName = clientSock.getRemoteSocketAddress().toString();
                TSocketReceivingThread receiveThread = new TSocketReceivingThread(mConnection, clientName);
                
                mConnection.addSocketAndWorkingThread(clientName, clientSock, receiveThread);
                //mConnection.mSockets.put(clientName, clientSock);
                //mConnection.mThreads.add(receiveThread);
                
                mConnection.fireRemoteConnectedNotification(
                    new TConnectionInfo( clientSock.getInetAddress(), new Integer( clientSock.getPort() ) ) );
                
                receiveThread.start();
                
            }
            catch (IOException e){
                TLogManager.logError( "TSocketListeningThread: " + e.toString() );
                break;
            }
        }
        
    }
}
