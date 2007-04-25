/*
 * TNotifyRemoveNodeMsg.java
 *
 * Created on April 24, 2007, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.*;
import java.net.InetSocketAddress;

/**
 *
 * @author Ming
 */
public class TNotifyRemoveNodeMsg implements java.io.Serializable {
    
    public TNodeInfo nodeInfo;
    public InetSocketAddress initiateNodeAddr;
    
    /** Creates a new instance of TJoinSessionMsg */
    public TNotifyRemoveNodeMsg(TNodeInfo NodeInfo, InetSocketAddress InitiateNodeAddr) {
        nodeInfo = NodeInfo;
        initiateNodeAddr = InitiateNodeAddr;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(nodeInfo);
        out.writeObject(initiateNodeAddr);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        nodeInfo = (TNodeInfo)in.readObject();
        initiateNodeAddr = (InetSocketAddress)in.readObject();
    }
}
