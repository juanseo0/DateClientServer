import java.net.*;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateServerMTP {

    protected static int port = 6013;

    public static void main(String[] args) {
        if (args.length >= 1) {
            if (isNumber(args[0])) {
                port = Integer.parseInt(args[0]);
            } else {
                System.out.println("Invalid port: " + args[0]);
				return;
            }
        }
        runServer();
    }

    public static void runServer(){
        ExecutorService execService = Executors.newCachedThreadPool();
        try {
            ServerSocket sock = new ServerSocket(port);
            while(true) {
                Socket client = sock.accept();
                execService.submit(new dateHandler(client));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    static class dateHandler implements Runnable {

        private final Socket currSocket;

        public dateHandler(Socket client) {
            this.currSocket = client;
        }

        @Override
        public void run() {
            try {
                PrintWriter pout = new PrintWriter(currSocket.getOutputStream(), true);
                pout.println(new java.util.Date());
                currSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
