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
        
        // set text
        if ( value.getClass().equals( TGap.class ) ) {
            // gap
            this.setText("");
            this.setForeground(null);
            this.setBorder(null);
            return this;
        }
        else {
        
            // paragraph
            this.setText( ((TParagraph)value).getContent() );
            table.setRowHeight(row,(int)this.getMinimumSize().getHeight());
            this.setForeground(Color.blue);
        }
        
        // set border
        if( ((TParagraphBase)value).getTryLock() ) {
            this.setBorder(new TParagraphtBorder("tryLock"));
        
        }
        else if( ((TParagraphBase)value).getLock() ) {
            this.setBorder(new TParagraphtBorder("lock"));
        
        }
        else{
            this.setBorder(null);
        
        }
        
        return this;
    }
    
}
