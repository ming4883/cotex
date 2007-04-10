/*
 * TReplyParagraphMsg.java
 *
 * Created on April 9, 2007, 9:17 AM
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
public class TReplyParagraphMsg implements java.io.Serializable {
    
    public TUniqueId ParagraphId;
    public String Content;
    
    /** Creates a new instance of TReplyParagraphMsg */
    public TReplyParagraphMsg(TUniqueId paragraphId, String content) {
        ParagraphId = paragraphId;
        Content = content;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(ParagraphId);
        out.writeObject(Content);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        ParagraphId = (TUniqueId)in.readObject();
        Content = (String)in.readObject();
    }
    
}
