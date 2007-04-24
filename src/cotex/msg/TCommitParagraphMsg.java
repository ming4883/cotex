/*
 * TCommitParagraphMsg.java
 *
 * Created on April 7, 2007, 5:37 PM
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
public class TCommitParagraphMsg implements java.io.Serializable {
    
    public InetSocketAddress InitiateNodeAddr;
    public TUniqueId ParagraphId;
    public int DataPort;
    
    /** Creates a new instance of TCommitParagraphMsg */
    public TCommitParagraphMsg(InetSocketAddress initiateNodeAddr, int port, TUniqueId paragraphId) {
        InitiateNodeAddr = initiateNodeAddr;
        ParagraphId = paragraphId;
        DataPort = port;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(InitiateNodeAddr);
        out.writeObject(ParagraphId);
        out.writeInt(DataPort);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        InitiateNodeAddr = (InetSocketAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        DataPort = in.readInt();
     }
    
}

