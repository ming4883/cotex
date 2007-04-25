/*
 * TStopWatch.java
 *
 * Created on April 23, 2007, 7:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.signal;

/**
 *
 * @author Ming
 */
public class TStopWatch {
    
    private long mStartTime;
    private long mStopTime;
    
    //----------------------------------
    public TStopWatch() {
        
    }
    
    //----------------------------------
    public void startTiming() {
    
        mStartTime = System.currentTimeMillis();
    }
    
    //----------------------------------
    public void stopTiming() {
     
        mStopTime = System.currentTimeMillis();
    }
    
    //----------------------------------
    public long elapsedTime() {
        
        return mStopTime - mStartTime;
    }
    
    //----------------------------------
    public void waitUntilTimeup(long time2Wait) throws InterruptedException {
        
        long t = elapsedTime();
        
        if(t < 1)
            t = 1;
            
        Thread.sleep(t);
    }
}
