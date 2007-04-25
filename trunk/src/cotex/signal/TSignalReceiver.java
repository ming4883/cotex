/*
 * TSignalReceiver.java
 *
 * Created on April 23, 2007, 7:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.signal;

import cotex.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Ming
 */
public class TSignalReceiver {
    
    //----------------------------------
    private DatagramSocket mSock = null;
    private DatagramPacket mPacket = null;
    private TStopWatch mStopWatch = null;
    private TMissCounter mMissCounter = null;
    private Thread mThread = null;
    private long mInterval;
    private ArrayList<ISignalReceiverListener> mListeners;
    
    //----------------------------------
    public TSignalReceiver(int maxPacketMiss) {
        
        mListeners = new ArrayList<ISignalReceiverListener>();
        mStopWatch = new TStopWatch();
        mMissCounter = new TMissCounter(maxPacketMiss);
    }
    
    //----------------------------------
    public void startup(int port, long interval) {
     
        int retry = 0;
        
        final int maxRetry = 10;
        
        while(true) {
        
            try {

                mInterval = interval;

                mSock = new DatagramSocket( port );
                mSock.setSoTimeout((int)interval);

                byte[] buf = new byte[8];
                mPacket = new DatagramPacket(buf, buf.length);

                TLogManager.logMessage("TSignalReceiver: start receiving signal from " + Integer.toString(port) );

                mMissCounter.reset();

                mThread = new ReceiverThread();
                mThread.start();
                
                break;

            } catch(java.io.IOException e) {

                retry++;
                
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ie) {
                }
                
                if(retry == maxRetry) {
                    TLogManager.logError(
                        "TSignalReceiver: failed to start receiving signal from " + 
                        Integer.toString(port) + "; retry = " +
                        Integer.toString(retry) + "; " +
                        e.toString() );
                    
                    break;
                }

            }
            
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
    private class ReceiverThread extends Thread {
    
        public void run() {
         
           while(true) {
             
                try {
                
                    if(null != mSock) {
                        
                        mStopWatch.startTiming();

                        mSock.receive(mPacket);
                            
                        //TLogManager.logMessage(
                        //    "TSignalReceiver: Signal received from " + mPacket.getSocketAddress().toString() );
                        
                        mStopWatch.stopTiming();

                        mMissCounter.reset();

                        mStopWatch.waitUntilTimeup(mInterval);
                        
                    }

                } catch(SocketTimeoutException e) {
                
                    mMissCounter.increment();
                    
                    if( mMissCounter.isLimitReached() )
                        fireSignalLostEvent();
                    
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
    public void addListener(ISignalReceiverListener listener) {
     
        mListeners.add(listener);
    }
    
    //----------------------------------
    public void removeListener(ISignalReceiverListener listener) {
     
        mListeners.remove(listener);
    }
    
    //----------------------------------
    private void fireSignalLostEvent() {
        
        Iterator<ISignalReceiverListener> iter = mListeners.iterator();
        
        while( iter.hasNext() ) {
            iter.next().onSignalLost(this);
        }
    }
    
    //----------------------------------
    
}
