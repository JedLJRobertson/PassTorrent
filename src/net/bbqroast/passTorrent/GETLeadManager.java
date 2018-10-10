package net.bbqroast.passTorrent;

import com.sun.javaws.exceptions.ErrorCodeResponseException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import net.bbqroast.http.HTTPUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GETLeadManager implements ILeadManager {
    private String trackerURL;

    public GETLeadManager(String trackerURL) throws IOException {
        if (!testHost(trackerURL))    {
            throw new UnknownHostException("Connection Error");
        }

        this.trackerURL = trackerURL;
    }

    private boolean testHost(String host) throws IOException {
        HttpURLConnection conn = HTTPUtil.getURL(host);

        if (conn.getResponseCode() == 200)    {
            return true;
        }
        return false;
    }

    public void getPeers(ITorrent manager) throws IOException {
        HttpURLConnection conn = HTTPUtil.getURL(trackerURL);

        if (conn.getResponseCode() != 200)    {
            throw new IOException(conn.getURL() + " Returned: " +conn.getResponseCode());
        }

        String data = HTTPUtil.readConn(conn);

        for (String host : data.split(";")) {
            if (host.matches("[a-zA-Z0-9:\\.]*[-][0-9\\.]*")) {
                Peer peer = manager.addPeer(host.split("-")[0], Integer.valueOf(host.split("-")[1]));
            }
        }
    }

    public void register(int port)   {
        try {
            HttpURLConnection conn = HTTPUtil.getURL(trackerURL + "?reg=" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
