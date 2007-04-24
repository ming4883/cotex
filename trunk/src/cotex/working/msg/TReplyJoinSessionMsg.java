/*
 * TReplyJoinSessionMsg.java
 *
 * Created on 2007年4月24日, 上午 3:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

import cotex.TNodeInfo;
import java.util.ArrayList;

/**
 *
 * @author cyrux
 */
public class TReplyJoinSessionMsg implements java.io.Serializable {
    
    public ArrayList<TNodeInfo> existingNodes;
    
    /** Creates a new instance of TReplyJoinSessionMsg */
    public TReplyJoinSessionMsg(ArrayList<TNodeInfo> ExistingNodes) {
        existingNodes = ExistingNodes;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt( existingNodes.size() );
        
        for(int i=0; i<existingNodes.size(); ++i)
            out.writeObject( existingNodes.get(i) );
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        int size = in.readInt();
        
        existingNodes = new ArrayList<TNodeInfo>();
        
        for(int i=0; i<size; ++i)
            existingNodes.add( (TNodeInfo)in.readObject() );
    }
}
