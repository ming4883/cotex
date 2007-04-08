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

import cotex.working.TParagraphBase;
import cotex.working.TGap;
import cotex.working.TParagraph;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import javax.swing.AbstractListModel;

/**
 *
 * @author Ming
 */
public class TWorkingNodeData  {
    
    private ArrayList<TParagraphBase> mParagraphs;
    
    private ArrayList<TSessionInfo> mSessions;
    private TSession mCurrSession = null;
    
    private TNodeInfo mSelfNodeInfo = null;
    
    //----------------------------------
    // DocumentTableModel
    private class DocumentTableModel extends AbstractTableModel {
        
        public Class getColumnClass(int c) {return TParagraphBase.class;}
    
        public int getColumnCount() {return 1;}

        public int getRowCount() { return mParagraphs.size(); }

        public TParagraphBase getValueAt(int r, int c) { return mParagraphs.get(r); }

        public boolean isCellEditable(int r, int c) {
            return !mParagraphs.get(r).isLocked();
        }

        public void setValueAt(Object value, int r, int c) {
            //para.setElementAt((TParagraph) value,r);

            mParagraphs.set(r, (TParagraphBase)value);

            fireTableDataChanged();
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
    
  
    /**
     * Creates a new instance of TWorkingNodeData
     */
    public TWorkingNodeData() {
        
        mSessions = new ArrayList<TSessionInfo>();
        mCurrSession = null;
        
        mParagraphs = new ArrayList<TParagraphBase>();
        
        mParagraphs.add( new TGap() );
        mParagraphs.add( new TParagraph("This is a test paragraph 1.") );
        mParagraphs.add( new TGap() );
        mParagraphs.add( new TParagraph("This is a test paragraph 2.") );
        mParagraphs.add( new TGap() );
    }
    
    //----------------------------------
    public void addSessionInfo(TSessionInfo session) {
        mSessions.add(session);
        mSessionListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public void removeSessionInfo(TSessionInfo session) {
        mSessions.remove(session);
        mSessionListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public TSessionInfo getSessionInfoByName(String name) throws TException {
        
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
    public TSessionInfo getSessionInfoById(TUniqueId sessionId) throws TException {
        
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
    public void setCurrentSession(TUniqueId sessionId) throws TException {
        
        mCurrSession = new TSession( getSessionInfoById(sessionId) );
        
        mWorkerListModel.notifyContentChanged();
    }
    
    //----------------------------------
    public TSession getCurrentSession() {
        return mCurrSession;
    }
    
    //----------------------------------
    public TNodeInfo getSelfNodeInfo() {
        return mSelfNodeInfo;
    }
    
    //----------------------------------
    public void setSelfNodeInfo(TNodeInfo nodeInfo) {
        mSelfNodeInfo = nodeInfo;
    }
}
