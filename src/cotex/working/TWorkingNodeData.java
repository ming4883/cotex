/*
 * TWorkingNodeData.java
 *
 * Created on April 6, 2007, 6:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.AbstractParagraph;
import cotex.Gap;
import cotex.Paragraph;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ming
 */
public class TWorkingNodeData extends AbstractTableModel {
    
    /**
     * Creates a new instance of TWorkingNodeData
     */
    public TWorkingNodeData() {
        this.para.add(new Gap(nextId));
        nextId++;
        
        addParagraph(1,new Paragraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
        addParagraph(3,new Paragraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
    }
    
    int nextId = 0;
    
    Vector<AbstractParagraph> para = new Vector<AbstractParagraph>();
    
    public void addParagraph(int newIndex, AbstractParagraph paragraph){
        paragraph.setId(nextId);
        para.add(newIndex,paragraph);
        nextId++;
        para.add(newIndex+1,new Gap(nextId));
        nextId++;
        fireTableDataChanged();
    }
    
    public Class getColumnClass(int c) {         return AbstractParagraph.class;  }
    
    public int getColumnCount() { return 1; }
    
    
    public int getRowCount() { return this.para.size(); }
    
    public AbstractParagraph getValueAt(int r, int c) { return this.para.get(r); }
    
    public boolean isCellEditable(int r, int c) {
        return !this.para.get(r).getLock();
    }
    
    public void setValueAt(Object value, int r, int c) {
        para.setElementAt((Paragraph) value,r);
        fireTableDataChanged();
    }
    
    public void updateParagraph(int paragraphId, String newContent){
        Paragraph currentParagrphc;
        for (int i = 1; i<para.size(); i+=2){
            currentParagrphc = (Paragraph) para.get(i);
            if(currentParagrphc.getId()==paragraphId) {
                currentParagrphc.setContent(newContent);
                fireTableDataChanged();
                return;
            }
        }
    }
    
}
