/*
 * TParagraphBorder.java
 *
 * Created on 2007�~3��29��, �U�� 7:38
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
public class TParagraphBorder extends AbstractBorder {
    
    private TParagraph mParagraph;
    
    /**
     * Creates a new instance of TParagraphBorder
     */
    public TParagraphBorder(TParagraph paragraph) {
        
        mParagraph = paragraph;
    }
    
    public void paintBorder(
        Component c,
        Graphics g,
        int x,
        int y,
        int width,
        int height) {
        
        Color currColor = g.getColor();
        
        Insets insets = getBorderInsets(c);
        
        /*
        if( mParagraph.getState() == TParagraph.State.LOCKED ) {
            
            g.setColor( Color.BLACK );
            
            //  Draw rectangles around the component, but do not draw
            //  in the component area itself.
            g.fillRect(x, y, width, insets.top);
            g.fillRect(x, y, insets.left, height);
            g.fillRect(x + width-insets.right, y, insets.right, height);
            g.fillRect(x, y + height-insets.bottom, width, insets.bottom);
            
        }
        */
        
        // restore current color
        g.setColor(currColor);
        
    }
    
    public boolean isBorderOpaque( ) {
        return true;
    }
    
    /*
    public Insets getBorderInsets(Component c) {
        
        //return new Insets(3, 3, 15, 3);
        return new Insets(3, 3, 3, 3);
    }
    */
    
    
}
