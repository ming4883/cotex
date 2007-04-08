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
    
    static public TNode createInstance(String nodeType, TConfig config) throws TException {
        
        if( nodeType.equals("working") ) {
            
            TNode node = new TNode(config);
            
            node.setModel( new cotex.working.TWorkingNodeModel(node) );
            node.setView( new cotex.working.TWorkingNodeView(node) );
            
            return node;
        }
        
        throw new TException("TNodeFactory.createInstance", "unknown node type: " + nodeType); 
    }
}
