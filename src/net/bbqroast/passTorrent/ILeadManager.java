package net.bbqroast.passTorrent;

import java.io.IOException;
import java.util.ArrayList;

public interface ILeadManager {
    ArrayList<Peer> getPeers() throws IOException;
}
