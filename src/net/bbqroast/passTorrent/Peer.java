package net.bbqroast.passTorrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Peer {
    private String host; // Surely a better type for this + validation TODO
    private int port;
    private Socket sock;


    public enum Status {
        StandBy,
        Failed,
        Online
    }
    private  Status state;

    public Peer(String host, int port, Socket sock)    {
        this.host = host;
        this.port = port;
        this.sock = sock;
        state = Status.Online;
    }


    public Peer(String host, int port)    {
        this.host = host;
        this.port = port;
        state = Status.StandBy;

        connect();
    }

    @Override
    public String toString()    {
        return host+":"+port;
    }

    private void connect()   {
        try {
            Socket echoSocket = new Socket(host, port);
            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            Logger.getInstance().log(toString()+" - Connected.");
            out.println("passtorrent");
            out.flush();
            state = Status.Online;
        } catch (UnknownHostException e) {
            Logger.getInstance().log(toString()+" - Unknown host.");
            state = Status.Failed;
        } catch (IOException e) {
            Logger.getInstance().log(toString()+" - IO Exception.");
            state = Status.Failed;
        }
    }

    public Status getState()    {
        return state;
    }


    public String getHost() {
        return host;
    }

    public int getPort()    {
        return port;
    }
}
