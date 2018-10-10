package net.bbqroast.passTorrent;

import java.io.IOException;
import java.util.ArrayList;

public interface ILeadManager {
    void getPeers(ITorrent manager) throws IOException;
    void register(int port);
}
