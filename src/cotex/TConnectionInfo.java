/*
 * TConnectionInfo.java
 *
 * Created on April 3, 2007, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.net.InetAddress;

/**
 *
 * @author Ming
 */
public class TConnectionInfo {
        
    InetAddress mAddr;
    Integer mPort;
    
    /** Creates a new instance of TConnectionInfo */
    public TConnectionInfo(InetAddress addr, int port) {
        mAddr = addr;
        mPort = Integer.valueOf(port);
    }
    
    public TConnectionInfo(InetAddress addr, Integer port) {
        mAddr = addr;
        mPort = port;
    }
    
    public InetAddress getAddress() {
        return mAddr;
    }
    
    public Integer getPort() {
        return mPort;
    }
    
    /** Create a new instance of TConnectionInfo by parseing a string **/
    public static TConnectionInfo fromString(String str) throws TException {
        
        String tokens[] = str.split(":");
        
        if(tokens.length != 2) {
            throw new TException("TConnectionInfo.fromString", "Invalid input string");
        }
        
        try {
        
            return new TConnectionInfo(
                InetAddress.getByName(tokens[0]),
                Integer.parseInt(tokens[1]) );
        }
        catch(Exception e) {
            throw new TException("TConnectionInfo.fromString", "Invalid input string"+e.getMessage());
        }
    }
    
}
