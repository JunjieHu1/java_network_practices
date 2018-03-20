import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


public class PooledDaytimeServer {

    //此服务器监听的端口
    public static final int PORT = 13;

    //主函数
    public static void main(String[] args) {
        
        //创建一个固定大小的线程池
        ExecutorService pool = Executors.newFixedThreadPool(50);

        //创建服务器监听套接字，监听指定的端口，然后将收到的请求交个进程池处理
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try{
                Socket connection = server.accept();
                //将这个连接交给线程池处理
                Callable<Void> task = new DaytimeTask(connection);
                pool.submit(task);
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //实现Callable<Void>接口
    public static class DaytimeTask implements Callable<Void> {

        private Socket connection;

        DaytimeTask (Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() {
            try {
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString() + "\r\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    //忽略
                }
            }
            return null;
        }
    }
}