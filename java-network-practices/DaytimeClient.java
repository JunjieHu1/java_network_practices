import java.io.*;
import java.net.*;

public class DaytimeClient {
 
    public static void main(String[] args) {

        String host = args.length > 0 ? args[0] : "time.nist.gov";
        Socket socket = null;

        try {//建立socket连接
            socket = new Socket(host, 13);
            //设置尝试连接时间上限
            socket.setSoTimeout(15000);
            //从连接中获取输入流
            InputStream in = socket.getInputStream();
            //创建读取输入流的读取器
            Reader r = new InputStreamReader(in, "ASCII");
            //创建可变字符串
            StringBuilder time = new StringBuilder();
            //读取输入流中的信息，将其逐个加入time可变字符串中
            for (int c = r.read(); c != -1; c = r.read()) {
                time.append((char) c);
            }
            //将完整的时间输出
            System.out.println(time);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}