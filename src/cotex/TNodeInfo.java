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

/**
 *
 * @author Ming
 */
public class TNodeInfo implements java.io.Serializable {
    
    private String mName;
    private InetAddress mAddr;
    private int mCmdPort;
    private int mRegPort;
    private int mDataPort;
    
    public final String REG = "Reg";
    public final String CMD = "Cmd";
    public final String DATA = "Data";
    
    /**
     * Creates a new instance of TNodeInfo
     */
    public TNodeInfo(String name, InetAddress addr, int cmdPort, int regPort, int dataPort) {
        mName = name;
        mAddr = addr;
        mCmdPort = cmdPort;
        mRegPort = regPort;
        mDataPort = dataPort;
    }
    
    public final int getPortByType(String type) {
        if(type==CMD)
            return mCmdPort;
        else if(type==REG)
            return mRegPort;
        else if(type==DATA)
            return mDataPort;
        
        return 0;
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
    
    public boolean equals(Object obj) {
        
        if(null == obj)
            return false;
        
        TNodeInfo rhs = (TNodeInfo)obj;
        return mAddr.equals(rhs.mAddr) && mCmdPort==rhs.mCmdPort && mDataPort==rhs.mDataPort && mRegPort==rhs.mRegPort && mName.equals(rhs.mName);
    }
    
    public final String toString() {
        return mName + " [" + mAddr.getHostAddress() + "]";
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        
        out.defaultWriteObject();
        out.writeObject(mName);
        out.writeObject(mAddr);
        out.writeInt(mCmdPort);
        out.writeInt(mRegPort);
        out.writeInt(mDataPort);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        
        mName = (String)in.readObject();
        mAddr = (InetAddress)in.readObject();
        mCmdPort = (int)in.readInt();
        mRegPort = (int)in.readInt();
        mDataPort = (int)in.readInt();
    }
}
