/*
 * TRequestSessionInfoMsg.java
 *
 * Created on 2007�~4��24��, �W�� 12:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

/**
 *
 * @author cyrux
 */
public class TRequestSessionInfoMsg implements java.io.Serializable{
    
    /** Creates a new instance of TRequestSessionInfoMsg */
    public TRequestSessionInfoMsg() {
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
