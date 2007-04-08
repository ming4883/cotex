/*
 * TWorkingNodeView.java
 *
 * Created on April 4, 2007, 10:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;
import cotex.working.gui.TDocumentPanel;
import cotex.working.gui.TSessionPanel;
import javax.swing.JComponent;
import java.util.HashMap;

/**
 *
 * @author Ming
 */
public class TWorkingNodeView implements INodeView {
    
    /** Commands **/
    
    public static class TNotfiyLockResultCmd extends TNodeCommand {
    }
    
    public static class TNotfiyCommitResultCmd extends TNodeCommand {
    }
    
    /** dispatching interface **/
    private interface ICmdInvoke {
        
        public abstract void invoke( TNodeCommand cmd );
        
    }
    
    /** private variables **/
    HashMap<Class, ICmdInvoke> mCmdDispatcher;
    
    java.util.ArrayList<javax.swing.JComponent> mGuiList;
    
    TNode mNode;
    TSessionPanel mSessionPanel;
    TDocumentPanel mDocumentPanel;
    
    
    /** Creates a new instance of TWorkingNodeView */
    public TWorkingNodeView(TNode node) {
        
        mNode = node;
        
        mGuiList = new java.util.ArrayList<javax.swing.JComponent>();
        
        mGuiList.add( mSessionPanel = new TSessionPanel(node) );
        mGuiList.add( mDocumentPanel = new TDocumentPanel(node) );
        
        initDispatcher();
    }
    
    public void startUp() throws TException {
    }
    
    public void shutDown() {
    }
    
    /*
    public TNode getNode() {
        return mNode;
    }
    
    public void setNode(TNode node) {
        mNode = node;
        mSessionPanel.setNode(node);
        mDocumentPanel.setNode(node);
    }
    */
    
    public java.util.List getGuiComponents() {
        return mGuiList;
    }
    
    public void execute(TNodeCommand cmd) {
        if( mCmdDispatcher.containsKey( cmd.getClass() ) )
            mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
    }
    
    private void initDispatcher() {
        
        mCmdDispatcher = new HashMap<Class, ICmdInvoke>();
        
        // lock result
        mCmdDispatcher.put(
                TNotfiyLockResultCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeNotfiyLockResult(cmd);}
        } );
        
        // commit result
        mCmdDispatcher.put(
                TNotfiyCommitResultCmd.class,
                new ICmdInvoke() {
            public void invoke(TNodeCommand cmd) {executeNotfiyCommitResult(cmd);}
        } );
    }
    
    private void executeNotfiyLockResult(TNodeCommand cmd) {
        
        mDocumentPanel.notifyLockResult( true );
    }
    
    private void executeNotfiyCommitResult(TNodeCommand cmd) {
        
        mDocumentPanel.notifyCommitResult( true );
    }
}
