/*
 * TCancelParagraphMsg.java
 *
 * Created on April 7, 2007, 5:36 PM
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
public class TCancelParagraphMsg implements java.io.Serializable {
    
    public InetSocketAddress InitiateNodeAddr;
    public TUniqueId ParagraphId;
    
    /** Creates a new instance of TCancelParagraphMsg */
    public TCancelParagraphMsg(InetSocketAddress initiateNodeAddr, TUniqueId paragraphId) {
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
        InitiateNodeAddr = (InetSocketAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
     }
    
}
