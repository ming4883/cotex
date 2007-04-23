/*
 * TRegServer.java
 *
 * Created on 2007年4月23日, 下午 12:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;

import java.io.IOException;
import java.net.*;
import java.util.*;
/**
 *
 * @author wing
 */
public class TRegServer extends Thread {
    
    private DatagramSocket ds = null;
    private Vector addresses = null;
    private Vector ports = null;
    private ServerSocket servSock = null;
    private Socket mSendSock = null;
    /** Creates a new instance of TRegServer */
    public TRegServer() {
        try {
            servSock = new ServerSocket(10000);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        addresses = new Vector();
        ports = new Vector();
    }
    
    public void run(){
        byte[] buf=new byte[256];
        try{
            while(true){
                Socket clientSock = servSock.accept();
                //DatagramPacket packet=new DatagramPacket(buf,buf.length);
                //ds.receive(packet);
                TRegReceiveThread rThread = new TRegReceiveThread(clientSock, addresses, ports);
                rThread.start();
            }
        }catch(IOException e){
            System.out.println("IOException :"+e);
        }
    }
}
