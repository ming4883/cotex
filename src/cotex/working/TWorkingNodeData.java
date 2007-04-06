/*
 * TWorkingNodeData.java
 *
 * Created on April 6, 2007, 6:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;

import cotex.working.TParagraphBase;
import cotex.working.TGap;
import cotex.working.TParagraph;
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
        this.para.add(new TGap(nextId));
        nextId++;
        
        addParagraph(1,new TParagraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
        addParagraph(3,new TParagraph("asdasdasdhajsdgsjafd jhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfjsdgsjafdjhfkjdshfdsfhgjksdhfhdskjfhdskjfhsdhkfhsdhfkjdshfkjshdkjfhkjdshfkjhsdkjfhkjsdhfkjdshkfhdskfhsdhfkjdshkjsa"));
    }
    
    int nextId = 0;
    
    Vector<TParagraphBase> para = new Vector<TParagraphBase>();
    
    public void addParagraph(int newIndex, TParagraphBase paragraph){
        paragraph.setId(nextId);
        para.add(newIndex,paragraph);
        nextId++;
        para.add(newIndex+1,new TGap(nextId));
        nextId++;
        fireTableDataChanged();
    }
    
    public Class getColumnClass(int c) {         return TParagraphBase.class;  }
    
    public int getColumnCount() { return 1; }
    
    
    public int getRowCount() { return this.para.size(); }
    
    public TParagraphBase getValueAt(int r, int c) { return this.para.get(r); }
    
    public boolean isCellEditable(int r, int c) {
        return !this.para.get(r).getLock();
    }
    
    public void setValueAt(Object value, int r, int c) {
        para.setElementAt((TParagraph) value,r);
        fireTableDataChanged();
    }
    
    public void updateParagraph(int paragraphId, String newContent){
        TParagraph currentParagrphc;
        for (int i = 1; i<para.size(); i+=2){
            currentParagrphc = (TParagraph) para.get(i);
            if(currentParagrphc.getId()==paragraphId) {
                currentParagrphc.setContent(newContent);
                fireTableDataChanged();
                return;
            }
        }
    }
    
}
