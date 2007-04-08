/*
 * TParagraph.java
 *
 * Created on 2007�~3��29��, �W�� 2:03
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
    private State mState;
    
    TParagraph(){
        mId = new TUniqueId();
        mState = State.UNLOCKED;
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
    
    public State getState() {
        return mState;
    }
    
    public TUniqueId getId(){
        return mId;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        out.writeObject(mId);
        out.writeObject(mState);
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        mId = (TUniqueId)in.readObject();
        mState = (State)in.readObject();
    }
}
