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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

/**
 *
 * @author cyrux
 */
public class TParagraphEditor extends JTextArea implements TableCellEditor {
    
     private class TActionPanel extends JWindow {
        
        //private JButton mOkBtn = new JButton(new ImageIcon("res/accept.gif","Accept"));
        //private JButton mCancelBtn = new JButton(new ImageIcon("res/cancel.gif","Cancel"));
        
        private JButton mOkBtn = new JButton();
        private JButton mCancelBtn = new JButton();
        
        private final int WIDTH = 160;
        private final int HEIGHT = 24;
        
        public TActionPanel() {
            
            mOkBtn.setText("Commit");
            mCancelBtn.setText("Canel");
            
            setSize(WIDTH, HEIGHT);
            setBackground(Color.yellow);
            
            JPanel panel = new JPanel(new GridLayout(0,2));
            panel.add(mOkBtn);
            panel.add(mCancelBtn);
            setContentPane(panel);
            
            mOkBtn.addActionListener(new ActionListener( ) {
                public void actionPerformed(ActionEvent ae) {
                    mNode.execute( new TWorkingNodeModel.TCommitParagraphCmd(mEditingParagraph) );
                    stopCellEditing();
                }
            });
            
            mCancelBtn.addActionListener(new ActionListener( ) {
                public void actionPerformed(ActionEvent ae) {
                    cancelCellEditing( );
                }
            });
            
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        }
    }
    
    private ArrayList<CellEditorListener> mListeners;
    private TActionPanel mActionPanel;
    private TContent mEditingParagraph;
    private TNode mNode = null;
    
    private JTable mEditingTable;
    private int mEditingRow;
    
    public TParagraphEditor(TNode node) {
        mNode               = node;
        mActionPanel        = new TActionPanel();
        mListeners          = new ArrayList<CellEditorListener>();
        mEditingParagraph   = null;
        
        this.setLineWrap(true);
        
        this.addKeyListener(new java.awt.event.KeyListener() {
            
            public void keyPressed(java.awt.event.KeyEvent e) {
                mEditingTable.setRowHeight(mEditingRow, (int)getMinimumSize().getHeight() );
            }
            
            public void keyReleased(java.awt.event.KeyEvent e) {}
            
            public void keyTyped(java.awt.event.KeyEvent e) {
                mEditingTable.setRowHeight(mEditingRow, (int)getMinimumSize().getHeight() );
            }
        });
        
        
        //this.setF
    }
    
    public Component getTableCellEditorComponent(
        JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column)
    {
        if(value == null) {
            return null;
        }
        
        //TLogManager.logMessage("getTabelCellEditor");
        
        // a gap, cannot be edited
        if( value.getClass().equals(TGap.class) ) {
            return null;
        }
        
        mEditingParagraph = (TContent)value;
        mEditingTable = table;
        mEditingRow = row;
        
        if(mEditingParagraph.getState() != TParagraph.State.LOCKED) {
            return null;
        }
        
        this.setText( mEditingParagraph.getContent() );
        
        Point p = table.getLocationOnScreen();
        Rectangle r = table.getCellRect(row, column, true);
        
        mActionPanel.setLocation(r.x + p.x + getWidth( ) - 50, r.y + p.y );
        mActionPanel.setVisible(true);
        
        return this;
        
    }
    
    public void addCellEditorListener(CellEditorListener listener) {
        mListeners.add(listener);
    }

    public void removeCellEditorListener(CellEditorListener listener) {
        mListeners.remove(listener);
    }
    
    public Object getCellEditorValue() {
        return mEditingParagraph;
    }
    
    public boolean isCellEditable(EventObject evt) {
        return true;
    }
    
    public boolean shouldSelectCell(EventObject evt) {
        return true;
    }
    
    public boolean stopCellEditing() {
        
        //TLogManager.logMessage("stopCellEditing");
        //fireStopCellEditingEvent();
        
        //mNode.execute( new TWorkingNodeModel.TCommitParagraphCmd(mEditingParagraph) );
        mActionPanel.setVisible(false);
        
        return true;
    }
    
    public void cancelCellEditing() {
        //fireCancelCellEditingEvent();
        
        mActionPanel.setVisible(false);
    }
    
    private void fireStopCellEditingEvent() {
        
        ChangeEvent evt = new ChangeEvent(this);
        
        java.util.Iterator<CellEditorListener> iter = mListeners.iterator();
        
        while( iter.hasNext() ) {
            CellEditorListener listener = iter.next();
            listener.editingStopped(evt);
        }
        
    }
    
    private void fireCancelCellEditingEvent() {
        
        ChangeEvent evt = new ChangeEvent(this);
        
        java.util.Iterator<CellEditorListener> iter = mListeners.iterator();
        
        while( iter.hasNext() ) {
            CellEditorListener listener = iter.next();
            listener.editingCanceled(evt);
        }
        
    }
    
   

    /*
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
        
        private JButton mOkBtn = new JButton(new ImageIcon("res/accept.gif","Accept"));
        private JButton mCancelBtn = new JButton(new ImageIcon("res/cancel.gif","Cancel"));
        private int w = 50;
        private int h = 24;
        
        public OkCancel( ) {
            setSize(w,h);
            setBackground(Color.yellow);
            
            JPanel panel = new JPanel(new GridLayout(0,2));
            panel.add(mOkBtn);
            panel.add(mCancelBtn);
            setContentPane(panel);
            
            mOkBtn.addActionListener(new ActionListener( ) {
                public void actionPerformed(ActionEvent ae) {
                    stopCellEditing( );
                }
            });
            
            mCancelBtn.addActionListener(new ActionListener( ) {
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
        } else {
            
            TContent currentParagraph = (TContent)value;
            
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
                
            } else {// try to lock
                
                currentParagraph.setTryLock(true);
                //mNode.execute(new TWorkingNodeModel.TLockParagraphCmd());
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
    
    public Object getCellEditorValue() {return new TContent(this.id,this.getText());}
    
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
    */
}
