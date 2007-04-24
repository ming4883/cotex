/*
 * TNewSessionMsg.java
 *
 * Created on 2007年4月24日, 上午 2:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.msg;

/**
 *
 * @author cyrux
 */
public class TNewSessionMsg implements java.io.Serializable {
    
    public String sessionName;
    
    /** Creates a new instance of TNewSessionMsg */
    public TNewSessionMsg(String SessionName) {
        sessionName = SessionName;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeUTF(sessionName);
    }
    
    private void readObject(java.io.ObjectInputStream in)
    throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        sessionName = in.readUTF();
    }
}
