/*
 * TCommitParagraphMsg.java
 *
 * Created on April 7, 2007, 5:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

/**
 *
 * @author Ming
 */
public class TCommitParagraphMsg implements java.io.Serializable {
    
    /** Creates a new instance of TCommitParagraphMsg */
    public TCommitParagraphMsg() {
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
    }
}
