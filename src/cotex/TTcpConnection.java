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

/**
 *
 * @author Ming
 */
public class TTcpConnection implements IConnection {
    
    IConnection.Mode mMode;
    
    /** Creates a new instance of TTcpConnection */
    public TTcpConnection() {
        mMode = IConnection.Mode.NONE;
    }
    
    public void addListener(IConnectionListener listener) {
    }
    
    public void removeListener(IConnectionListener listener) {
    }
    
    public void open(Mode mode, TConnectionInfo info) {
    }

    public void close() {
    }

    public boolean isOpened() {
        return mMode != IConnection.Mode.NONE;
    }

    public void sendObject(Object obj) {
    }

    public Mode getMode() {
        return mMode;
    }
}
