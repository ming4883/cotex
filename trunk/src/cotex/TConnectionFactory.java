/*
 * TConnectionFactory.java
 *
 * Created on April 3, 2007, 6:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public class TConnectionFactory {
    
    /** Creates a new instance of TConnectionFactory */
    private TConnectionFactory() {
    }
    
    public static IConnection createInstance(String typeName) throws TException {
        
        if(typeName == "tcp")
            return new TTcpConnection();
        
            
        throw new TException("TConnectionFactory.createInstance", "Unknown connection type: " + typeName);
        //return null;
    }
    
}
