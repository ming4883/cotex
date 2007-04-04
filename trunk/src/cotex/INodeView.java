/*
 * INodeView.java
 *
 * Created on April 3, 2007, 2:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author Ming
 */
public interface INodeView {
    
    public abstract TNode getNode();
    
    public abstract void setNode(TNode node);
    
    public abstract java.util.List getGuiComponents();
    
    public abstract void execute(TNodeCommand cmd);
    
}
