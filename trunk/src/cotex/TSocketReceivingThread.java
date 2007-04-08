/*
 * TSocketReceivingThread.java
 *
 * Created on 2007年4月6日, 下午 3:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author wing
 */
public class TSocketReceivingThread extends Thread {
    
    private TTcpConnection mConnection;
    private String mClientName;
    
    /**
     * Creates a new instance of TSocketReceivingThread
     */
    public TSocketReceivingThread(TTcpConnection connection, String clientName) {
        this.mConnection = connection;
        this.mClientName = clientName;
    }
    
    public void run() {
        
        Socket sock = mConnection.getSocket(mClientName);
        
        ObjectInputStream inputStream = null;
        
        try {
            
            inputStream = new ObjectInputStream( sock.getInputStream() );
        } 
        catch (IOException ex) {
             TLogManager.logError("TSocketReceivingThread: failed to create ObjectInputStream; socket close");
        }
        
        if(null != inputStream) {
        
            boolean connected = true;

            while(connected) {

                try{

                    synchronized(sock) {
                    
                        Object obj = inputStream.readObject();
                        mConnection.fireObjectReceivedNotification(obj);
                    }

                }
                catch(IOException e){
                    TLogManager.logError("TSocketReceivingThread(" + mClientName +  "): IOException - "+e.toString());
                    connected = false;

                }
                catch(ClassNotFoundException cnfe){
                    TLogManager.logError("TSocketReceivingThread(" + mClientName +  "): ClassNotFoundException - " + cnfe.toString());

                }
            }
            
            try {
                inputStream.close();
            }
            catch (IOException ex) {
                TLogManager.logError("TSocketReceivingThread: failed to close ObjectInputStream");
            }
        }
        
        mConnection.removeSocketAndWorkingThread(mClientName, this);
    }
}
