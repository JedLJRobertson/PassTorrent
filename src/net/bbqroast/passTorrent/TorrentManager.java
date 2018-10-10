package net.bbqroast.passTorrent;

import com.dosse.upnp.UPnP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TorrentManager implements ITorrent {
    private ILeadManager leadManager;
    private ServerSocket socket;
    private ArrayList<Peer> peers = new ArrayList<>();
    private int port;

    public TorrentManager(ILeadManager leadManager, int port) {
        this.leadManager = leadManager;
        this.port = port;

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
                        acceptConnection(s);
                    } catch (Throwable t) {
                        Logger.getInstance().logErr("Network error: "+t);
                    }
                }
            }
        }.start();

        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            if (UPnP.isMappedTCP(port)) { //is the port already mapped?
                Logger.getInstance().log("UPnP port forwarding not enabled: port is already mapped");
                leadManager.register(port);
            } else if (UPnP.openPortTCP(port)) { //try to map port
                Logger.getInstance().log("UPnP port forwarding enabled");
                leadManager.register(port);
            } else {
                Logger.getInstance().log("UPnP port forwarding failed");
            }
        } else {
            Logger.getInstance().log("UPnP is not available");
        }

        // Find some peers
        Logger.getInstance().log("Getting peers...");
        try {
            leadManager.getPeers(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void acceptConnection(Socket s) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        new Thread() {
            @Override
            public void run() {
                for (;;) {
                    String received, line;
                    received = "";
                    try {
                        while ((line = in.readLine()) != null) {
                            if (line.equals("passtorrent")) {
                                addPeer(s.getInetAddress().getHostAddress(), s.getPort(), s);
                                return;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Adds a peer with a given host and port, assuming not duplicate
     * @param host
     * @param port
     */
    public synchronized Peer addPeer(String host, int port)  { // TODO: Validate hosts
        if ((host.equals("127.0.0.1") || host.equals("localhost")) && port == this.port)    {
            return null;
        }
        for (Peer peer : peers) {
            if (peer.getHost().equals(host) && peer.getPort() == port)  {
                return null;
            }
        }

        Peer peer = new Peer(host, port);
        peers.add(peer);
        return peer;
    }

    /**
     * Adds a peer with a given host and port and socket, assuming not duplicate
     * @param host
     * @param port
     */
    public synchronized Peer addPeer(String host, int port, Socket sock)  { // TODO: Validate hosts
        if ((host.equals("127.0.0.1") || host.equals("localhost")) && port == this.port)    {
            return null;
        }
        for (Peer peer : peers) {
            if (peer.getHost().equals(host) && peer.getPort() == port)  {
                return null;
            }
        }

        Peer peer = new Peer(host, port,sock);
        peers.add(peer);
        return peer;
    }
}
