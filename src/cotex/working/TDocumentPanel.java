/*
 * TDocumentPanel.java
 *
 * Created on April 4, 2007, 10:55 AM
 */

package cotex.working;

import cotex.*;

/**
 *
 * @author  Ming
 */
public class TDocumentPanel extends javax.swing.JPanel {
    
    /**
     * Creates new form TDocumentPanel
     */
    public TDocumentPanel(INodeModel model) {
        mModel = model;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        mLockBtn = new javax.swing.JButton();
        mCommitBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        setName("Document");
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(80, 46));
        jPanel1.setMinimumSize(new java.awt.Dimension(80, 46));
        jPanel1.setPreferredSize(new java.awt.Dimension(80, 100));
        mLockBtn.setText("Lock");
        mLockBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        mLockBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        mLockBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        mLockBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mLockBtnMouseClicked(evt);
            }
        });

        jPanel1.add(mLockBtn);

        mCommitBtn.setText("Commit");
        mCommitBtn.setEnabled(false);
        mCommitBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        mCommitBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        mCommitBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        mCommitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mCommitBtnMouseClicked(evt);
            }
        });

        jPanel1.add(mCommitBtn);

        add(jPanel1, java.awt.BorderLayout.EAST);

        mTextArea.setColumns(20);
        mTextArea.setEditable(false);
        mTextArea.setRows(5);
        jScrollPane1.setViewportView(mTextArea);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void mCommitBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mCommitBtnMouseClicked
// TODO add your handling code here:
        
        mModel.execute( new TWorkingNodeModel.TCommitParagraphCmd() );
        
        mTextArea.setEditable(false);
        mLockBtn.setEnabled(true);
        mCommitBtn.setEnabled(false);
        
    }//GEN-LAST:event_mCommitBtnMouseClicked

    private void mLockBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mLockBtnMouseClicked
// TODO add your handling code here:
        
        mModel.execute( new TWorkingNodeModel.TLockParagraphCmd() );
        
        mTextArea.setEditable(true);
        mLockBtn.setEnabled(false);
        mCommitBtn.setEnabled(true);
        
    }//GEN-LAST:event_mLockBtnMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton mCommitBtn;
    private javax.swing.JButton mLockBtn;
    private javax.swing.JTextArea mTextArea;
    // End of variables declaration//GEN-END:variables
    
    
    private INodeModel mModel;
}
