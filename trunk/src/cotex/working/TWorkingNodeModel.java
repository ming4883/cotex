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
    
    private interface ICommandInvoke {
        
        public abstract Object invoke( TNodeCommand cmd );
        
    }
    
    /** private variables **/
    HashMap<Class, ICommandInvoke> mCmdDispatcher;
    
    /** Creates a new instance of TWorkingNodeModel */
    public TWorkingNodeModel() {
        
        mCmdDispatcher = new HashMap<Class, ICommandInvoke>();
        
        // exit
        mCmdDispatcher.put(
            TExitCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeExit(cmd);} 
            } );
            
        // new session
        mCmdDispatcher.put(
            TNewSessionCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeNewSession(cmd);} 
            } );
            
        // join session
        mCmdDispatcher.put(
            TJoinSessionCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeJoinSession(cmd);} 
            } );
            
        // lock paragraph
        mCmdDispatcher.put(
            TLockParagraphCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeLockParagraph(cmd);} 
            } );
            
        // commit paragraph
        mCmdDispatcher.put(
            TCommitParagraphCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeCommitParagraph(cmd);} 
            } );
            
        // insert paragraph
        mCmdDispatcher.put(
            TInsertParagraphCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeInsertParagraph(cmd);} 
            } );
        
        // erase paragraph
        mCmdDispatcher.put(
            TEraseParagraphCmd.class,
            new ICommandInvoke() {
                public Object invoke(TNodeCommand cmd) {return executeEraseParagraph(cmd);} 
            } );
    }
    
    /** dispatcher for TNodeCommand **/
    public Object execute(TNodeCommand cmd) {
        
        if( mCmdDispatcher.containsKey( cmd.getClass() ) )
            mCmdDispatcher.get( cmd.getClass() ).invoke(cmd);
        
        return null;
    }
    
    private void sleepImpl(int time) {
        
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    /** Acutally implementation **/
    private Object executeExit(TNodeCommand cmd) {
        
        javax.swing.JOptionPane.showMessageDialog(null, "TWorkingNodeModel.executeExit Invoke");
        return null;
    }
    
    private Object executeNewSession(TNodeCommand cmd) {
        
        return null;
    }
    
    private Object executeJoinSession(TNodeCommand cmd) {
        
        return null;
    }
    
    private Object executeLockParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: locking paragraph ...");
        
        try {
            
            Thread t = new Thread() {
                public void run() { sleepImpl(1000); }
            };
            
            t.start();
            
            t.join();
        } 
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        TLogManager.logMessage("TWorkingNodeModel: lock paragraph done");
        
        return null;
    }
    
    private Object executeCommitParagraph(TNodeCommand cmd) {
        
        TLogManager.logMessage("TWorkingNodeModel: committing paragraph");
        
        try {
            
            Thread t = new Thread() {
                public void run() { sleepImpl(1000); }
            };
            
            t.start();
            
            t.join();
        } 
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        TLogManager.logMessage("TWorkingNodeModel: commit paragraph done");
        
        return null;
    }
    
    private Object executeInsertParagraph(TNodeCommand cmd) {
        
        return null;
    }
    
    private Object executeEraseParagraph(TNodeCommand cmd) {
        
        return null;
    }
}
