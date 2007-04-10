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
    private String mPendingContent;
    
    /**
     * Creates a new instance of TContent
     */
    public TContent() {
        mContent = "";
        mPendingContent = "";
    }
    
    public TContent(String content) {
        mContent = content;
    }
    
    public String getContent() {
        return mContent;
    }
    
    public void setContent(String content) {
        mContent = content;
    }
    
    public String getPendingContent() {
        return mPendingContent;
    }
    
    public void setPendingContent(String content) {
        mPendingContent = content;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mContent);
        //out.writeObject(mPendingContent);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mContent = (String)in.readObject();
        //mPendingContent = (String)in.readObject();
    }
}
