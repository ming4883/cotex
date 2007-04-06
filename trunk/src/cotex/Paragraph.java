/*
 * Paragraph.java
 *
 * Created on 2007年3月29日, 上午 2:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author cyrux
 */
public class Paragraph extends AbstractParagraph{
    String content;
    /**
     * Creates a new instance of Paragraph
     */
    public Paragraph() {
        this.content="";
    }
    public Paragraph(int id, String content) {
        this.id=id;
        this.content=content;
    }
    public Paragraph(String content) {
        this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content=content;
    }
}
