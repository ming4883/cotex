/*
 * TSocketReceivingThread.java
 *
 * Created on 2007年4月6日, 下午 3:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

//import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

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
        
        try {
            
            ObjectInputStream inputStream = new ObjectInputStream( sock.getInputStream() );

            boolean active = true;

            while(active) {

                try {
                    Object obj = inputStream.readObject();
                    mConnection.fireObjectReceivedNotification(obj);
                    
                    this.sleep(1);
                }
                catch(ClassNotFoundException e) {
                    TLogManager.logMessage("TSocketReceivingThread(" + mClientName + "): unknown object recieved");
                }
                catch(InterruptedException e) {
                    active = false;
                }
                catch(IOException e) {
                    active = false;
                }
                
            }
            
            inputStream.close();
            sock.close();
        }
        catch(IOException e) {
        }
        
        mConnection.removeSocketAndWorkingThread(mClientName, this);
    }
}
