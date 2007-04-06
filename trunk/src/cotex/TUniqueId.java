/*
 * TUniqueId.java
 *
 * Created on April 6, 2007, 7:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.rmi.dgc.VMID;

/**
 *
 * @author Ming
 */
public class TUniqueId implements java.io.Serializable {
    
    VMID mId;
    
    /** Creates a new instance of TUniqueId */
    public TUniqueId() {
        mId = new VMID();
    }
    
    public boolean equals(TUniqueId rhs) {

        return mId.equals(rhs.mId);
    }
    
    public String toString() {
        
        return mId.toString();
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mId);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mId = (VMID)in.readObject();
    }
    
}
