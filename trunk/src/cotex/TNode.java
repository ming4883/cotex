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
    public TNode(TConfig config) {
        
        mConfig = config;
        mConnectionMgr = new TConnectionManager( mConfig.getSetting("General", "ConnectionType") );
        
    }
    
    public void setModel(INodeModel model) {
        mModel = model;
    }
    
    public void setView(INodeView view) {
        mView = view;
    }    
    
    public INodeView getView() {
        return mView;
    }
    
    public INodeModel getModel() {
        return mModel;
    }
    
    public TConnectionManager getConnectionManager() {
        return mConnectionMgr;
    }
    
    public TConfig getConfig() {
        return mConfig;
    }
    
    public void execute(TNodeCommand cmd) {
        
        mView.execute(cmd);
        mModel.execute(cmd);
    }
    
}
