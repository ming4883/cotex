/*
 * TGap.java
 *
 * Created on 2007年3月29日, 上午 2:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;

/**
 *
 * @author cyrux
 */
public class TGap extends TParagraph implements java.io.Serializable {
    
    /**
     * Creates a new instance of TGap
     */
    public TGap() {
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
    }
}
