import java.io.*;
import java.net.*;

public class WebLog {
    
    public static void main(String[] args) {
        //从网站站点与本地机器之间打开一个输入流
        try(FileInputStream fin = new FileInputStream(args[0]);
            Reader in = new InputStreamReader(fin);
            BufferedReader bin = new BufferedReader(in);) {
                //对缓冲区中的字符串进行解析
                for(String entry = bin.readLine();
                    entry != null;
                    bin.readLine()) {
                        //分解Ip地址
                        int index = entry.indexOf(' ');
                        String ip = entry.substring(0, index);
                        String others = entry.substring(index);

                        try {
                            InetAddress address = InetAddress.getByName(ip);
                            System.out.println(address.getHostName()+others);
                        } catch(UnknownHostException e) {
                            e.printStackTrace();
                        }

                }
            } catch(IOException e) {
                e.printStackTrace();
            }
    }
}