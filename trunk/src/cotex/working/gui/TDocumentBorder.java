/*
 * TDocumentBorder.java
 *
 * Created on 2007年3月29日, 下午 7:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.gui;

import cotex.*;
import cotex.working.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author cyrux
 */
public class TDocumentBorder extends AbstractBorder {
    
    
    /**
     * Creates a new instance of TDocumentBorder
     */
    public TDocumentBorder() {
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y,
            int width, int height) {
        Insets insets = getBorderInsets(c);
        g.setColor(Color.black);
        
        //  Draw rectangles around the component, but do not draw
        //  in the component area itself.
        g.fillRect(x, y, width, insets.top);
        g.fillRect(x, y, insets.left, height);
        g.fillRect(x+width-insets.right, y, insets.right, height);
        g.fillRect(x, y+height-insets.bottom, width, insets.bottom);
    }
    public boolean isBorderOpaque( ) { return true;}
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }
    
    
    
}
