package net.bbqroast.passTorrent;


import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            PassTorrent client = new PassTorrent(new TorrentManager(new GETLeadManager("http://localhost/leads.php"), 7890));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
