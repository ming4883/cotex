/*
 * TRegReceiveThread.java
 *
 * Created on 2007年4月23日, 下午 1:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.registry;
import cotex.TSessionInfo;
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
    ArrayList sessionNodeList = null;
    /** Creates a new instance of TRegReceiveThread */
    public TRegReceiveThread() {
    }
    
    public TRegReceiveThread(Socket sock, ArrayList<TSessionInfo> SessionList, ArrayList SessionNodeList){
        clientSock = sock;
        this.sessionList = SessionList;
        this.sessionNodeList = SessionNodeList;
    }
    
    public void run(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream( clientSock.getInputStream() );
            Object obj = null;
            try {
                obj = inputStream.readObject();
                //mConnection.fireObjectReceivedNotification(obj);
                this.sleep(1);
            } catch(ClassNotFoundException e) {
                System.err.println("TRegReceiveThread: unknown object recieved");
            } catch(InterruptedException e) {
                System.err.println("TRegReceiveThread: Exception thrown:" +e);
            } catch(IOException e) {
                System.err.println("TRegReceiveThread: Exception thrown:" +e);
            }
            inputStream.close();
            if(obj.getClass().equals(TRequestSessionInfoMsg.class)) {
                _onReceiveTRequestSessionIfoMsg((TRequestSessionInfoMsg)obj);
            }
            
            clientSock.close();
        } catch(IOException e) {
        }
        //System.out.println("TRegReceiveThread: cmd object received: " + obj.toString() );
        /*addresses.add(clientSock.getInetAddress());
        ports.add(clientSock.getPort());
        String addressList="";
        addressList+=";"+addresses.get(0)+":"+ports.get(0);
        for(int i =1;i<addresses.size();i++) {
            addressList+=";"+((String)addresses.get(i))+":"+((String)ports.get(i));
        }
        //buf=addressList.getBytes();
        Socket sendSock;
        for(int i =0;i<addresses.size();i++) {
            PrintStream bw;
            try {
                sendSock = new Socket((InetAddress)(addresses.get(i)),Integer.parseInt(ports.get(i).toString()));
                bw = new PrintStream(sendSock.getOutputStream());
                bw.println(addressList);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }*/
    }
    public void _onReceiveTRequestSessionIfoMsg(TRequestSessionInfoMsg msg) {
        try {
            ObjectOutputStream mSendOStream = new ObjectOutputStream( clientSock.getOutputStream() );
            mSendOStream.writeObject(new TReplySessionInfoMsg(sessionList));
            mSendOStream.flush();
            mSendOStream.close();
            System.out.println("TRegReceiveThread: _onReceiveTRequestSessionIfoMsg ok");
        } catch (IOException ex) {
            System.err.println("TRegReceiveThread: _onReceiveTRequestSessionIfoMsg error");
        }
    }
}
