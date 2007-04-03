/*
 * TNode.java
 *
 * Created on April 3, 2007, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public class TNode {
    
    private INodeView mView;
    private INodeModel mModel;
    
    /** Creates a new instance of TNode */
    public TNode(INodeView view, INodeModel model) {
        mView = view;
        mModel = model;
    }
    
    public INodeView getView() {
        return mView;
    }
    
    public INodeModel getModel() {
        return mModel;
    }
    
}
