/*
 * TInsertParagraphMsg.java
 *
 * Created on April 7, 2007, 5:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.*;
import cotex.working.TContent;
import cotex.working.TGap;
import java.net.InetSocketAddress;

/**
 *
 * @author Ming
 */
public class TInsertParagraphMsg implements java.io.Serializable {
    
    public InetSocketAddress InitiateNodeAddr;
    public TUniqueId ParagraphId;
    public TContent NewParagraph;
    public TGap NewGap;
    
    /** Creates a new instance of TInsertParagraphMsg */
    public TInsertParagraphMsg(InetSocketAddress initiateNodeAddr, TUniqueId paragraphId,TContent newParagraph,TGap newGap) {
        InitiateNodeAddr = initiateNodeAddr;
        ParagraphId = paragraphId;
        NewParagraph = newParagraph;
        NewGap = newGap;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(InitiateNodeAddr);
        out.writeObject(ParagraphId);
        out.writeObject(NewParagraph);
        out.writeObject(NewGap);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        InitiateNodeAddr = (InetSocketAddress)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        NewParagraph = (TContent)in.readObject();
        NewGap = (TGap)in.readObject();
    }
    
}

