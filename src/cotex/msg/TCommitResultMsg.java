/*
 * TCommitResultMsg.java
 *
 * Created on April 9, 2007, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.*;
import java.net.InetSocketAddress;

/**
 *
 * @author Ming
 */
public class TCommitResultMsg implements java.io.Serializable {
    
    public InetSocketAddress InitiateNodeAddr;
    public TUniqueId ParagraphId;
    public boolean Result;
    
    /** Creates a new instance of TLockResultMsg */
    public TCommitResultMsg(InetSocketAddress initiateNodeAddr, TUniqueId paragraphId, boolean result) {
        
        InitiateNodeAddr = initiateNodeAddr;
        ParagraphId = paragraphId;
        Result = result;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(InitiateNodeAddr);
        out.writeObject(ParagraphId);
        out.writeBoolean(Result);
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        InitiateNodeAddr = (InetSocketAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        Result = in.readBoolean();
        
    }
}
