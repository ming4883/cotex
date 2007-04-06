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
public class TGap extends TParagraphBase{
    
    /**
     * Creates a new instance of TGap
     */
    public TGap() {
        this.id=0;
        this.lock=false;
    }
    public TGap(int id) {
        this.id=id;
        this.lock=false;
    }
}
