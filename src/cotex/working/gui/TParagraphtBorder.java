/*
 * TParagraphtBorder.java
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
public class TParagraphtBorder extends AbstractBorder {
    
    String mType = "";
    
    /**
     * Creates a new instance of TParagraphtBorder
     */
    public TParagraphtBorder() {
        this.mType = "tryLock";
    }
    
    public TParagraphtBorder(String type) {
        this.mType = type;
    }
    
    public void paintBorder(
        Component c,
        Graphics g,
        int x,
        int y,
        int width,
        int height) {
        
        Insets insets = getBorderInsets(c);
        
        if(mType=="lock") {
            g.setColor(Color.BLUE);
        }
        else {
            g.setColor(Color.BLACK);
        }
        
        //  Draw rectangles around the component, but do not draw
        //  in the component area itself.
        g.fillRect(x, y, width, insets.top);
        g.fillRect(x, y, insets.left, height);
        g.fillRect(x+width-insets.right, y, insets.right, height);
        g.fillRect(x, y+height-insets.bottom, width, insets.bottom);
        
        if(mType=="tryLock") {
            g.setColor(Color.WHITE);
            g.drawString("Acquiring lock ...", insets.left, y + height - 2);
        }
    }
    
    public boolean isBorderOpaque( ) { return true;}
    
    public Insets getBorderInsets(Component c) {
        
        if(mType=="tryLock") {
            return new Insets(3, 3, 15, 3);
        }
        
        return new Insets(3, 3, 3, 3);
    }
    
    
    
}
