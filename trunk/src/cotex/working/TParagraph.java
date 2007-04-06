/*
 * TParagraph.java
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
public class TParagraph extends TParagraphBase{
    String mContent;
    /**
     * Creates a new instance of TParagraph
     */
    public TParagraph() {
        this.mContent="";
    }
    
    public TParagraph(int id, String content) {
        this.id = id;
        this.mContent = content;
    }
    
    public TParagraph(String content) {
        this.mContent = content;
    }
    
    public String getContent(){
        return this.mContent;
    }
    
    public void setContent(String content){
        this.mContent=content;
    }
}
