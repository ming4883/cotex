/*
 * TLeaveSessionMsg.java
 *
 * Created on April 25, 2007, 11:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.*;

/**
 *
 * @author Ming
 */
public class TLeaveSessionMsg implements java.io.Serializable {
    
    public TUniqueId sessionId;
    public TNodeInfo workerInfo;
    
    /** Creates a new instance of TLeaveSessionMsg */
    public TLeaveSessionMsg(TUniqueId SessionId, TNodeInfo WorkerInfo) {
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
