/*
 * IConnection.java
 *
 * Created on April 3, 2007, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public interface IConnection {
    
    public abstract void addListener(IConnectionListener listener);
    public abstract void removeListener(IConnectionListener listener);
    
    public abstract void open(Integer port) throws TException;
    public abstract void close();
    public abstract boolean isOpened();
     
    public abstract void startSending(java.net.InetAddress addr) throws TException;
    public abstract void send(Object obj) throws TException;
    public abstract void stopSending(java.net.InetAddress addr) throws TException;
    
    public abstract void sendObject(java.net.InetAddress addr, Object obj) throws TException;
}
