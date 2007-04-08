/*
 * TWorkingNodeData.java
 *
 * Created on April 6, 2007, 6:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.working;


import cotex.*;
import cotex.session.*;

import cotex.working.TParagraph;
import cotex.working.TGap;
import cotex.working.TContent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import javax.swing.AbstractListModel;

/**
 *
 * @author Ming
 */
public class TWorkingNodeData  {
    
    private ArrayList<TParagraph> mParagraphs;
    
    private ArrayList<TSessionInfo> mSessions;
    private TSession mCurrSession = null;
    
    private TNodeInfo mSelfNodeInfo = null;
    
    //----------------------------------
    // DocumentTableModel
    private class DocumentTableModel extends AbstractTableModel {
        
        public Class getColumnClass(int c) {return TParagraph.class;}
    
        public int getColumnCount() {return 1;}

        public int getRowCount() { return mParagraphs.size(); }

        public TParagraph getValueAt(int r, int c) { return mParagraphs.get(r); }

        public boolean isCellEditable(int r, int c) {
            
            return true;
        }

        public void setValueAt(Object value, int r, int c) {
            //para.setElementAt((TContent) value,r);

            mParagraphs.set(r, (TParagraph)value);

            fireTableDataChanged();
        }
        
        public void notifyContentChanged() {
           this.fireTableDataChanged();
        }
    }
    
    DocumentTableModel mDocumentTableModel = new DocumentTableModel();
    
    public DocumentTableModel getDocumentTableModel() {
        return mDocumentTableModel;
    }
    
    //----------------------------------
    // SessionListModel
    private class SessionListModel extends AbstractListModel {
        
        public Object getElementAt(int index) {
            return mSessions.get(index);
        }
        
        public int getSize() {
            return mSessions.size();
        }
        
        public void notifyContentChanged() {
            fireContentsChanged( this, 0, getSize() );
        }
    }
    
    SessionListModel mSessionListModel = new SessionListModel();
    
    public SessionListModel getSessionListModel() {
        return mSessionListModel;
    }
    
    //----------------------------------
    // WorkerListModel
    private class WorkerListModel extends AbstractListModel {
        
        public Object getElementAt(int index) {
            
            if(null == mCurrSession)
                return null;
            
            return mCurrSession.getNodeAt(index);
        }
        
        public int getSize() {
            
            if(null == mCurrSession)
                return 0;
            
            return mCurrSession.getNodeCount();
        }
        
        public void notifyContentChanged() {
            fireContentsChanged( this, 0, getSize() );
        }
    }
    
    WorkerListModel mWorkerListModel = new WorkerListModel();
    
    public WorkerListModel getWorkerListModel() {
        return mWorkerListModel;
    }
    
    //----------------------------------
    public TWorkingNodeData() {
        
        mSessions = new ArrayList<TSessionInfo>();
        mCurrSession = null;
        
        mParagraphs = new ArrayList<TParagraph>();
    }
    
    //----------------------------------
    // Paragraphs
    //----------------------------------
    public void paragraphAdd(TParagraph paragraph) {
        
        mParagraphs.add( paragraph );
        
        mDocumentTableModel.notifyContentChanged();
    }
    
    //----------------------------------
    public ArrayList<TParagraph> paragraphGetList() {
        
        return mParagraphs;
    }
    
    //----------------------------------
    public void paragraphSetList(ArrayList<TParagraph> paragraphList) {
        
        mParagraphs.clear();
        
        for(int i=0; i<paragraphList.size(); ++i) {
        
            mParagraphs.add( paragraphList.get(i) );
        }
        
        mDocumentTableModel.notifyContentChanged();
        
    }
    
    //----------------------------------
    public TParagraph paragraphGetById(TUniqueId id) throws TException {
        
        Iterator<TParagraph> iter = mParagraphs.iterator();
        
        while( iter.hasNext() ) {
            
            TParagraph paragraph = iter.next();
            
            if( paragraph.getId().equals( id ) )
                return paragraph;
        }
        
        throw new TException("TWorkingNodeData.paragraphGetById", "paragraph does not exist");
        
    }
    
    //----------------------------------
    public void paragraphUpdateContent(TUniqueId id, String content) throws TException {
        
        TParagraph paragraph = paragraphGetById(id);
        
        if( paragraph.getClass().equals( TContent.class ) ) {
            ( (TContent)paragraph ).setContent( content );
        }
        else {
            throw new TException("TWorkingNodeData.paragraphUpdateContent", "not a content paragraph");
        }
        
        mDocumentTableModel.notifyContentChanged();
    }
    
    //----------------------------------
    // Session
    //----------------------------------
    public void sessionAdd(TSessionInfo session) {
        mSessions.add(session);
        mSessionListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public void sessionRemove(TSessionInfo session) {
        mSessions.remove(session);
        mSessionListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public TSessionInfo sessionGetByName(String name) throws TException {
        
        Iterator<TSessionInfo> iter = mSessions.iterator();
        
        while( iter.hasNext() ) {
            
            TSessionInfo session = iter.next();
            
            if( session.getName().equals(name) )
                return session;
            
        }
        
        throw new TException(
            "TWorkingNodeData.getSessionByName",
            "Session \"" + name + "\" not found");
    }
    
    //----------------------------------
    public TSessionInfo sessionGetById(TUniqueId sessionId) throws TException {
        
        Iterator<TSessionInfo> iter = mSessions.iterator();
        
        while( iter.hasNext() ) {
            
            TSessionInfo session = iter.next();
            
            if( session.getId().equals(sessionId) )
                return session;
            
        }
        
        throw new TException(
            "TWorkingNodeData.getSessionById",
            "Session \"" + sessionId.toString() + "\" not found");
    }
    
    //----------------------------------
    public void sessionSetCurrent(TUniqueId sessionId) throws TException {
        
        mCurrSession = new TSession( sessionGetById(sessionId) );
        
        mWorkerListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public TSession sessionGetCurrent() {
        return mCurrSession;
    }
    
    //----------------------------------
    // Node
    //----------------------------------
    public TNodeInfo nodeGetSelf() {
        return mSelfNodeInfo;
    }
    
    //----------------------------------
    public void nodeSetSelf(TNodeInfo nodeInfo) {
        mSelfNodeInfo = nodeInfo;
    }
    
    //----------------------------------
    public TNodeInfo nodeGetLeft() {
        
        TNodeInfo node = null;
        
        if(null != mSelfNodeInfo && null != mCurrSession) {
            try {
                node =  mCurrSession.getLeftNode( mSelfNodeInfo );
            }
            catch(TException e) {
                TLogManager.logException(e);
            }
        }
        
        return node;
        
    }
    
    //----------------------------------
    public TNodeInfo nodeGetRight() {
        
        TNodeInfo node = null;
        
        if(null != mSelfNodeInfo && null != mCurrSession) {
            try {
                node =  mCurrSession.getRightNode( mSelfNodeInfo );
            }
            catch(TException e) {
                TLogManager.logException(e);
            }
        }
        
        return node;
        
    }
    
    //----------------------------------
}
