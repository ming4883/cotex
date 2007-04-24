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
    
    //----------------------------------
    private TNodeFactory() {
    }
    
    //----------------------------------
    static public TNode createWorkingInstance(TConfig config) throws TException {
        
        TNode node = new TNode(config);
        
        node.setModel( new cotex.working.TWorkingNodeModel(node) );
        node.setView( new cotex.working.TWorkingNodeView(node) );
        
        return node;
        
    }
    
    //----------------------------------
    static public TNode createRegistryInstance(TConfig config) throws TException {
        
        TNode node = new TNode(config);
        
        node.setModel( new cotex.registry.TRegistryNodeModel(node) );
        node.setView( new cotex.registry.TRegistryNodeView(node) );
        
        return node;   
    }
    
    //----------------------------------
}
