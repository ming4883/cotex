/*
 * TNodeInfo.java
 *
 * Created on April 7, 2007, 6:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.session;

import cotex.TUniqueId;
import java.net.InetAddress;

/**
 *
 * @author Ming
 */
public class TNodeInfo {
    
    private String mName;
    private TUniqueId mId;
    private InetAddress mAddr;
    
    /**
     * Creates a new instance of TNodeInfo
     */
    public TNodeInfo(String name, InetAddress addr) {
        mName = name;
        mAddr = addr;
        mId = new TUniqueId();
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
        
        TNodeInfo rhs = (TNodeInfo)obj;
        return mId.equals(rhs.mId);
    }
    
    public final String toString() {
        return mName;
    }
}
