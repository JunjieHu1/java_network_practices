import java.io.*;
import java.net.*;

//查看指定主机上前1024个端口中那些端口安装有TCP服务器
public class LowPortScanner {

    public static void main(String[] args) {

        String host = args.length > 0 ? args[0] : "localhost" ;
        
        for (int i = 0; i < 1024; i++) {
            try {
                Socket socket = new Socket(host, i);
                System.out.println("There is a server on port " + i + "of" + host);
            } catch (UnknownHostException e) {
                System.out.println(e);
                break;
            } catch (IOException e) {
                System.out.println("there is no server on  port number" + i);
            }
        }
    }
}