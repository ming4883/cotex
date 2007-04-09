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
import cotex.session.TNodeInfo;

/**
 *
 * @author Ming
 */
public class TRequestParagraphMsg implements java.io.Serializable {
    
    TNodeInfo NodeInfo;
    TUniqueId ParagraphId;
    
    /** Creates a new instance of TRequestParagraphMsg */
    public TRequestParagraphMsg(TNodeInfo nodeInfo, TUniqueId paragraphId) {
        
        NodeInfo = nodeInfo;
        ParagraphId = paragraphId;
    }
    
     private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(NodeInfo);
        out.writeObject(ParagraphId);
        
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        
        NodeInfo = (TNodeInfo)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
        
    }
    
}
