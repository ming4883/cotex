/*
 * TConnectionManager.java
 *
 * Created on April 3, 2007, 6:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.util.HashMap;

/**
 *
 * @author Ming
 */
public class TConnectionManager {
    
    
    private HashMap<String, IConnection> mConnections;
    private String mTypeName;
    
    /** Creates a new instance of TConnectionManager */
    public TConnectionManager(String connectionTypeName) {
        
        mTypeName = connectionTypeName;
        mConnections = new HashMap<String, IConnection>();
        
        TLogManager.logMessage("TConnectionManager: connection type = " + mTypeName);
        
    }
    
    public IConnection addConnection(String name) throws TException {
        
        if( mConnections.containsKey(name) ) {
            throw new TException(
                "TConnectionManager.addConnection",
                "connection " + name + " already exists");
        }
        
        IConnection connection = TConnectionFactory.createInstance(mTypeName);
        mConnections.put(name, connection);
        
        return connection;
    }
    
    public void removeConnection(String name) throws TException {
        
        if( !mConnections.containsKey(name) ) {
            throw new TException(
                "TConnectionManager.removeConnection",
                "connection " + name + " does not exist");
        }
        
        mConnections.remove(name);
    }
    
    public IConnection getConnection(String name) throws TException {
        
        if( !mConnections.containsKey(name) ) {
            throw new TException(
                "TConnectionManager.removeConnection",
                "connection " + name + " does not exist");
        }
        
        return mConnections.get(name);
    }
    
    public boolean hasConnection(String name) {
        return mConnections.containsKey(name);
    }
    
}
