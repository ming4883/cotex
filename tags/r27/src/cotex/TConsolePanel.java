/*
 * TConsolePanel.java
 *
 * Created on April 4, 2007, 12:00 PM
 */

package cotex;

import javax.swing.SwingUtilities;

/**
 *
 * @author  Ming
 */
public class TConsolePanel extends javax.swing.JPanel implements ILogger {
    
    /** Creates new form TConsolePanel */
    public TConsolePanel() {
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
        mTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        setName("Console");
        mTextArea.setColumns(20);
        mTextArea.setEditable(false);
        mTextArea.setRows(5);
        jScrollPane1.setViewportView(mTextArea);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea mTextArea;
    // End of variables declaration//GEN-END:variables
    
    public void logMessage(String str) {
        appendText(str);
    }
    
    public void logError(String str) {        
        appendText(str);
    }
    
    public void logException(TException e) {       
        appendText( e.toString() );
    }
    
    private void appendText(String str) {
        
        final String constStr = str;
        
        Runnable doRun = new Runnable() {
            
            public void run() {
                 mTextArea.append( constStr + "\n" );
                 mTextArea.setCaretPosition( mTextArea.getText().length() );
            }
        };
        
        SwingUtilities.invokeLater(doRun);
    }
}
