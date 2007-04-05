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
    Socket mSocket;
    ServerSocket mServerSocket;
    
    /** Creates a new instance of TTcpConnection */
    public TTcpConnection() {
        mMode = IConnection.Mode.NONE;
    }
    
    public void addListener(IConnectionListener listener) {
        
    }
    
    public void removeListener(IConnectionListener listener) {
    }
    
    public void open(Mode mode, TConnectionInfo info) {
        mMode = mode;
        try{
            if(mMode == IConnection.Mode.ACTIVE) {
                mSocket = new Socket(info.getAddress(),info.getPort());
            }else if(mMode == IConnection.Mode.PASSIVE){
                mServerSocket = new ServerSocket(info.getPort());
                Socket tempSocket = mServerSocket.accept();
            }
            TLogManager.logMessage("TTcpConnection: connection made");
        }catch(IOException e){
            TLogManager.logError("TTcpConnection Error: The socket can not be initialize. error:"+e.toString());
        }
    }
    
    public void close() {
        try{
            if(mMode == IConnection.Mode.ACTIVE) {
                mSocket.close();
            }else if(mMode == IConnection.Mode.PASSIVE){
                mServerSocket.close();
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
            OutputStream o = mSocket.getOutputStream();
            ObjectOutput s = new ObjectOutputStream(o);
            
            s.writeObject(obj);
            s.flush();
            s.close();
            TLogManager.logMessage("TTcpConnection sendObject success");
        } catch (Exception e) {
            TLogManager.logError("TTcpConnection sendObject Error:"+e.toString());
        }
    }
    
    public Mode getMode() {
        return mMode;
    }
}
