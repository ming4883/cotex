/*
 * INodeModel.java
 *
 * Created on April 3, 2007, 2:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public interface INodeModel {
    
    public abstract TNode getNode();
    
    public abstract void setNode(TNode node);
    
    public abstract void execute(TNodeCommand cmd);
    
}
