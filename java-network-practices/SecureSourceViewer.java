import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class SecureSourceViewer {

    public static void main(String[] args) {
        
        Authenticator.setDefault(new DialogAuthenticator);

        for (int i = 0; i < args.length; i++) {
            
            try{
                //打开连接开始读取数据
                URL url = new URL(args[i]);
                try(InputStream in = new BufferedInputStream(url.openStream())) {
                    //将InputStream 串联到 Reader
                    Reader r = new InputStreamReader(in);
                    int c = 0;
                    while((c = r.read()) != -1) {
                        System.out.println((char) c);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //打印一个空行
        System.out.println();
    }
}