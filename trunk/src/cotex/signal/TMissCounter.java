/*
 * TMissCounter.java
 *
 * Created on April 23, 2007, 8:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.signal;

/**
 *
 * @author Ming
 */

public class TMissCounter {
    
    //----------------------------------
    private int mMissCnt = 0;
    private int mLimit;
    
    //----------------------------------
    public TMissCounter(int limit) {
        
        mLimit = limit;
        
        reset();
    }
    
    //----------------------------------
    public void reset() {
     
        mMissCnt = 0;
    }
    
    //----------------------------------
    public void increment() {
     
        mMissCnt++;
    }
    
    //----------------------------------
    public boolean isLimitReached() {
        
     
        return (mMissCnt >= mLimit);
    }
}
