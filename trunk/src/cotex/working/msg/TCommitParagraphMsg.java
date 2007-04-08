/*
 * TCommitParagraphMsg.java
 *
 * Created on April 7, 2007, 5:37 PM
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
public class TCommitParagraphMsg implements java.io.Serializable {
    
    public TUniqueId InitiateNodeId;
    public TUniqueId ParagraphId;
    
    /** Creates a new instance of TCommitParagraphMsg */
    public TCommitParagraphMsg(TUniqueId initiateNodeId, TUniqueId paragraphId) {
        InitiateNodeId = initiateNodeId;
        ParagraphId = paragraphId;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(InitiateNodeId);
        out.writeObject(ParagraphId);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        InitiateNodeId = (TUniqueId)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
     }
    
}

