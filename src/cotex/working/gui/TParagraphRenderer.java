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
            
        }
        else {
            
            // set text
            if ( value.getClass().equals( TGap.class ) ) {
                // gap
                this.setText("");
                this.setForeground(null);
                this.setBorder(null);
                table.setRowHeight(row, (int)this.getMinimumSize().getHeight() );
            }
            else {

                TContent paragraph = (TContent)value;

                // paragraph
                this.setText( paragraph.getContent() );

                
                this.setForeground(Color.DARK_GRAY);
                //this.setBorder(null);
                //this.setBorder( new TParagraphBorder( paragraph ) );
                
                //TitledBorder border = javax.swing.BorderFactory.createTitledBorder("Locked");
                //border.setTitlePosition(20);
                
                //this.setBorder( border );
                
                table.setRowHeight(row, (int)this.getMinimumSize().getHeight() + 6 );

            }
        }
       
        return this;
    }
    
}
