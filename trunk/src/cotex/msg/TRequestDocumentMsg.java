/*
 * TRequestDocumentMsg.java
 *
 * Created on April 9, 2007, 12:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.TNodeInfo;

/**
 *
 * @author Ming
 */
public class TRequestDocumentMsg implements java.io.Serializable {
    
    public TNodeInfo NodeInfo;
    
    /** Creates a new instance of TRequestDocumentMsg */
    public TRequestDocumentMsg(TNodeInfo nodeInfo) {
        NodeInfo = nodeInfo;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(NodeInfo);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        NodeInfo = (TNodeInfo)in.readObject();
    }
    
}
