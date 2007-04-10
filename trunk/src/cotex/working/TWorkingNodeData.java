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
import cotex.TNodeInfo;
import cotex.TSession;
import cotex.TSessionInfo;

import cotex.working.TParagraph;
import cotex.working.TGap;
import cotex.working.TContent;

import java.util.ArrayList;
import java.util.Iterator;
import java.net.InetAddress;

import javax.swing.table.AbstractTableModel;
import javax.swing.AbstractListModel;

/**
 *
 * @author Ming
 */
public class TWorkingNodeData  {
        
    //----------------------------------
    // Paragraphs
    //----------------------------------
    public class Paragraphs {
        
        private ArrayList<TParagraph> mParagraphs;
        
        //------------------------------
        public Paragraphs() {
            mParagraphs = new ArrayList<TParagraph>();
        }
        
        //------------------------------
        public void add(TParagraph paragraph) {

            mParagraphs.add( paragraph );

            tableModel.notifyContentChanged();
        }

        //------------------------------
        public ArrayList<TParagraph> getList() {

            return mParagraphs;
        }

        //------------------------------
        public void setList(ArrayList<TParagraph> paragraphList) {

            mParagraphs.clear();

            for(int i=0; i<paragraphList.size(); ++i) {

                mParagraphs.add( paragraphList.get(i) );
            }

            tableModel.notifyContentChanged();

        }

        //------------------------------
        public TParagraph getById(TUniqueId id) throws TException {

            Iterator<TParagraph> iter = mParagraphs.iterator();

            while( iter.hasNext() ) {

                TParagraph paragraph = iter.next();

                if( paragraph.getId().equals( id ) )
                    return paragraph;
            }

            throw new TException("TWorkingNodeData.paragraphGetById", "paragraph does not exist");

        }
        
        //------------------------------
        public void setLocking(TUniqueId id) throws TException {

            getById(id).notifyLocking();
            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        public void setLocked(TUniqueId id, TNodeInfo owner) throws TException {

            getById(id).notifyLocked();
            getById(id).setLockOwner(owner);
            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        public void cancelLocked(TUniqueId id) throws TException {

            getById(id).notifyCancelLock();
            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        public void commit(TUniqueId id) throws TException {

            TParagraph paragraph = getById(id);

            if( paragraph.getClass().equals( TContent.class ) ) {
                
                TContent content = (TContent)paragraph;
                
                content.setContent( content.getPendingContent() );
                content.notifyUnlocked();
                content.setLockOwner( null );
                content.setPendingContent("");
            }
            else {
                throw new TException("TWorkingNodeData.paragraphs.commit", "not a content paragraph");
            }

            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        public void rollback(TUniqueId id) throws TException {

            TParagraph paragraph = getById(id);

            if( paragraph.getClass().equals( TContent.class ) ) {
                
                TContent content = (TContent)paragraph;
                
                content.notifyUnlocked();
                content.setLockOwner( null );
                content.setPendingContent("");
            }
            else {
                throw new TException("TWorkingNodeData.paragraphs.commit", "not a content paragraph");
            }

            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        public void notifyGuiUpdate() {
            
            tableModel.notifyContentChanged();
        }
        
        //------------------------------
        // TableModel
        private class TableModel extends AbstractTableModel {
            
            public Class getColumnClass(int c) {return TParagraph.class;}
            
            public int getColumnCount() {return 1;}
            
            public int getRowCount() { return mParagraphs.size(); }
            
            public TParagraph getValueAt(int r, int c) { return mParagraphs.get(r); }
            
            public boolean isCellEditable(int r, int c) {
                
                TParagraph paragraph = mParagraphs.get(r);
                
                return 
                    ( paragraph.getState() == TParagraph.State.LOCKED ) &&
                    ( nodes.self().equals( paragraph.getLockOwner() ) );
                    
            }
            
            public void setValueAt(Object value, int r, int c) {
                
                mParagraphs.set(r, (TParagraph)value);
                
                fireTableDataChanged();
            }
            
            public void notifyContentChanged() {
                this.fireTableDataChanged();
            }
        }
        
        public TableModel tableModel = new TableModel();
    }
    
    //----------------------------------
    // Sessions
    //----------------------------------
    public class Sessions {
        
        //------------------------------
        private ArrayList<TSessionInfo> mSessions;
        private TSession mCurrSession = null;
        
        //------------------------------
        public Sessions() {
            
            mSessions = new ArrayList<TSessionInfo>();
            mCurrSession = null;
        }
        
        //------------------------------
        public void add(TSessionInfo session) {
            
            mSessions.add(session);
            listModel.notifyContentChanged();
        }

        //------------------------------
        public void remove(TSessionInfo session) {
            
            mSessions.remove(session);
            listModel.notifyContentChanged();
        }
        
        //------------------------------
        public TSessionInfo getByName(String name) throws TException {

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

        //------------------------------
        public TSessionInfo getById(TUniqueId sessionId) throws TException {

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

        //------------------------------
        public void setCurrent(TUniqueId sessionId) throws TException {

            mCurrSession = new TSession( getById(sessionId) );

            nodes.listModel.notifyContentChanged();
        }

        //------------------------------
        public TSession getCurrent() {
            return mCurrSession;
        }
        
        //------------------------------
        public boolean hasCurrent() {
            return mCurrSession != null;
        }
        
        //----------------------------------
        // ListModel
        private class ListModel extends AbstractListModel {
            
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
        
        public ListModel listModel = new ListModel();
    }
    
    //----------------------------------
    // Nodes
    //----------------------------------
    public class Nodes {
    
        private TNodeInfo mSelfNodeInfo = null;
        
        //------------------------------
        public TNodeInfo self() {
            return mSelfNodeInfo;
        }

        //------------------------------
        public void setSelf(TNodeInfo nodeInfo) {
            mSelfNodeInfo = nodeInfo;
        }

        //------------------------------
        public TNodeInfo getLeft() {

            TNodeInfo node = null;

            if(null != mSelfNodeInfo && sessions.hasCurrent() ) {
                
                try {
                    
                    node = sessions.getCurrent().getLeftNode( mSelfNodeInfo );
                }
                catch(TException e) {
                    
                    TLogManager.logException(e);
                }
            }

            return node;

        }

        //------------------------------
        public TNodeInfo getRight() {

            TNodeInfo node = null;

            if(null != mSelfNodeInfo && sessions.hasCurrent() ) {
                
                try {
                    
                    node = sessions.getCurrent().getRightNode( mSelfNodeInfo );
                }
                catch(TException e) {
                    
                    TLogManager.logException(e);
                }
            }

            return node;

        }
        
        //----------------------------------
        public TNodeInfo getByAddr(InetAddress addr) throws TException {
            
            return sessions.getCurrent().getNodeByAddr(addr);
            
        }
        
        //----------------------------------
        // ListModel
        private class ListModel extends AbstractListModel {

            public Object getElementAt(int index) {

                if( !sessions.hasCurrent() )
                    return null;

                return sessions.getCurrent().getNodeAt(index);
            }

            public int getSize() {

                if( !sessions.hasCurrent() )
                    return 0;

                return sessions.getCurrent().getNodeCount();
            }

            public void notifyContentChanged() {
                fireContentsChanged( this, 0, getSize() );
            }
        }
    
        public ListModel listModel = new ListModel();
    }
    
    //----------------------------------
    public Paragraphs paragraphs                    = new Paragraphs();
    public Sessions sessions                        = new Sessions();
    public Nodes nodes                              = new Nodes();
    
    //----------------------------------
    public TWorkingNodeData() {
    }

    //----------------------------------
}
