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
    
    static public TNode createInstance(String nodeType) throws TException {
        
        if( nodeType.equals("working") ) {
            
            cotex.working.TWorkingNodeModel model = new cotex.working.TWorkingNodeModel();
            cotex.working.TWorkingNodeView view = new cotex.working.TWorkingNodeView(model);
            
            model.setView(view);
            
            return new TNode(
                view,
                model );
        }
        
        throw new TException("TNodeFactory.createInstance", "unknown node type: " + nodeType); 
    }
}
