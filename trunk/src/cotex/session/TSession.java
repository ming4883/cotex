/*
 * TSession.java
 *
 * Created on April 7, 2007, 10:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.session;

import cotex.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Ming
 */
public class TSession {
    
    ArrayList<TNodeInfo> mNodes;
    TSessionInfo mInfo;
    
    /** Creates a new instance of TSession */
    public TSession(TSessionInfo info) {
         mNodes = new ArrayList<TNodeInfo>();
    }
    
    public final TSessionInfo getInfo() {
        return mInfo;
    }
    
    public void AddNode(TNodeInfo node) {
        mNodes.add(node);
    }
    
    public void RemoveNode(TNodeInfo node) {
        
        mNodes.remove(node);
        
    }
    
    public TNodeInfo getLeftNode(TNodeInfo node) throws TException {
        
        int idx = indexOf(node);
        
        if (idx == -1)
            throw new TException("TSession.getLeftWorker", "Worker not exist.");
        
        if (idx == 0)
            return mNodes.get( mNodes.size()  - 1 );
        
        return mNodes.get(idx - 1);
    }

    
    public TNodeInfo getNodeAt(int index) {
        return mNodes.get(index);
    }

    
    public int getNodeCount() {
        return mNodes.size();
    }

    
    public TNodeInfo getRightNode(TNodeInfo node) throws TException {
        
        int idx = indexOf(node);
        
        if (idx == -1)
            throw new TException("TSession.getRightWorker", "Worker not exist.");
        
        if(idx == mNodes.size() - 1)
            return mNodes.get(0);
        
        return mNodes.get(idx + 1);
    }

    
    public int indexOf(TNodeInfo node) {
        
        for (int i = 0; i < mNodes.size(); ++i) {
            if (mNodes.get(i).equals(node) )
                return i;
        }
        
        return -1;
        
    }
    
}
