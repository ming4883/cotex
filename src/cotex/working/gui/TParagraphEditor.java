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

import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
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
    
    //----------------------------------
    class TActionPanel {
        
        //------------------------------
        private JPanel mActionPanel = null;
        private JPanel mContentActionPanel = null;
        private JPanel mGapActionPanel = null;
        private Popup mPopup = null;
        
        private java.awt.event.ActionListener mActionListener;
        
        //------------------------------
        private void createActionPanel() {
            
            mActionPanel = new JPanel();
            mActionPanel.setLayout( new java.awt.BorderLayout() );
            
            mActionListener = new java.awt.event.ActionListener( ) {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    onAction(evt);
                }
            };
            
            createContentActionPanel();
            createGapActionPanel();
            
        }
        
        //------------------------------
        private void createContentActionPanel() {
            
            mContentActionPanel = new JPanel();
            
            java.awt.GridLayout layout = new java.awt.GridLayout();
            
            layout.setRows(1);
            layout.setColumns(3);
            
            mContentActionPanel.setLayout( layout );
            
            JButton btn;
            
            // commit
            btn = new JButton();
            btn.setText("Commit");
            btn.setActionCommand("Commit");
            btn.addActionListener( mActionListener );
            
            mContentActionPanel.add(btn);
            
            // delete
            btn = new JButton();
            btn.setText("Delete");
            btn.setActionCommand("Delete");
            btn.addActionListener( mActionListener );
            
            mContentActionPanel.add(btn);
            
            // cancel
            btn = new JButton();
            btn.setText("Cancel");
            btn.setActionCommand("Cancel");
            btn.addActionListener( mActionListener );
            
            mContentActionPanel.add(btn);
            
            mContentActionPanel.setSize( mContentActionPanel.getMinimumSize() );
            
        }
        
        //------------------------------
        private void createGapActionPanel() {
            
            mGapActionPanel = new JPanel();
            
            java.awt.GridLayout layout = new java.awt.GridLayout();
            
            layout.setRows(1);
            layout.setColumns(2);
            
            mGapActionPanel.setLayout( layout );
            
            JButton btn;
            
            // commit
            btn = new JButton();
            btn.setText("Insert");
            btn.setActionCommand("Insert");
            btn.addActionListener( mActionListener );
            
            mGapActionPanel.add(btn);
            
            // cancel
            btn = new JButton();
            btn.setText("Cancel");
            btn.setActionCommand("Cancel");
            btn.addActionListener( mActionListener );
            
            mGapActionPanel.add(btn);
            
            mGapActionPanel.setSize( mGapActionPanel.getMinimumSize() );
            
        }
        
        //------------------------------
        private void onAction(java.awt.event.ActionEvent evt) {
            
            TLogManager.logMessage("TDocumentPanel: onAction");
            
            if(evt.getActionCommand().equals( "Commit" ) ) {
                
                ( (TContent)mEditingParagraph ).setPendingContent( getText() );
                mNode.execute( new TWorkingNodeModel.TCommitParagraphCmd(mEditingParagraph) );
                
            }
            
            if(evt.getActionCommand().equals( "Delete" ) ) {
                
            }
            
            if(evt.getActionCommand().equals( "Insert" ) ) {
                try{
                    TWorkingNodeModel tempModel = (TWorkingNodeModel)mNode.getModel();
                    TContent mContent = tempModel.getData().paragraphs.createContent();
                    mContent.setContent("New Paragraph");
                    TGap mGap = tempModel.getData().paragraphs.createGap();
                    mNode.execute( new TWorkingNodeModel.TInsertParagraphCmd(mEditingParagraph, mContent, mGap ));
                } catch(TException e){
                    TLogManager.logException(e);
                }
            }
            
            if(evt.getActionCommand().equals( "Cancel" ) ) {
                
                mNode.execute( new TWorkingNodeModel.TCancelParagraphCmd(mEditingParagraph) );
            }
            
            mPopup.hide();
            
        }
        
        //------------------------------
        private void popup() {
            
            if(null != mPopup)
                mPopup.hide();
            
            if(null == mActionPanel)
                createActionPanel();
            
            mActionPanel.removeAll();
            
            JPanel subPanel;
            
            if(mEditingParagraph.getClass().equals( TContent.class ) )
                subPanel = mContentActionPanel;
            else
                subPanel = mGapActionPanel;
            
            mActionPanel.add(subPanel);
            mActionPanel.setSize( subPanel.getSize().width, 24 );
            
            java.awt.Rectangle rect = mEditingTable.getCellRect(mEditingRow, 0, true);
            
            java.awt.Point p = new java.awt.Point(rect.x, rect.y);
            SwingUtilities.convertPointToScreen( p, mEditingTable );
            
            p.x += rect.width - mActionPanel.getSize().width;
            p.y -= mActionPanel.getSize().height;
            
            mPopup = PopupFactory.getSharedInstance().getPopup( mEditingTable.getRootPane(), mActionPanel, p.x, p.y );
            
            mPopup.show();
            
        }
    }
    
    //----------------------------------
    private ArrayList<CellEditorListener> mListeners;
    private TActionPanel mActionPanel;
    
    private TNode mNode = null;
    
    private TParagraph mEditingParagraph;
    private JTable mEditingTable;
    private int mEditingRow;
    
    //private TParagraphEditor getThis() {return this;}
    
    //----------------------------------
    public TParagraphEditor(TNode node) {
        
        mNode               = node;
        mActionPanel        = new TActionPanel();
        mListeners          = new ArrayList<CellEditorListener>();
        mEditingParagraph   = null;
        
        this.setLineWrap(true);
        
        this.addKeyListener(new java.awt.event.KeyListener() {
            
            public void keyPressed(java.awt.event.KeyEvent e) {
                
                updateEditingTableRowHeight();
            }
            
            public void keyReleased(java.awt.event.KeyEvent e) {}
            
            public void keyTyped(java.awt.event.KeyEvent e) {
                
                updateEditingTableRowHeight();
            }
        });
        
    }
    
    //----------------------------------
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        
        mEditingParagraph = (TParagraph)value;
        mEditingTable = table;
        mEditingRow = row;
        
        if(value == null) {
            return null;
        }
        
        // ensure locked before editing
        if(mEditingParagraph.getState() != TParagraph.State.LOCKED) {
            return null;
        }
        
        // a gap cannot be edited
        if( value.getClass().equals(TGap.class) ) {
            
            this.setText("");
            this.setEditable(false);
        } else {
            
            this.setText( ((TContent)mEditingParagraph).getContent() );
            this.setEditable(true);
        }
        
        this.setBorder( new TParagraphBorder( mEditingParagraph ) );
        
        Point p = table.getLocationOnScreen();
        Rectangle r = table.getCellRect(row, column, true);
        
        updateEditingTableRowHeight();
        
        // show the action panel
        mActionPanel.popup();
        
        return this;
        
    }
    
    //----------------------------------
    public void addCellEditorListener(CellEditorListener listener) {
        mListeners.add(listener);
    }
    
    //----------------------------------
    public void removeCellEditorListener(CellEditorListener listener) {
        mListeners.remove(listener);
    }
    
    //----------------------------------
    public Object getCellEditorValue() {
        return mEditingParagraph;
    }
    
    //----------------------------------
    public boolean isCellEditable(EventObject evt) {
        return true;
    }
    
    //----------------------------------
    public boolean shouldSelectCell(EventObject evt) {
        return true;
    }
    
    //----------------------------------
    public boolean stopCellEditing() {
        
        //TLogManager.logMessage("stopCellEditing");
        //fireStopCellEditingEvent();
        
        //mNode.execute( new TWorkingNodeModel.TCommitParagraphCmd(mEditingParagraph) );
        //mActionPanel.setVisible(false);
        
        return true;
    }
    
    //----------------------------------
    public void cancelCellEditing() {
        //fireCancelCellEditingEvent();
        
        //mActionPanel.setVisible(false);
    }
    
    //----------------------------------
    private void fireStopCellEditingEvent() {
        
        ChangeEvent evt = new ChangeEvent(this);
        
        java.util.Iterator<CellEditorListener> iter = mListeners.iterator();
        
        while( iter.hasNext() ) {
            CellEditorListener listener = iter.next();
            listener.editingStopped(evt);
        }
        
    }
    
    //----------------------------------
    private void fireCancelCellEditingEvent() {
        
        ChangeEvent evt = new ChangeEvent(this);
        
        java.util.Iterator<CellEditorListener> iter = mListeners.iterator();
        
        while( iter.hasNext() ) {
            CellEditorListener listener = iter.next();
            listener.editingCanceled(evt);
        }
        
    }
    
    //----------------------------------
    private void updateEditingTableRowHeight() {
        
        int h = (int)getMinimumSize().getHeight();
        
        javax.swing.border.Border border = getBorder();
        
        if(null != border) {
            
            java.awt.Insets insets = border.getBorderInsets(null);
            h += insets.bottom - insets.top;
        }
        
        mEditingTable.setRowHeight(mEditingRow, h);
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
