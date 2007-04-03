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
    
    
    private HashMap mConnections;
    private String mTypeName;
    
    /** Creates a new instance of TConnectionManager */
    public TConnectionManager(String connectionTypeName) {
        
        mTypeName = connectionTypeName;
        mConnections = new HashMap();
        
    }
    
    public void addConnection(String name) throws TException {
        
        IConnection connection = TConnectionFactory.createInstance(mTypeName);
        mConnections.put(name, connection);
    }
    
    public void removeConnection(String name) {
        mConnections.remove(name);
    }
    
    public IConnection getConnection(String name) {
        return (IConnection)mConnections.get(name);
    }
    
    public boolean hasConnection(String name) {
        return mConnections.containsKey(name);
    }
    
}
