/*
 * TSessionPanel.java
 *
 * Created on April 23, 2007, 11:15 AM
 */

package cotex.registry;

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
        jScrollPane1.setViewportView(mSessionTree);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree mSessionTree;
    // End of variables declaration//GEN-END:variables
    
}
