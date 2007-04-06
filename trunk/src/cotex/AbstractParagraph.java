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
    boolean lock=false,tryLock=false;
    public boolean getLock() {
        return lock;
    }
    public void setLock(boolean lock) {
        this.lock=lock;
    }
    public boolean getTryLock() {
        return tryLock;
    }
    public void setTryLock(boolean tryLock) {
        this.tryLock=tryLock;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
}
