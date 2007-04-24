/*
 * TLockParagraphMsg.java
 *
 * Created on April 7, 2007, 5:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.*;
import java.net.InetAddress;

/**
 *
 * @author Ming
 */
public class TLockParagraphMsg implements java.io.Serializable {
    
    public InetAddress InitiateNodeAddr;
    public TUniqueId ParagraphId;
    
    /** Creates a new instance of TLockParagraphMsg */
    public TLockParagraphMsg(InetAddress initiateNodeAddr, TUniqueId paragraphId) {
        InitiateNodeAddr = initiateNodeAddr;
        ParagraphId = paragraphId;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(InitiateNodeAddr);
        out.writeObject(ParagraphId);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        InitiateNodeAddr = (InetAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
     }
    
}
