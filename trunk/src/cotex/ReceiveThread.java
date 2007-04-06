/*
 * ReceiveThread.java
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
public class ReceiveThread extends Thread {
    TTcpConnection connection;
    String clientName;
    /** Creates a new instance of ReceiveThread */
    public ReceiveThread() {
    }
    
    public ReceiveThread(TTcpConnection connection, String clientName){
        this.connection = connection;
        this.clientName = clientName;
    }
    
    public void run() {
        try{
            Socket sock = ((Socket)connection.mSockets.get(clientName));
            while(true) {
                InputStream o = sock.getInputStream();
                ObjectInput s = new ObjectInputStream(o);
                Object obj = s.readObject();
                s.close();
                for(int i=0; i<connection.mListeners.size(); ++i)
                {
                    connection.mListeners.get(i).notifyObjectReceived(obj);
                }  
            }
        }catch(IOException e){
            TLogManager.logError("ReceiveThread IO Exception Error: "+e.toString());
        }catch(ClassNotFoundException cnfe){
            TLogManager.logError("ReceiveThread Class Not Found Exception Error: "+cnfe.toString());
        }
    }
}
