/*
 * TDocumentPanel.java
 *
 * Created on April 4, 2007, 10:55 AM
 */

package cotex.working;

import cotex.*;
import javax.swing.table.TableModel;

/**
 *
 * @author  Ming
 */
public class TDocumentPanel extends javax.swing.JPanel {
    
    /**
     * Creates new form TDocumentPanel
     */
    public TDocumentPanel() {
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
        mTable = new javax.swing.JTable();

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
        mLockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLockBtnActionPerformed(evt);
            }
        });

        jPanel1.add(mLockBtn);

        mCommitBtn.setText("Commit");
        mCommitBtn.setEnabled(false);
        mCommitBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        mCommitBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        mCommitBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        mCommitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCommitBtnActionPerformed(evt);
            }
        });

        jPanel1.add(mCommitBtn);

        add(jPanel1, java.awt.BorderLayout.EAST);

        //mTable.setModel((TableModel)(mNode.getModel()));
        mTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        mTable.setAutoscrolls(true);
        mTable.setShowHorizontalLines(false);
        mTable.setShowVerticalLines(false);
        mTable.setTableHeader(null);
        mTable.setUpdateSelectionOnSort(false);
        mTable.setDefaultRenderer(AbstractParagraph.class, new ParagraphRenderer());
        mTable.setDefaultEditor(AbstractParagraph.class,  new ParagraphEditor());
        //docModel.addParagraph(1,new Paragraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
        //docModel.addParagraph(3,new Paragraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
 
        jScrollPane1.setViewportView(mTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void mCommitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCommitBtnActionPerformed
// TODO add your handling code here:
        if(mNode != null)
            mNode.execute( new TWorkingNodeModel.TCommitParagraphCmd() );
    }//GEN-LAST:event_mCommitBtnActionPerformed

    private void mLockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLockBtnActionPerformed
// TODO add your handling code here:
        if(mNode != null)
            mNode.execute( new TWorkingNodeModel.TLockParagraphCmd() );
    }//GEN-LAST:event_mLockBtnActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton mCommitBtn;
    private javax.swing.JButton mLockBtn;
    private javax.swing.JTable mTable;
    // End of variables declaration//GEN-END:variables
    
    
    private TNode mNode = null;
    
    public void setNode(TNode node) {
        mNode = node;
        
        TWorkingNodeModel nodeModel = (TWorkingNodeModel)mNode.getModel();
    
        mTable.setModel( nodeModel.getData() );
    }
    
    public void notifyLockResult(boolean result) {
        
        if(result) {
            //mTextArea.setEditable(true);
            mLockBtn.setEnabled(false);
            mCommitBtn.setEnabled(true);
        }
    }
    
    public void notifyCommitResult(boolean result) {
        
        if(result) {
            //mTextArea.setEditable(false);
            mLockBtn.setEnabled(true);
            mCommitBtn.setEnabled(false);
        }
    }
}
