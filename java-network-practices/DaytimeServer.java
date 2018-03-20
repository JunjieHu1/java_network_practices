import java.io.*;
import java.net.*;
import java.util.Date;

public class DaytimeServer {

    public static final int DEFAULT_PORT = 13;

    public static void main(String[] args) {

        //创建一个监听13端口的服务器socket
        try (ServerSocket server = new ServerSocket(13)) {
            //开始无限循环，准备时刻接收一个连接
            while (true) {
                try (Socket connection = server.accept()) {
                    //接受了一个请求并建立了连接之后，通过服务器的输出流，输出响应
                    //获取一个输出流写出器
                    Writer out = new OutputStreamWriter(connection.getOutputStream());
                    Date now = new Date();
                    out.write(now.toString());
                    out.flush();
                    //响应写入完毕之后，关闭连接
                    out.close();
                } catch (IOException e  ) { }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}