/*
 * TNodeFactory.java
 *
 * Created on April 3, 2007, 2:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public class TNodeFactory {
    
    /** Creates a new instance of TNodeFactory */
    private TNodeFactory() {
    }
    
    static public TNode createInstance(String nodeType) {
        
        if(nodeType == "working") {
            return new TNode(
                new cotex.working.TWorkingNodeView(),
                new cotex.working.TWorkingNodeModel() );
        }
        
        return null;
    }
}
