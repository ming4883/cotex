/*
 * TParagraph.java
 *
 * Created on 2007年3月29日, 上午 2:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.*;

/**
 *
 * @author cyrux
 */
public abstract class TParagraph implements java.io.Serializable {
    
    public enum State {
        UNLOCKED,
        LOCKING,
        LOCKED,
    };
    
    private TUniqueId mId;
    private TNodeInfo mLockOwner;
    private State mState;
    
    TParagraph(){
        mId         = new TUniqueId();
        mState      = State.UNLOCKED;
        mLockOwner  = null;
    }
    
    public void notifyLocking() throws TException  {
        
        if(mState != State.UNLOCKED) {
            throw new TException(
                "TParagraph.notifyLocking",
                "Inconsistence paragraph state");
        }
        
        mState = State.LOCKING;
    }
    
    public void notifyLocked() throws TException  {
        
        if(mState != State.LOCKING) {
            throw new TException(
                "TParagraph.notifyLocked",
                "Inconsistence paragraph state");
        }
        
        mState = State.LOCKED;
    }
    
    public void notifyUnlocked() throws TException  {
        
        if(mState != State.LOCKED) {
            throw new TException(
                "TParagraph.notifyUnlocked",
                "Inconsistence paragraph state");
        }
        
        mState = State.UNLOCKED;
    }
    
    public void notifyCancelLock() throws TException  {
        
        if(mState != State.LOCKING) {
            throw new TException(
                "TParagraph.notifyCancelLock",
                "Inconsistence paragraph state");
        }
        
        mState = State.UNLOCKED;
    }
    
    public State getState() {
        return mState;
    }
    
    public TUniqueId getId(){
        return mId;
    }
    
    public void setLockOwner(TNodeInfo owner) {
        mLockOwner = owner;
    }
    
    public TNodeInfo getLockOwner() {
        return mLockOwner;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mId);
        out.writeObject(mLockOwner);
        out.writeObject(mState);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mId = (TUniqueId)in.readObject();
        mLockOwner = (TNodeInfo)in.readObject();
        mState = (State)in.readObject();
    }
}
