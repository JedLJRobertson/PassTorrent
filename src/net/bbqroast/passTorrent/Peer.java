package net.bbqroast.passTorrent;

public class Peer {
    private String host; // Surely a better type for this + validation TODO
    private int port;

    public Peer(String host, int port)    {
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString()    {
        return host+":"+port;
    }
}
