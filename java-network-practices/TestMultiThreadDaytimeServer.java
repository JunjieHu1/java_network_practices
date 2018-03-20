import java.io.*;
import java.net.*;



public class TestMultiThreadDaytimeServer {

    public static void main(String[] args) throws Exception {
        InetAddress machine = InetAddress.getLocalHost();
        String localhost = machine.getHostName();
        int port = 13;

        for (int i = 0; i < 10000000; i++) {
            Socket socket = new Socket(localhost, port);
            socket.setSoTimeout(15000);
            InputStreamReader r = new InputStreamReader(socket.getInputStream(), "ASCII");
            StringBuilder time = new StringBuilder();
            int c = 0;
            while ((c = r.read()) != -1) {
                time.append((char) c);
            }
            System.out.println(i+" " +time);
            
            
        }
    }
}