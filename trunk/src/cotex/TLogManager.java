/*
 * TLogManager.java
 *
 * Created on April 4, 2007, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Ming
 */
public class TLogManager {
    
    
    ArrayList<ILogger> mLoggers;
    static TLogManager msInst = null;
    
    /** Creates a new instance of TLogManager */
    private TLogManager() {
        
        mLoggers = new ArrayList<ILogger>();
        
    }
    
    public static synchronized TLogManager getInstance() {
        
        if(msInst == null)
            msInst = new TLogManager();
        
        return msInst;
    }
    
    public static synchronized void addLogger(ILogger logger) {
    
        getInstance().addLoggerImpl(logger);
        
    }
    
    public static synchronized void logMessage(String str) {
        
        getInstance().logMessageImpl(str);
        
    }
    
    public static synchronized void logError(String str) {
        
        getInstance().logErrorImpl(str);
        
    }
    
    public static synchronized void logException(TException e) {
        
        getInstance().logExceptionImpl(e);
        
    }
    
    private void addLoggerImpl(ILogger logger) {
    
        mLoggers.add(logger);
        
    }
    
    private void logMessageImpl(String str) {
        
        Iterator<ILogger> iter = mLoggers.iterator();
        
        while( iter.hasNext() ) {
            iter.next().logMessage(str);
        }
        
    }
    
    private void logErrorImpl(String str) {
        
        Iterator<ILogger> iter = mLoggers.iterator();
        
        while( iter.hasNext() ) {
            iter.next().logError(str);
        }
        
    }
    
    private void logExceptionImpl(TException e) {
        
        Iterator<ILogger> iter = mLoggers.iterator();
        
        while( iter.hasNext() ) {
            iter.next().logException(e);
        }
        
    }
    
}
