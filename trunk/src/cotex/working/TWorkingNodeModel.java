/*
 * TWorkingNodeModel.java
 *
 * Created on April 3, 2007, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;

/**
 *
 * @author Ming
 */
public class TWorkingNodeModel implements INodeModel {
    
    /** Creates a new instance of TWorkingNodeModel */
    public TWorkingNodeModel() {
    }
    
    public Object execute(TNodeCommand cmd) {
        
        if(cmd.getClass() == TExitCmd.class) {
            javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel.OnExitCmd");
        }
        
        return null;
    }
}
