/*
 * ILogger.java
 *
 * Created on April 4, 2007, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public interface ILogger {
    
    public abstract void logMessage(String str);
    public abstract void logError(String str);
    public abstract void logException(TException e);
    
}
