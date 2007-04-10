/*
 * TNodeCheckList.java
 *
 * Created on April 10, 2007, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;

import java.util.HashMap;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Ming
 */
public class TNodeCheckList {
    
    private HashMap<InetAddress, Boolean> mList;
    
    /** Creates a new instance of TNodeCheckList */
    public TNodeCheckList(TSession session) {
        
        mList = new HashMap<InetAddress, Boolean>();
        
        for(int i=0; i<session.getNodeCount(); ++i) {
            mList.put( session.getNodeAt(i).getAddr(), Boolean.FALSE );
        }
        
    }
    
    public synchronized void set(InetAddress addr) {
        
        TLogManager.logMessage( "TNodeCheckList: setting node: " + addr.toString() );
        
        
        mList.put(addr, Boolean.TRUE);
        
    }
    
    public synchronized boolean allSet() {
        
        Iterator<Boolean> iter = mList.values().iterator();
        
        while( iter.hasNext() ) {
            Boolean b = iter.next();
            
            if(b.booleanValue() == false)
                return false;
        }
        
        return true;
    }
    
    /*
    public synchronized void reset() {
        
        mList.entrySet().iterator();
        
    }
    */
    
}
