/*
 * ISignalReceiverListener.java
 *
 * Created on April 23, 2007, 7:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.signal;

/**
 *
 * @author Ming
 */
public interface ISignalReceiverListener {
    
    public abstract void onSignalLost(TSignalReceiver receiver);
}
