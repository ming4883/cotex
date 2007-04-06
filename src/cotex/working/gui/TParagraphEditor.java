/*
 * TParagraphEditor.java
 *
 * Created on 2007年3月29日, 上午 4:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working.gui;

import cotex.*;
import cotex.working.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
/**
 *
 * @author cyrux
 */
public class TParagraphEditor  extends JTextArea implements TableCellEditor {
    
    public OkCancel helper = new OkCancel( );
    protected transient Vector listeners;
    protected transient String originalValue;
    protected transient boolean editing;
    protected transient int id;
    
    TNode mNode = null;
    
    public TParagraphEditor(){
        super();
        listeners = new Vector( );
        this.setWrapStyleWord(true);
        //this.setAutoscrolls(true);
        this.setLineWrap(true);
        
    }
    
    public void setNode(TNode node) {
        mNode = node;
    }
    
    public class OkCancel extends JWindow {
        
        private JButton okB = new JButton(new ImageIcon("res/accept.gif","Accept"));
        private JButton cancelB = new JButton(new ImageIcon("res/cancel.gif","Cancel"));
        private int w = 50;
        private int h = 24;
        
        public OkCancel( ) {
            setSize(w,h);
            setBackground(Color.yellow);
            JPanel p = new JPanel(new GridLayout(0,2));
            p.add(okB);
            p.add(cancelB);
            setContentPane(p);
            
            okB.addActionListener(new ActionListener( ) {
                public void actionPerformed(ActionEvent ae) {
                    stopCellEditing( );
                }
            });
            
            cancelB.addActionListener(new ActionListener( ) {
                public void actionPerformed(ActionEvent ae) {
                    cancelCellEditing( );
                }
            });
        }
    }
    
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row, int column) {
        
        final int currentRow = row;
        final JTable tb = table;
        final JTextArea ja = this;
        
        if (value == null) {
            return null;
        }
        
        if ( value.getClass().equals( TGap.class) ) {
            return null;
        }
        else {
            
            TParagraph currentParagraph = (TParagraph)value;
            
            if(currentParagraph.getLock() && currentParagraph.getTryLock()) { // is already try to lock
                
                this.setText(currentParagraph.getContent());
                this.id=currentParagraph.getId();
                this.setBorder(new TParagraphtBorder("edit"));
                this.addKeyListener(new java.awt.event.KeyListener() {
                    
                    public void keyPressed(java.awt.event.KeyEvent e) {
                        tb.setRowHeight(currentRow,(int)ja.getMinimumSize().getHeight());
                    }
                    
                    public void keyReleased(java.awt.event.KeyEvent e) {}
                    
                    public void keyTyped(java.awt.event.KeyEvent e) {
                        tb.setRowHeight(currentRow,(int)ja.getMinimumSize().getHeight());
                    }
                });
                
            }
            else {// try to lock
                
                currentParagraph.setTryLock(true);
                //table.getParent();
                return null;
            }
        }
        
        originalValue = this.getText();
        editing = true;
        Point p = table.getLocationOnScreen();
        Rectangle r = table.getCellRect(row, column, true);
        helper.setLocation(r.x + p.x + getWidth( ) - 50, r.y + p.y );
        helper.setVisible(true);
        
        return this;
    }
    
// CellEditor methods
    public void cancelCellEditing( ) {
        fireEditingCanceled( );
        editing = false;
        helper.setVisible(false);
    }
    
    public Object getCellEditorValue( ) {return new TParagraph(this.id,this.getText());}
    
    public boolean isCellEditable(EventObject eo) {
        return true;
    }
    
    public boolean shouldSelectCell(EventObject eo) {
        return true;
    }
    
    public boolean stopCellEditing( ) {
        
        fireEditingStopped( );
        editing = false;
        helper.setVisible(false);
        return true;
    }
    
    public void addCellEditorListener(CellEditorListener cel) {
        listeners.addElement(cel);
    }
    
    public void removeCellEditorListener(CellEditorListener cel) {
        listeners.removeElement(cel);
    }
    
    protected void fireEditingCanceled( ) {
        this.setText(originalValue);
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size( ) - 1; i >= 0; i--) {
            ((CellEditorListener)listeners.elementAt(i)).editingCanceled(ce);
        }
        editing = false;
        helper.setVisible(false);
    }
    
    protected void fireEditingStopped( ) {
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size( ) - 1; i >= 0; i--) {
            ((CellEditorListener)listeners.elementAt(i)).editingStopped(ce);
        }
        editing = false;
        helper.setVisible(false);
    }
    
}
