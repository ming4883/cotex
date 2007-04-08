/*
 * IConnectionListener.java
 *
 * Created on April 3, 2007, 6:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public interface IConnectionListener {
    
    public abstract void notifyRemoteConnected(IConnection connection, TConnectionInfo info);
    public abstract void notifyObjectReceived(IConnection connection, Object receivedObject);
    
}
