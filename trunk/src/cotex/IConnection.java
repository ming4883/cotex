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
    
    enum Mode {
        NONE,
        PASSIVE,
        ACTIVE,
    }
    
    public abstract void addListener(IConnectionListener listener);
    public abstract void removeListener(IConnectionListener listener);
    public abstract void open(Mode mode, TConnectionInfo info) throws TException;
    public abstract void open(Mode mode, java.net.InetAddress addr, Integer port) throws TException;
    public abstract void close();
    public abstract boolean isOpened();
    public abstract void sendObject(Object obj) throws TException;
    public abstract Mode getMode();
}
