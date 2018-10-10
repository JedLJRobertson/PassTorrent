package net.bbqroast.passTorrent;

public interface ITorrent {
    Peer addPeer(String host, int port);
}
