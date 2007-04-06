/*
 * AbstractParagraph.java
 *
 * Created on 2007年3月29日, 上午 2:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author cyrux
 */
public abstract class AbstractParagraph {
    int id=0;
    boolean lock=false;
    public boolean getLock() {
        return this.lock;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
}
