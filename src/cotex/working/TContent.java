/*
 * TContent.java
 *
 * Created on 2007年3月29日, 上午 2:10
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
public class TContent extends TParagraph implements java.io.Serializable {
    
    private String mContent;
    
    /**
     * Creates a new instance of TContent
     */
    public TContent() {
        this.mContent = "";
    }
    
    public TContent(String content) {
        this.mContent = content;
    }
    
    public String getContent() {
        return this.mContent;
    }
    
    public void setContent(String content) {
        this.mContent = content;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mContent);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mContent = (String)in.readObject();
    }
}
