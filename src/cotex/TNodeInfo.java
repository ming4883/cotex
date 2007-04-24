/*
 * TNodeInfo.java
 *
 * Created on April 7, 2007, 6:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 * @author Ming
 */
public class TNodeInfo implements java.io.Serializable {
    
    private String mName;
    private InetAddress mAddr;
    private InetSocketAddress mSockAddr;
    private int mCmdPort;
    private int mDataPort;
    
    static public TNodeInfo self = null;
    static public boolean DisplayAddress = true;
    
    /**
     * Creates a new instance of TNodeInfo
     */
    public TNodeInfo(String name, InetAddress addr, int cmdPort, int dataPort) {
        
        mName = name;
        mAddr = addr;
        mCmdPort = cmdPort;
        mDataPort = dataPort;
        
        updateSocketAddress();
    }
    
    public final int getCmdPort() {
     
        return mCmdPort;
    }
    
    public final int getDataPort() {
     
        return mDataPort;
    }
    
    public final String getName() {
        return mName;
    }
    
    public void Rename(String newName) {
        mName = newName;
    }
    
    public final InetAddress getAddr() {
        return mAddr;
    }
    
    public final InetSocketAddress getSocketAddr() {
     
        return mSockAddr;
    }
    
    public boolean equals(Object obj) {
        
        if(null == obj)
            return false;
        
        TNodeInfo rhs = (TNodeInfo)obj;
        
        return 
            ( mAddr.equals(rhs.mAddr) ) && 
            ( mCmdPort==rhs.mCmdPort ) && 
            ( mDataPort==rhs.mDataPort );
    }
    
    public final String toString() {
        
        String str = mName;
        
        if(DisplayAddress)
            str = str + " [" + mAddr.getHostAddress() + ":" + Integer.toString(mCmdPort) + "]";
        
        if( this.equals(self) )
            str = str + " [self]";
            
        return str;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(mName);
        out.writeObject(mAddr);
        out.writeInt(mCmdPort);
        out.writeInt(mDataPort);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        
        mName = (String)in.readObject();
        mAddr = (InetAddress)in.readObject();
        mCmdPort = (int)in.readInt();
        mDataPort = (int)in.readInt();
        
        updateSocketAddress();
    }
    
    private void updateSocketAddress() {
     
        mSockAddr = new InetSocketAddress(mAddr, mCmdPort);
    }
}
