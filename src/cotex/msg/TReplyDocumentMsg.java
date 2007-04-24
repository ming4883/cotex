/*
 * TReplyDocumentMsg.java
 *
 * Created on April 9, 2007, 12:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.msg;

import cotex.working.TParagraph;
import java.util.ArrayList;

/**
 *
 * @author Ming
 */
public class TReplyDocumentMsg implements java.io.Serializable {
    
    public ArrayList<TParagraph> ParagraphList;
    
    /** Creates a new instance of TReplyDocumentMsg */
    public TReplyDocumentMsg(ArrayList<TParagraph> paragraphList) {
        
        ParagraphList = paragraphList;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {

        out.defaultWriteObject();
        
        out.writeInt( ParagraphList.size() );
        
        for(int i=0; i<ParagraphList.size(); ++i)
            out.writeObject( ParagraphList.get(i) );
    }

    private void readObject(java.io.ObjectInputStream in) 
        throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        
        int size = in.readInt();
        
        ParagraphList = new ArrayList<TParagraph>();
        
        for(int i=0; i<size; ++i)
            ParagraphList.add( (TParagraph)in.readObject() );
    }
}
