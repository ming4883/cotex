/*
 * TParagraphRenderer.java
 *
 * Created on 2007年3月29日, 上午 3:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package cotex.working.gui;
import cotex.*;
import cotex.working.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

/**
 *
 * @author cyrux
 */
public class TParagraphRenderer extends JTextArea implements TableCellRenderer {
    
    /**
     * Creates a new instance of TParagraphRenderer
     */
    public TParagraphRenderer() {
        super();
        this.setWrapStyleWord(true);
        
        //this.setAutoscrolls(true);
        this.setLineWrap(true);
    }
    
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,int column) {
        
        // some problem
        if (value == null) {
            this.setForeground(null);
            this.setText("Error! null value");
            this.setBorder(null);
            
            return this;
        }
        
        int extend = 0;
        
        // set text
        if ( value.getClass().equals( TGap.class ) ) {
            
            // gap
            this.setText("");
            this.setForeground(null);
        }
        else {
            
            // paragraph
            TContent paragraph = (TContent)value;
            
            this.setText( paragraph.getContent() );
            this.setForeground(Color.DARK_GRAY);
            
            extend = 3;
        }
        
        javax.swing.border.Border border = new TParagraphBorder( (TParagraph)value );
            
        int h = (int)getMinimumSize().getHeight() + extend;

        if(null != border) {

            java.awt.Insets insets = border.getBorderInsets(null);
            h += insets.bottom - insets.top;
        }

        this.setBorder( border );
        table.setRowHeight(row, h );
        
        return this;
    }
    
}
