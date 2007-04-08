/*
 * TSessionInfo.java
 *
 * Created on April 7, 2007, 6:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.session;

import cotex.TUniqueId;
import cotex.TException;

/**
 *
 * @author Ming
 */
public class TSessionInfo implements java.io.Serializable {
    
    private String mName;
    private TUniqueId mId;
    
    /**
     * Creates a new instance of TSessionInfo
     */
    public TSessionInfo(String name) {
        mName = name;
        mId = new TUniqueId();
        
    }
    
    public final String toString() {
        return mName;
    }
    
    public final String getName() {
        return mName;
    }
    
    public final TUniqueId getId() {
        return mId;
    }
    
    public boolean equals(Object obj) {
        
        TSessionInfo rhs = (TSessionInfo)obj;
        
        return mId.equals(rhs.mId);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mId);
        out.writeObject(mName);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mId = (TUniqueId)in.readObject();
        mName = (String)in.readObject();
    }
}
