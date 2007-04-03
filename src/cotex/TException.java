/*
 * TException.java
 *
 * Created on April 3, 2007, 2:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public class TException extends Throwable {
    
    private String mWhere;
    private String mWhy;
    
    /** Creates a new instance of TException */
    public TException(String where, String why) {
        mWhere = where;
        mWhy = why;
    }
    
    public String where() {
        
        return mWhere;
    }
    
    public String why() {
        
        return mWhy;
    }
    
    public String toString() {
     
        return "Exception at " + mWhere + ":\n" +
                mWhy;
    }
}
