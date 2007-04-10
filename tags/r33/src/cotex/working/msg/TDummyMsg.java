/*
 * TDummyMsg.java
 *
 * Created on April 7, 2007, 11:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

/**
 *
 * @author Ming
 */
public class TDummyMsg implements java.io.Serializable {
    
    /** Creates a new instance of TDummyMsg */
    public TDummyMsg() {
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
    }
}
