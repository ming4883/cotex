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
        
        boolean active = true;
        
        while(active) {
            
            try{
                
                Socket clientSock = serverSock.accept();
               
                String key = clientSock.getRemoteSocketAddress().toString();
                TSocketReceivingThread receiveThread = new TSocketReceivingThread(mConnection, key);
                
                mConnection.addSocketAndWorkingThread(key, clientSock, receiveThread);
                receiveThread.start();
                
                this.sleep(1);
            }
            catch (IOException e){
                TLogManager.logError( "TSocketListeningThread: " + e.toString() );
                break;
            }
            catch (InterruptedException e) {
                TLogManager.logMessage( "TSocketListeningThread: stop listening" );
                active = false;
            }
        }
        
    }
}
