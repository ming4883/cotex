/*
 * TRequestParagraphMsg.java
 *
 * Created on April 9, 2007, 9:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

import cotex.TUniqueId;
import java.net.InetAddress;

/**
 *
 * @author Ming
 */
public class TRequestParagraphMsg implements java.io.Serializable {
    
    public InetAddress RequestorAddr;
    public TUniqueId ParagraphId;
    public int DataPort;
    
    /** Creates a new instance of TRequestParagraphMsg */
    public TRequestParagraphMsg(InetAddress requestorAddr, int port, TUniqueId paragraphId) {
        DataPort = port;
        RequestorAddr = requestorAddr;
        ParagraphId = paragraphId;
    }
    
     private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(RequestorAddr);
        out.writeObject(ParagraphId);
        out.writeInt(DataPort);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        
        RequestorAddr = (InetAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        DataPort = in.readInt();
    }
    
}
