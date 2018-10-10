package net.bbqroast.passTorrent;

public class Logger {
    private static Logger ourInstance = new Logger();

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
    }

    public void log(String out)    {
        System.out.println(out);
    }

    public void logErr(String out)    {
        System.err.println(out);
    }
}
