/*
 * TLockResultMsg.java
 *
 * Created on April 9, 2007, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

import cotex.*;

/**
 *
 * @author Ming
 */
public class TLockResultMsg implements java.io.Serializable {
    
    public TUniqueId InitiateNodeId;
    public TUniqueId ParagraphId;
    public boolean Result;
    
    /** Creates a new instance of TLockResultMsg */
    public TLockResultMsg(TUniqueId initiateNodeId, TUniqueId paragraphId, boolean result) {
        
        InitiateNodeId = initiateNodeId;
        ParagraphId = paragraphId;
        Result = result;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(InitiateNodeId);
        out.writeObject(ParagraphId);
        out.writeBoolean(Result);
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        InitiateNodeId = (TUniqueId)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        Result = in.readBoolean();
        
    }
}