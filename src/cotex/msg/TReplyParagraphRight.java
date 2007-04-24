/*
 * TReplyParagraphRight.java
 *
 * Created on 2007年4月24日, 下午 5:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.TUniqueId;

/**
 *
 * @author cyrux
 */
public class TReplyParagraphRight  implements java.io.Serializable {
    public boolean Result;
    public TUniqueId ParagraphId;
    
    /** Creates a new instance of TReplyParagraphRight */
    public TReplyParagraphRight(TUniqueId paragraphId, boolean result) {
        ParagraphId = paragraphId;
        Result = result;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeBoolean(Result);
        out.writeObject(ParagraphId);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        Result = in.readBoolean();
        ParagraphId = (TUniqueId)in.readObject();
    }
}
