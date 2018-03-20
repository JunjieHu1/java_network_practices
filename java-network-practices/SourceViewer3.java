import java.io.*;
import java.net.*;

//包括http头部和内容主体的SourceViewer
public class SourceViewer3 {

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            try {
                URL url = new URL(args[i]);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                //获取响应码
                int code = http.getResponseCode();
                //获取响应行信息
                String responseMessage = http.getResponseMessage();
                //输出响应行
                System.out.println("HTTP/1.X" + " " + code + " " + responseMessage);

                //打印所有的首部信息
                for (int j = 1; ; j++) {
                    String key = http.getHeaderFieldKey(j);
                    String value = http.getHeaderField(j);
                    if (key == null || value == null) {
                        break;
                    }
                    System.out.println(key + ":" + value);
                }

                //打印一个空行
                System.out.println();

                //获取输入流，读取正文
                try {
                    printFromStream(http.getInputStream());                 
                } catch (IOException e) {
                    printFromStream(http.getErrorStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printFromStream(InputStream raw) throws IOException {

        try(InputStream buffer = new BufferedInputStream(raw)) {
            Reader r = new InputStreamReader(buffer);
            int c = 0;
            while ((c = r.read()) != -1) {
                System.out.print((char) c);
            }
        }
    }

}