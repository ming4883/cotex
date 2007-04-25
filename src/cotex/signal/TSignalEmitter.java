/*
 * TSignalEmitter.java
 *
 * Created on April 23, 2007, 7:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.signal;

import cotex.*;
import java.net.*;

/**
 *
 * @author Ming
 */
public class TSignalEmitter {
    
    //----------------------------------
    private DatagramSocket mSock = null;
    private DatagramPacket mPacket = null;
    private TStopWatch mStopWatch = null;
    private Thread mThread = null;
    private long mInterval;
    private long mDelay;
    
    //----------------------------------
    public TSignalEmitter() {
        
        mStopWatch = new TStopWatch();
    }
    
    //----------------------------------
    public void startup(InetSocketAddress destAddr, long interval) {
     
        try {
            
            mInterval = interval;
            mDelay = 0;
            
            mSock = new DatagramSocket();
            
            byte[] buf = new byte[8];
            mPacket = new DatagramPacket(buf, buf.length, destAddr);
            
            TLogManager.logMessage("TSignalEmitter: start emitting signal to " + destAddr.toString() );
            
            mThread = new EmitterThread();
            mThread.start();
            
        } catch(java.io.IOException e) {
        
            
        }
    }
    
    //----------------------------------
    public void shutdown() {
     
        if(null != mThread) {
            
            mThread.interrupt();
            mThread = null;
            
        }
        
        mPacket = null;
        
        if(null != mSock) {
        
            mSock.close();
            mSock = null;
        
        }

    }
    
    //----------------------------------
    private class EmitterThread extends Thread {
    
        public void run() {
         
            while(true) {
             
                try {
                
                    if(null != mSock) {
                        
                        mStopWatch.startTiming();

                        mSock.send(mPacket);

                        mStopWatch.stopTiming();

                        mStopWatch.waitUntilTimeup(mInterval + mDelay);

                    }
                    
                } catch(SocketException e) {
                    
                    // todo report error
                } catch(java.io.IOException e) {
                    
                    // todo report error
                } catch(InterruptedException e) {
                 
                    // todo report error
                    break;
                }
                
            }
        }
    }
    
    //----------------------------------
    public void setDelay(long delay) {
     
        mDelay = delay;
    }
    
    //----------------------------------
    
}
