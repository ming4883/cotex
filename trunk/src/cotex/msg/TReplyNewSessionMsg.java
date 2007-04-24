/*
 * TReplyNewSessionMsg.java
 *
 * Created on 2007年4月24日, 上午 2:36
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
public class TReplyNewSessionMsg  implements java.io.Serializable {
    
    public TUniqueId sessionId;
    
    /** Creates a new instance of TReplyNewSessionMsg */
    public TReplyNewSessionMsg(TUniqueId SessionId) {
        sessionId = SessionId;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(sessionId);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        sessionId = (TUniqueId)in.readObject();
    }
}
