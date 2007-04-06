/*
 * ListenerThread.java
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
public class ListenerThread extends Thread{
    TTcpConnection connection;
    /** Creates a new instance of ListenerThread */
    public ListenerThread() {
    }
    
    public ListenerThread(TTcpConnection connection){
        this.connection = connection;
    }
    
    public void run() {
        while(true) {
            try{
                Socket clientSock = ( (ServerSocket)connection.mSockets.get("Listen") ).accept();
                
                for(int i=0; i<connection.mListeners.size(); ++i) {
                    IConnectionListener listener = connection.mListeners.get(i);
                    listener.notifyRemoteConnected(TConnectionInfo.fromString(clientSock.getRemoteSocketAddress().toString().substring(1)));
                }
                String clientName = clientSock.getRemoteSocketAddress().toString();
                connection.mSockets.put(clientName, clientSock);
                ReceiveThread receiveThread = new ReceiveThread(connection, clientName);
                connection.mThreads.add(receiveThread);
                receiveThread.start();
            }catch (IOException e){
                TLogManager.logError("ListenerThread Error: "+e.toString());
                break;
            }catch (TException te){
                break;
            } 
        }
        
    }
}
