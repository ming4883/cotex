/*
 * ParagraphRenderer.java
 *
 * Created on 2007年3月29日, 上午 3:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package cotex;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author cyrux
 */
public class ParagraphRenderer extends JTextArea implements TableCellRenderer {
    
    /**
     * Creates a new instance of ParagraphRenderer
     */
    public ParagraphRenderer() {
        super();
        this.setWrapStyleWord(true);
        //this.setAutoscrolls(true);
        this.setLineWrap(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,int column) {
        if (value == null) {
            this.setForeground(null);
            this.setText("");
            this.setBorder(null);
            return this;
        }
        if ((row%2)==0) {
            this.setText("");
            this.setForeground(null);
            this.setBorder(null);
            return this;
        } else {
            this.setText(((Paragraph)value).getContent());
            table.setRowHeight(row,(int)this.getMinimumSize().getHeight());
            this.setForeground(Color.blue);
        }
        if(((AbstractParagraph)value).getTryLock()) {
            this.setBorder(new ParagraphtBorder("tryLock"));
        }else if(((AbstractParagraph)value).getLock()) {
            this.setBorder(new ParagraphtBorder("lock"));
        }else{
            this.setBorder(null);
        }
        return this;
    }
    
}
