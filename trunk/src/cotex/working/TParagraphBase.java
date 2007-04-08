/*
 * TParagraphBase.java
 *
 * Created on 2007年3月29日, 上午 2:03
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
public abstract class TParagraphBase {
    
    private TUniqueId mId;
    private boolean mLocked;
    
    TParagraphBase(){
        mId = new TUniqueId();
        mLocked = false;
    }
    
    public void lock() throws TException {
        
        if(mLocked)
            throw new TException("TParagraphBase.lock", "ParagraphBase is already locked");
        
        mLocked = true;
    }
    
    public void unlock() throws TException {
        
        if(!mLocked)
            throw new TException("TParagraphBase.unlock", "ParagraphBase has not been locked");
        
        mLocked = false;
    }
    
    public boolean isLocked() {
        return mLocked;
    }
    
    public TUniqueId getId(){
        return mId;
    }
}
