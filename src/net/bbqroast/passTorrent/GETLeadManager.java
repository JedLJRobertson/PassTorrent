package net.bbqroast.passTorrent;

import net.bbqroast.http.HTTPUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

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
}
