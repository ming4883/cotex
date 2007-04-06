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
    public TSessionPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        mSessionTree = new javax.swing.JTree();

        setLayout(new java.awt.BorderLayout());

        setName("Sessions");
        mSessionTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mSessionTreeMouseReleased(evt);
            }
        });

        jScrollPane1.setViewportView(mSessionTree);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void mSessionTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mSessionTreeMouseReleased

        if( evt.isPopupTrigger() ) {
        
            JMenuItem menuItem;
            JPopupMenu popup = new JPopupMenu();
            
            menuItem = new JMenuItem("New");
            //menuItem.addActionListener(this);
            popup.add(menuItem);
            
            menuItem = new JMenuItem("Join");
            //menuItem.addActionListener(this);
            popup.add(menuItem);
            
            popup.show( evt.getComponent(), evt.getX(), evt.getY() );
        
        }
    }//GEN-LAST:event_mSessionTreeMouseReleased
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree mSessionTree;
    // End of variables declaration//GEN-END:variables
 
    private TNode mNode = null;
     
    public void setNode(TNode node) {
        mNode = node;
    }
}