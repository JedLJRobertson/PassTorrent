package net.bbqroast.passTorrent;

import com.dosse.upnp.UPnP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TorrentManager implements ITorrent {
    private ILeadManager leadManager;
    private ServerSocket socket;

    public TorrentManager(ILeadManager leadManager, int port) {
        this.leadManager = leadManager;

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        Socket s = socket.accept(); //wait for connections on socket
                        System.out.println("Incoming connection from " + s.getInetAddress().getHostAddress()); //print remote machine IP
                        s.close(); //close the connection
                    } catch (Throwable t) {
                        System.err.println("Network error: "+t);
                    }
                }
            }
        }.start();

        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            if (UPnP.isMappedTCP(port)) { //is the port already mapped?
                System.out.println("UPnP port forwarding not enabled: port is already mapped");
            } else if (UPnP.openPortTCP(port)) { //try to map port
                System.out.println("UPnP port forwarding enabled");
            } else {
                System.out.println("UPnP port forwarding failed");
            }
        } else {
            System.out.println("UPnP is not available");
        }
    }
}
