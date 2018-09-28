package chess;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public abstract class Dialog {
    private Socket s;
    private Scanner in;
    private PrintWriter out;
    private Thread th;

    private Dialog(Socket accept) throws IOException {
        s = accept;
        in = new Scanner(s.getInputStream());
        out = new PrintWriter(s.getOutputStream());
        th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (s.isClosed()) {
                        onClosed();
                        th.stop();
                    }
                    if (in.hasNext()) {
                        onMessage(in.nextLine());
                    }
                }
            }
        });
        th.start();
    }

    public void send(String str) {
        out.println(str);
        out.flush();
    }

    public abstract void onMessage(String line);

    public abstract void onClosed();


    public static Dialog open(int port, Handler h) throws IOException {
        ServerSocket s = new ServerSocket(port);
        System.err.println("Open");
        Socket soc = s.accept();
        System.err.println("Connected");
        return new Dialog(soc) {
            @Override
            public void onMessage(String line) {
                h.onMessage(line);
            }

            @Override
            public void onClosed() {
                h.onClosed();
            }

        };
    }

    public void close() throws IOException {
        s.close();
    }

    public static Dialog connect(String ip, int port, Handler h) throws IOException {
        return new Dialog(new Socket(ip, port)) {
            @Override
            public void onMessage(String line) {
                h.onMessage(line);
            }

            @Override
            public void onClosed() {
                h.onClosed();
            }
        };
    }

    public interface Handler {

        void onMessage(String line);

        void onClosed();
    }
}
