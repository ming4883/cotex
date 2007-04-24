/*
 * TRegReceiveThread.java
 *
 * Created on 2007年4月23日, 下午 1:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.registry;
import cotex.TNodeInfo;
import cotex.TSessionInfo;
import cotex.TUniqueId;
import cotex.working.msg.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;

/**
 *
 * @author wing
 */
public class TRegReceiveThread extends Thread{
    
    Socket clientSock = null;
    ArrayList<TSessionInfo> sessionList = null;
    ArrayList<ArrayList<TNodeInfo>> sessionNodeList = null;
    /** Creates a new instance of TRegReceiveThread */
    public TRegReceiveThread() {
    }
    
    private int getSessionIndexByName(String name){
        for(int i=0;i<sessionList.size();i++){
            if(sessionList.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }
    private int getSessionIndexById(TUniqueId sessionId){
        for(int i=0;i<sessionList.size();i++){
            if(sessionList.get(i).getId().equals(sessionId))
                return i;
        }
        return -1;
    }
    public TRegReceiveThread(Socket sock, ArrayList<TSessionInfo> SessionList, ArrayList<ArrayList<TNodeInfo>> SessionNodeList){
        clientSock = sock;
        this.sessionList = SessionList;
        this.sessionNodeList = SessionNodeList;
    }
    
    public void run(){
        try {
            ObjectOutputStream mSendOStream = new ObjectOutputStream( clientSock.getOutputStream() );
            ObjectInputStream inputStream = new ObjectInputStream( clientSock.getInputStream() );
            Object obj = null;
            try {
                obj = inputStream.readObject();
                this.sleep(1);
            } catch(ClassNotFoundException e) {
                System.err.println("TRegReceiveThread: unknown object recieved");
            } catch(InterruptedException e) {
                System.err.println("TRegReceiveThread: Exception thrown:" +e);
            } catch(IOException e) {
                System.err.println("TRegReceiveThread: Exception thrown:" +e);
            }
            //inputStream.close();
            System.out.println("TRegReceiveThread: object recieved");
            if(obj.getClass().equals(TRequestSessionInfoMsg.class)) {
                _onReceiveTRequestSessionInfoMsg((TRequestSessionInfoMsg)obj,mSendOStream);
            }else if(obj.getClass().equals(TNewSessionMsg.class)) {
                _onReceiveTNewSessionMsg((TNewSessionMsg)obj,mSendOStream);
            }else if(obj.getClass().equals(TJoinSessionMsg.class)) {
                _onReceiveTJoinSessionMsg((TJoinSessionMsg)obj,mSendOStream);
            } else if(obj.getClass().equals(TRequestNodeInfoMsg.class)) {
                _onReceiveTRequestNodeInfoMsg((TRequestNodeInfoMsg)obj,mSendOStream);
            }
            
            clientSock.close();
        } catch(IOException e) {
        }
    }
    
    private synchronized void addNewSession(TSessionInfo session,ArrayList<TNodeInfo> NodesOfNewSession){
        sessionList.add(session);
        sessionNodeList.add(NodesOfNewSession);
    }
    private synchronized void addNewNode(int index, TNodeInfo newNodeInfo){
        ArrayList<TNodeInfo> currentNodesOfSession =sessionNodeList.get(index);
        System.out.print(currentNodesOfSession.size());
        currentNodesOfSession.add(newNodeInfo);
    }
    
    public void _onReceiveTRequestSessionInfoMsg(TRequestSessionInfoMsg msg, ObjectOutputStream mSendOStream) {
        try {
            mSendOStream.writeObject(new TReplySessionInfoMsg(sessionList));
            mSendOStream.flush();
            // mSendOStream.close();
            System.out.println("TRegReceiveThread: _onReceiveTRequestSessionInfoMsg ok");
        } catch (IOException ex) {
            System.err.println("TRegReceiveThread: _onReceiveTRequestSessionInfoMsg error"+ex.toString());
        }
    }
    public void _onReceiveTNewSessionMsg(TNewSessionMsg msg, ObjectOutputStream mSendOStream) {
        try {
            int index = getSessionIndexByName(msg.sessionName);
            if(index==-1) {
                TSessionInfo session = new TSessionInfo( msg.sessionName );
                ArrayList<TNodeInfo> NodesOfNewSession = new ArrayList<TNodeInfo>();
                addNewSession( session,NodesOfNewSession);
                mSendOStream.writeObject(new TReplyNewSessionMsg(session.getId()));
            } else{
                mSendOStream.writeObject(new TReplyNewSessionMsg(null));
            }
            mSendOStream.flush();
            // mSendOStream.close();
            System.out.println("TRegReceiveThread: _onReceiveTNewSessionMsg ok");
        } catch (IOException ex) {
            System.err.println("TRegReceiveThread: _onReceiveTNewSessionMsg error"+ex.toString());
        }
    }
    public void _onReceiveTJoinSessionMsg(TJoinSessionMsg msg, ObjectOutputStream mSendOStream) {
        try {
            int index = getSessionIndexById(msg.sessionId);
            if(index>-1) {
                addNewNode(index,msg.workerInfo);
                for(int i=0;i<sessionNodeList.get(index).size();i++){
                    if(!sessionNodeList.get(index).get(i).equals(msg.workerInfo)){
                        InetAddress addr = sessionNodeList.get(index).get(i).getAddr();
                        int port = sessionNodeList.get(index).get(i).getPortByType("Reg");
                        Socket ackSock = new Socket(addr,port);
                        ObjectOutputStream mAckOStream = new ObjectOutputStream( ackSock.getOutputStream() );
                        mAckOStream.writeObject(new TReplyJoinSessionMsg(sessionNodeList.get(index)) );
                        mAckOStream.flush();
                        mAckOStream.close();
                        ackSock.close();
                    }
                }
                mSendOStream.writeObject(new TReplyJoinSessionMsg(sessionNodeList.get(index)));
            }else{
                ArrayList<TNodeInfo> NodesOfSession = new ArrayList<TNodeInfo>();
                mSendOStream.writeObject(new TReplyJoinSessionMsg(NodesOfSession));
            }
            mSendOStream.flush();
            // mSendOStream.close();
            System.out.println("TRegReceiveThread: _onReceiveTJoinSessionMsg ok");
        } catch (IOException ex) {
            System.err.println("TRegReceiveThread: _onReceiveTJoinSessionMsg error"+ex.toString());
        }
    }
    public void _onReceiveTRequestNodeInfoMsg(TRequestNodeInfoMsg msg, ObjectOutputStream mSendOStream) {
        try {
            int index = getSessionIndexById(msg.sessionId);
            if(index>-1) {
                mSendOStream.writeObject(new TReplyJoinSessionMsg(sessionNodeList.get(index)));
            }else{
                ArrayList<TNodeInfo> NodesOfSession = new ArrayList<TNodeInfo>();
                mSendOStream.writeObject(new TReplyJoinSessionMsg(NodesOfSession));
            }
            mSendOStream.flush();
            // mSendOStream.close();
            System.out.println("TRegReceiveThread: _onReceiveTRequestNodeInfoMsg ok");
        } catch (IOException ex) {
            System.err.println("TRegReceiveThread: _onReceiveTRequestNodeInfoMsg error"+ex.toString());
        }
    }
}
