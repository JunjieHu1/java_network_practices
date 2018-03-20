import java.io.*;
import java.net.*;
import java.util.Date;

public class MultiThreadDaytimeServer {

    public static final int PORT = 13;

    public static void main(String[] args) {

        //创建一个服务器套接字
        try (ServerSocket server = new ServerSocket(PORT)) {
            //开始循环监听
            while (true) {
                try {
                    Socket connection = server.accept();
                    //将建立的连接交给一个县城处理
                    Thread task = new DaytimeThread(connection);
                    //启动线程
                    task.start();
                } catch (IOException e) {}
            }
        } catch (IOException e) {
            System.err.println("Could't start the server!!!");
        }
    }

    //创建一个内部类
    private static class DaytimeThread extends Thread {
        //声明一个Socket变量，将在构造函数之中使用
        private Socket connection;

        DaytimeThread (Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString());
                out.write("\r\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }

    }
}