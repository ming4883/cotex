/*
 * TJoinSessionMsg.java
 *
 * Created on 2007年4月24日, 上午 3:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

import cotex.TNodeInfo;
import cotex.TUniqueId;

/**
 *
 * @author cyrux
 */
public class TJoinSessionMsg implements java.io.Serializable {
    
    public TUniqueId sessionId;
    public TNodeInfo workerInfo;
    
    /** Creates a new instance of TJoinSessionMsg */
    public TJoinSessionMsg(TUniqueId SessionId, TNodeInfo WorkerInfo) {
        sessionId = SessionId;
        workerInfo = WorkerInfo;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(sessionId);
        out.writeObject(workerInfo);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        sessionId = (TUniqueId)in.readObject();
        workerInfo = (TNodeInfo)in.readObject();
    }
}
