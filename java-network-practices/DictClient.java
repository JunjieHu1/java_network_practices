import java.io.*;
import java.net.*;

public class DictClient {

    //创建常量
    private static final String SERVER = "dict.org";
    private static final int PORT = 2628;
    public static final int TIMEOUT = 15000;

    public static void main(String[] args) {

        Socket socket = null;

        try {
            socket = new Socket(SERVER, PORT);
            socket.setSoTimeout(TIMEOUT);
            //获取输出流连接
            OutputStream out = socket.getOutputStream();
            //首先建立输出流读取器，然后对输出流进行缓冲
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            //建立输入流连接
            InputStream in = socket.getInputStream();
            //首先建立输入流读取器，然后对输入流进行缓冲
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //对命令行中的单词发送给远程服务器，并且获得响应
            for (String arg: args) {
                define(arg, bw, br);
            }

            bw.write("quit\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
    }

    private static void define(String word, Writer out, BufferedReader in) 
             throws IOException, UnsupportedEncodingException {
                    //声明 in 的时候一定要使用BufferedReader 不能使用Reader
                 out.write("DEFINE eng-lat" + word + "\r\n");
                 out.flush();//刷新输出，确保数据通过网络发送

                 //开始读取响应
                 for (String line= in.readLine(); line != null; line = in.readLine()) {//此处出错了
                     if (line.startsWith("250")) {
                         return;
                     } else if (line.startsWith("552")) {
                         System.out.println("No defination for" + word);
                         return;
                     } else if(line.matches("\\d\\d\\d")) {
                         continue;
                     } else if(line.trim().equals(".")) {
                         continue;
                     } else {
                         System.out.println(line);
                     }
                 }

    }
 }