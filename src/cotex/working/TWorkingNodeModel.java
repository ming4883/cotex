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
import java.util.HashMap;


/**
 *
 * @author Ming
 */
public class TWorkingNodeModel implements INodeModel {
    
    /** Command classes for INodeView **/
    public static class TNewSessionCmd extends TNodeCommand {
    }
    
    public static class TJoinSessionCmd extends TNodeCommand {
    }
    
    public static class TLockParagraphCmd extends TNodeCommand {
    }
    
    public static class TCommitParagraphCmd extends TNodeCommand {
    }
    
    public static class TInsertParagraphCmd extends TNodeCommand {
    }
    
    public static class TEraseParagraphCmd extends TNodeCommand {
    }
    
    /** dispatching interface **/
    private interface ICmdInvoke {
        
        public abstract void invoke( TNodeCommand cmd );
        
    }
    
    /** private variables **/
    HashMap<Class, ICmdInvoke> mCmdDispatcher;
    INodeView mView = null;
    
    /** Creates a new instance of TWorkingNodeModel */
    public TWorkingNodeModel() {
        initDispatcher();
    }
    
    public void setView(INodeView view) {
        mView = view;
    }

    /** dispatcher for TNodeCommand **/
    public void execute(TNodeCommand cmd) {
        
        if( mCmdDispatcher.containsKey( cmd.getClass() ) )
            mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
    }
    
    private void sleepImpl(int time) {
        
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void initDispatcher() {
        
        mCmdDispatcher = new HashMap<Class, ICmdInvoke>();
        
        // exit
        mCmdDispatcher.put(
            TExitCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeExit(cmd);} 
            } );
            
        // new session
        mCmdDispatcher.put(
            TNewSessionCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeNewSession(cmd);} 
            } );
            
        // join session
        mCmdDispatcher.put(
            TJoinSessionCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeJoinSession(cmd);} 
            } );
            
        // lock paragraph
        mCmdDispatcher.put(
            TLockParagraphCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeLockParagraph(cmd);} 
            } );
            
        // commit paragraph
        mCmdDispatcher.put(
            TCommitParagraphCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeCommitParagraph(cmd);} 
            } );
            
        // insert paragraph
        mCmdDispatcher.put(
            TInsertParagraphCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeInsertParagraph(cmd);} 
            } );
        
        // erase paragraph
        mCmdDispatcher.put(
            TEraseParagraphCmd.class,
            new ICmdInvoke() {
                public void invoke(TNodeCommand cmd) {executeEraseParagraph(cmd);} 
            } );
    }
    
    /** Acutally implementation **/
    private void executeExit(TNodeCommand cmd) {
        
        javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel.executeExit Invoke");
    }
    
    private void executeNewSession(TNodeCommand cmd) {
        
    }
    
    private void executeJoinSession(TNodeCommand cmd) {
        
    }
    
    private void executeLockParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: locking paragraph ...");
        
        try {
            
            Thread t = new Thread() {
                public void run() {
                    sleepImpl(1000);
                    mView.execute( new TWorkingNodeView.TNotfiyLockResultCmd() );
                    TLogManager.logMessage("TWorkingNodeModel: lock paragraph done");
                }
            };
            
            t.start();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void executeCommitParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: committing paragraph");
        
        try {
            
            Thread t = new Thread() {
                public void run() {
                    sleepImpl(1000);
                    mView.execute( new TWorkingNodeView.TNotfiyCommitResultCmd() );
                    TLogManager.logMessage("TWorkingNodeModel: commit paragraph done");
                }
            };
            
            t.start();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void executeInsertParagraph(TNodeCommand cmd) {
        
    }
    
    private void executeEraseParagraph(TNodeCommand cmd) {
        
    }
}
