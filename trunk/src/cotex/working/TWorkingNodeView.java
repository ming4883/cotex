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
import javax.swing.JComponent;

/**
 *
 * @author Ming
 */
public class TWorkingNodeView implements INodeView {
    
    java.util.ArrayList<javax.swing.JComponent> mGuiList;
    INodeModel mModel;
    
    /** Creates a new instance of TWorkingNodeView */
    public TWorkingNodeView(INodeModel model) {
        
        mModel = model;
        
        mGuiList = new java.util.ArrayList<javax.swing.JComponent>();
        
        mGuiList.add( new TSessionPanel(model) );
        mGuiList.add( new TDocumentPanel(model) );
        
    }
    
    public java.util.List getGuiComponents() {
        return mGuiList;
    }
}
