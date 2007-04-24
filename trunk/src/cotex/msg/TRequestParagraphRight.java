/*
 * TRequestParagraphRight.java
 *
 * Created on 2007年4月24日, 下午 5:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.TNodeInfo;
import cotex.TUniqueId;

/**
 *
 * @author cyrux
 */
public class TRequestParagraphRight  implements java.io.Serializable {
    
    public TUniqueId ParagraphId;
    public TNodeInfo RequestNodeInfo;
    
    /** Creates a new instance of TRequestParagraphRight */
    public TRequestParagraphRight(TUniqueId paragraphId, TNodeInfo requestNodeInfo) {
        ParagraphId = paragraphId;
        RequestNodeInfo = requestNodeInfo;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(RequestNodeInfo);
        out.writeObject(ParagraphId);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        RequestNodeInfo = (TNodeInfo)in.readObject();
        ParagraphId = (TUniqueId)in.readObject();
    }
}
