/*
 * TSessionPanel.java
 *
 * Created on April 4, 2007, 1:54 PM
 */

package cotex.working.gui;

import cotex.*;
import cotex.working.*;
import javax.swing.*;


/**
 *
 * @author  Ming
 */
public class TSessionPanel extends javax.swing.JPanel {
    
    /** Creates new form TSessionPanel */
    public TSessionPanel(TNode node) {
        mNode = node;
        initComponents();
        
        TWorkingNodeModel workingModel = (TWorkingNodeModel)mNode.getModel();
        
        mSessionList.setModel( workingModel.getData().sessions.listModel );
        mWorkerList.setModel( workingModel.getData().nodes.listModel );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        mSessionList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        mWorkerList = new javax.swing.JList();

        setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        setName("Sessions");
        mSessionList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        mSessionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        mSessionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mSessionListMouseReleased(evt);
            }
        });

        jScrollPane1.setViewportView(mSessionList);

        add(jScrollPane1);

        mWorkerList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        mWorkerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(mWorkerList);

        add(jScrollPane2);

    }// </editor-fold>//GEN-END:initComponents
        
    private void mSessionListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mSessionListMouseReleased
// TODO add your handling code here:
        if( evt.isPopupTrigger() ) {
            
            JMenuItem menuItem;
            JPopupMenu popup = new JPopupMenu();
            
            mSessionList.setSelectedIndex( mSessionList.locationToIndex( evt.getPoint() ) );
            java.awt.event.ActionListener listener = new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    onSessionMenuAction(e);
                }
            };
            
            menuItem = new JMenuItem("Refresh");
            menuItem.setActionCommand("Refresh");
            menuItem.addActionListener(listener);
            popup.add(menuItem);
            
            menuItem = new JMenuItem("New");
            menuItem.setActionCommand("New");
            menuItem.addActionListener(listener);
            popup.add(menuItem);
            if( mSessionList.getSelectedValue() != null ) {
                
                menuItem = new JMenuItem("Join");
                menuItem.setActionCommand("Join");
                menuItem.addActionListener(listener);
                popup.add(menuItem);
                
            }
            popup.show( evt.getComponent(), evt.getX(), evt.getY() );
        }
    }//GEN-LAST:event_mSessionListMouseReleased
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList mSessionList;
    private javax.swing.JList mWorkerList;
    // End of variables declaration//GEN-END:variables
    
    private TNode mNode = null;
    
    /*
    public void setNode(TNode node) {
        mNode = node;
    }
     */
    
    private void onSessionMenuAction(java.awt.event.ActionEvent evt) {
        
        if( evt.getActionCommand().equals("Join") ) {
            
            cotex.TSessionInfo session = (cotex.TSessionInfo)mSessionList.getSelectedValue();
            mNode.execute( new TWorkingNodeModel.TJoinSessionCmd( session.getId() ) );
            
        } else if( evt.getActionCommand().equals("New") ) {
            
            String newSessionName =
                    JOptionPane.showInputDialog("Please input the name of the new session:");
            
            mNode.execute( new TWorkingNodeModel.TNewSessionCmd(newSessionName) );
        }else if( evt.getActionCommand().equals("Refresh") ) {
            
            mNode.execute( new TWorkingNodeModel.TRefreshSessionListCmd() );
        }
    }
}