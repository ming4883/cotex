/*
 * TRegReceiveThread.java
 *
 * Created on 2007年4月23日, 下午 1:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.*;
/**
 *
 * @author wing
 */
public class TRegReceiveThread extends Thread{
    
    Socket clientSock = null;
    Vector addresses = null;
    Vector ports = null;
    /** Creates a new instance of TRegReceiveThread */
    public TRegReceiveThread() {
    }
    
    public TRegReceiveThread(Socket sock, Vector addresses, Vector ports){
        clientSock = sock;
        this.addresses = addresses;
        this.ports = ports;
    }
    
    public void run(){
         addresses.add(clientSock.getInetAddress());
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
             //DatagramPacket sendPacket=new DatagramPacket(buf,buf.length,(InetAddress)(addresses.get(i)),Integer.parseInt(ports.get(i).toString()));
            //  ds.send(sendPacket);
         }
    }
}
