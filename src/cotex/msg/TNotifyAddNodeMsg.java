/*
 * TNotifyAddNodeMsg.java
 *
 * Created on April 24, 2007, 4:40 PM
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
public class TNotifyAddNodeMsg implements java.io.Serializable {
    
    public TNodeInfo nodeInfo;
    
    /** Creates a new instance of TJoinSessionMsg */
    public TNotifyAddNodeMsg(TNodeInfo NodeInfo) {
        nodeInfo = NodeInfo;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(nodeInfo);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        nodeInfo = (TNodeInfo)in.readObject();
    }
}
