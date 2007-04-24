/*
 * TRegistryNodeView.java
 *
 * Created on April 23, 2007, 10:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.registry;

import cotex.*;

/**
 *
 * @author Ming
 */
public class TRegistryNodeView implements INodeView {
    
    //----------------------------------
    private TSessionPanel mSessionPanel = null;
    private java.util.ArrayList<javax.swing.JComponent> mGuiList = null;
        
    //----------------------------------
    public TRegistryNodeView(TNode node) {
        
        mSessionPanel = new TSessionPanel(node);
        
        mGuiList = new java.util.ArrayList<javax.swing.JComponent>();
        
        mGuiList.add( mSessionPanel );
    }
    
    //----------------------------------
    public void startUp() throws TException {
    
    }
    
    //----------------------------------
    public void shutDown() {
    
    }
    
    //----------------------------------
    public java.util.List getGuiComponents() {
    
        return mGuiList;
    }
    
    //----------------------------------
    public void execute(TNodeCommand cmd) {
    
    }
    
    //----------------------------------
}
