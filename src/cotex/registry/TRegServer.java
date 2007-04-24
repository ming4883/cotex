/*
 * TRegServer.java
 *
 * Created on 2007年4月23日, 下午 12:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cotex.registry;

import cotex.TConfig;
import cotex.TNodeInfo;
import cotex.TSessionInfo;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author wing
 */
public class TRegServer {
    
    
    
    /** Creates a new instance of TRegServer */
    public TRegServer() {
        
    }
    
    public static void main(String[] args) {
        ServerSocket servSock = null;
        Socket mSendSock = null;
        
        ArrayList<TSessionInfo> mSessionList = new ArrayList<TSessionInfo>();
        ArrayList<ArrayList<TNodeInfo>> mNodeList = new ArrayList<ArrayList<TNodeInfo>>();
        TConfig config = new TConfig("cotex.config.xml");
        String strRegListenPort = config.getSetting("Reg", "listenPort");
        int regListenPort = Integer.parseInt(strRegListenPort );
        
        try {
            servSock = new ServerSocket(regListenPort);
            System.out.println("TRegServer: Start and listen at port: "+strRegListenPort );
            while(true){
                Socket clientSock = servSock.accept();
                TRegReceiveThread rThread = new TRegReceiveThread(clientSock, mSessionList, mNodeList);
                rThread.start();
            }
        }catch(IOException e){
            System.err.println("TRegServer: IOException: " + e);
        }
    }
    
    
}
