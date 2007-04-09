/*
 * TNodeCommand.java
 *
 * Created on April 3, 2007, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public abstract class TNodeCommand {
    
    private java.util.HashMap<String, Object> mArgs;
    
    /** Creates a new instance of TNodeCommand */
    public TNodeCommand() {
        mArgs = new java.util.HashMap<String, Object>();
    }
    
    public void setArg(String key, Object value) {
        
        mArgs.put(key, value);
    }
    
    public Object getArg(String key) throws TException {
        
        if( !mArgs.containsKey(key) ) {
            throw new TException("TNodeCommand.getArg()", "arg: " + key + " does not exist");
        }
        
        return mArgs.get(key);
    }
    
    public boolean hasArg(String key) {
        
        return mArgs.containsKey(key);
        
    }
}
