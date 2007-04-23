/*
 * TReplySessionInfoMsg.java
 *
 * Created on 2007年4月24日, 上午 12:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

import cotex.TSessionInfo;
import java.util.ArrayList;

/**
 *
 * @author cyrux
 */
public class TReplySessionInfoMsg implements java.io.Serializable{
    
    public ArrayList<TSessionInfo> sessionInfo;
    
    /** Creates a new instance of TReplySessionInfoMsg */
    public TReplySessionInfoMsg(ArrayList<TSessionInfo > SessionInfo) {
        sessionInfo=SessionInfo;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt( sessionInfo.size() );
        
        for(int i=0; i<sessionInfo.size(); ++i)
            out.writeObject( sessionInfo.get(i) );
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        int size = in.readInt();
        
        sessionInfo = new ArrayList<TSessionInfo>();
        
        for(int i=0; i<size; ++i)
            sessionInfo.add( (TSessionInfo)in.readObject() );
    }
}
