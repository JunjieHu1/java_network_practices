import java.io.*;
import java.net.*;
import java.util.*;

//打印响应头部信息
public class LastModified {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                URL url = new URL(args[i]);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("HEAD");
                //打印全部信息
                for (int j = 1; ; j++) {
                    String key = http.getHeaderFieldKey(j);
                    String value = http.getHeaderField(j);
                    System.out.println(key + ":" + value);
                    if (key == null || value == null) {
                        break;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();    
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}