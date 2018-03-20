import java.io.*;
import java.net.*;

/**
 * 获取Socket信息
 * @author Junjie Hu
 * @version 0.1.1 
 * 
 */

 public class SocketInfo {

    //获取Socket信息
    public static void main(String[] args) {

        for (String host: args) {
            try {

                Socket socket = new Socket(host, 80);
                System.out.println("connect to " + socket.getInetAddress() 
                + " on port " + socket.getPort() + " from port of " 
                + socket.getLocalPort() + " of " + socket.getLocalAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 }