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
    private TConfig mConfig;
    private TConnectionManager mConnectionMgr;
    
    
    /** Creates a new instance of TNode */
    public TNode(INodeView view, INodeModel model, TConfig config) {
        mView = view;
        mModel = model;
        
        mView.setNode(this);
        mModel.setNode(this);
        
        mConfig = config;
        
        mConnectionMgr = new TConnectionManager( mConfig.getSetting("General", "ConnectionType") );
        
    }
    
    public INodeView getView() {
        return mView;
    }
    
    public INodeModel getModel() {
        return mModel;
    }
    
    public void execute(TNodeCommand cmd) {
        
        mView.execute(cmd);
        mModel.execute(cmd);
        
    }
    
}
