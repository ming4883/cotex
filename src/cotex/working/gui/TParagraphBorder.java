/*
 * TParagraphBorder.java
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
        
        if( mParagraph.getClass().equals(TContent.class) )
            paintContentBorder(g, x, y, width, height);
        else
            paintGapBorder(g, x, y, width, height);
        
        // restore current color
        g.setColor(currColor);
        
    }
    
    public boolean isBorderOpaque( ) {
        return true;
    }
    
    public Insets getBorderInsets(Component c) {
        
        if( mParagraph.getClass().equals(TContent.class) )
            return getContentInsets();
        
        return getGapInsets();
    }
    
    private void paintGapBorder(
        Graphics g,
        int x,
        int y,
        int width,
        int height) {
        
        Insets insets = getGapInsets();
        
        if( mParagraph.getState() != TParagraph.State.UNLOCKED ) {
            g.setColor( new Color(220, 220, 220) );
            
            g.fillRect(x, y, width, height);
            
            g.setColor( Color.DARK_GRAY );
            
            if( mParagraph.getState() == TParagraph.State.LOCKING )
                g.drawString( "Locking", 2, 10);
            else
                g.drawString( "Locked", 2, 10);
            
        }
    }
    
    private void paintContentBorder(
        Graphics g,
        int x,
        int y,
        int width,
        int height) {
        
        Insets insets = getContentInsets();
        
        if( mParagraph.getState() != TParagraph.State.UNLOCKED ) {
            
            //g.setColor( new Color(160, 160, 160) );
            g.setColor( new Color(220, 220, 220) );
            
            g.fillRect(x, y, width, insets.top);
            g.fillRect(x, y, insets.left, height);
            g.fillRect(x + width-insets.right, y, insets.right, height);
            g.fillRect(x, y + height-insets.bottom, width, insets.bottom);
            
            g.setColor( Color.DARK_GRAY );
            
            if( mParagraph.getState() == TParagraph.State.LOCKING )
                g.drawString( "Locking", 2, height - 5);
            else
                g.drawString( "Locked", 2, height - 5);
        }
    }
    
    private Insets getGapInsets() {
        
        //if( mParagraph.getState() == TParagraph.State.UNLOCKED )
        //   return new Insets(0, 0, 0, 0);
        
        //return new Insets(2, 2, 10, 2);
        return new Insets(0, 0, 0, 0);
    }
    
    private Insets getContentInsets() {
        
        if( mParagraph.getState() == TParagraph.State.UNLOCKED )
            return new  Insets(2, 2, 2, 2);
        
        return new Insets(2, 2, 15, 2);
    }
    /**/
    
    
}
