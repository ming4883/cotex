/*
 * TAccessRightQuery.java
 *
 * Created on 2007�~4��25��, �W�� 3:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

/**
 *
 * @author cyrux
 */
public class TAccessRightQuery {
    
    TNodeInfo requester;
    TUniqueId paragraphId;
    
    /** Creates a new instance of TAccessRightQuery */
    public TAccessRightQuery(TNodeInfo Requester, TUniqueId ParagraphId) {
        
        requester = Requester;
        paragraphId = ParagraphId;
    }
    
    public TNodeInfo getRequester() {
        return requester;
    }
    
    public TUniqueId getParagraphId() {
        return paragraphId;
    }
    
    public String toString(){
        return requester.toString() + " request: " + paragraphId.toString();
    }
}
