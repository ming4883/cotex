/*
 * TNodeInfo.java
 *
 * Created on April 7, 2007, 6:27 PM
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
public class TNodeInfo implements java.io.Serializable {
    
    private String mName;
    private InetAddress mAddr;
    
    /**
     * Creates a new instance of TNodeInfo
     */
    public TNodeInfo(String name, InetAddress addr) {
        mName = name;
        mAddr = addr;
    }
    
    public final String getName() {
        return mName;
    }
    
    public void Rename(String newName) {
        mName = newName;
    }
    
    public final InetAddress getAddr() {
        return mAddr;
    }
    
    public boolean equals(Object obj) {
        
        if(null == obj)
            return false;
        
        TNodeInfo rhs = (TNodeInfo)obj;
        return mAddr.equals(rhs.mAddr);
    }
    
    public final String toString() {
        return mName + " [" + mAddr.getHostAddress() + "]";
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mName);
        out.writeObject(mAddr);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        
        mName = (String)in.readObject();
        mAddr = (InetAddress)in.readObject();
    }
}
