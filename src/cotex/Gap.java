/*
 * Gap.java
 *
 * Created on 2007�~3��29��, �W�� 2:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author cyrux
 */
public class Gap extends AbstractParagraph{
    
    /**
     * Creates a new instance of Gap
     */
    public Gap() {
        this.id=0;
        this.lock=false;
    }
    public Gap(int id) {
        this.id=id;
        this.lock=false;
    }
}
