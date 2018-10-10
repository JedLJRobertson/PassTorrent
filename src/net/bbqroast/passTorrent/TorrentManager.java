package net.bbqroast.passTorrent;

import com.dosse.upnp.UPnP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TorrentManager implements ITorrent {
    private ILeadManager leadManager;
    private ServerSocket socket;
    private ArrayList<Peer> peers;

    public TorrentManager(ILeadManager leadManager, int port) {
        this.leadManager = leadManager;

        Logger.getInstance().log("Getting peers...");
        try {
            peers = leadManager.getPeers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Peer peer : peers) {
            Logger.getInstance().log(peer.toString());
        }

        // Launch listening socket
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
                        Logger.getInstance().log("Incoming connection from " + s.getInetAddress().getHostAddress()); //print remote machine IP
                        s.close(); //close the connection
                    } catch (Throwable t) {
                        Logger.getInstance().logErr("Network error: "+t);
                    }
                }
            }
        }.start();

        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            if (UPnP.isMappedTCP(port)) { //is the port already mapped?
                Logger.getInstance().log("UPnP port forwarding not enabled: port is already mapped");
            } else if (UPnP.openPortTCP(port)) { //try to map port
                Logger.getInstance().log("UPnP port forwarding enabled");
            } else {
                Logger.getInstance().log("UPnP port forwarding failed");
            }
        } else {
            Logger.getInstance().log("UPnP is not available");
        }
    }
}
