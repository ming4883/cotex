/*
 * TSession.java
 *
 * Created on April 7, 2007, 10:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 * @author Ming
 */
public class TSession {
    
    ArrayList<TNodeInfo> mNodes;
    TSessionInfo mInfo;
    
    /** Creates a new instance of TSession */
    public TSession(TSessionInfo info) {
        
        mInfo = info;
        mNodes = new ArrayList<TNodeInfo>();
    }
    
    public final TSessionInfo getInfo() {
        return mInfo;
    }
    
    public void addNode(TNodeInfo node) {
        mNodes.add(node);
    }
    
    public void removeNode(TNodeInfo node) {
        
        mNodes.remove(node);
        
    }
    
    public ArrayList<TNodeInfo> getList() {
     
        return mNodes;
    }
    
    public void setList(ArrayList<TNodeInfo> Nodes) {
        mNodes=Nodes;
    }
    
    public TNodeInfo getLeftNode(TNodeInfo node) throws TException {
        
        int idx = indexOf(node);
        if (idx == -1)
            throw new TException("TSession.getLeftWorker", "Worker not exist.");
        
        if (idx == 0)
            return mNodes.get( mNodes.size()  - 1 );
        
        return mNodes.get(idx - 1);
    }
    
    public TNodeInfo getRightNode(TNodeInfo node) throws TException {
        
        int idx = indexOf(node);
        
        if (idx == -1)
            throw new TException("TSession.getRightWorker", "Worker not exist.");
        
        if(idx == mNodes.size() - 1)
            return mNodes.get(0);
        
        return mNodes.get(idx + 1);
    }
    
    public TNodeInfo getNodeAt(int index) {
        return mNodes.get(index);
    }
    
    public int getNodeCount() {
        return mNodes.size();
    }
    
    public int indexOf(TNodeInfo node) {
        
        for (int i = 0; i < mNodes.size(); ++i) {
            if (mNodes.get(i).equals(node) )
                return i;
        }
        
        return -1;
        
    }
    
    public TNodeInfo getNodeBySocketAddr(InetSocketAddress addr) throws TException {
        
        Iterator<TNodeInfo> iter = mNodes.iterator();
        
        while( iter.hasNext() ) {
            
            TNodeInfo info = iter.next();
            
            if( info.getSocketAddr().equals(addr) )
                return info;
        }
        
        throw new TException("TSession.getNodeByAddr", "Node not found");
    }
}
