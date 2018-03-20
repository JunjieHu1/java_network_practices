import java.io.*;
import java.net.*;

import org.xml.sax.InputSource;

public class Reverse {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage:java Reverse" 
            +"http://location of your servlet"
            +"String_to_Reverse");
            System.exit(1);
        }

        String stringToReverse = URLEncoder.encode(args[1], "UTF-8");
        
        //创建URL对象
        URL url = new URL(args[0]);
        //获得URLconnection 对象
        URLConnection uc = url.openConnection();
        //允许此连接具有输出功能
        uc.setDoOutput(true);
        //准备向此连接中写入字符串
        OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
        //正式向输出流中写入字符串（字符串会转换为字节进行传输
        out.write("String=" + stringToReverse);
        //关闭输出流
        out.close();

        //从服务器的输出流中读取响应
        //对字符串响应进行缓冲
        BufferedReader bin = new BufferedReader(new InpurtStreamReader(uc.getInputStream()));
        //对输出流中的字符串进行解码，并显示在控制台上
        String decodingString;
        while((decodingString = bin.readLine()) != null) {
            System.out.println(decodingString);
        }
    }
}