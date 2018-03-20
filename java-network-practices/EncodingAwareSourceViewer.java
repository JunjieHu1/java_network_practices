import java.io.*;
import java.net.*;
import java.nio.charset.UnsupportedCharsetException;

public class EncodingAwareSourceViewer {

    public static void main(String[] args) {

        for(int i = 0; i < args.length; i++) {
            try{
                //设置默认字符编码格式
                String encoding = "UTF-8";

                //创建URL对象
                URL url = new URL(args[i]);
                //获取URLConnection对象
                URLConnection uc = url.openConnection();
                //在打开连接之前可以对连接进行自己想要的配置
                //
                //正式打开连接，获取响应的内容类型
                String contentType = uc.getContentType();
                //从内容类型中将内容的具体编码格式解析出来
                int encodingStart = contentType.indexOf("charset=");
                if (encodingStart != -1) {
                    encoding = contentType.substring(encodingStart+8);
                }
                //缓冲输入流
                InputStream in = new BufferedInputStream(uc.getInputStream());
                //将缓冲输出流串联到输入流读取器
                Reader r = new InputStreamReader(in);
                //设置一个哨兵，检查输入流是否结束
                int c = 0;
                //使用正确的编码格式循环读取输入流，一次读取一个字节，直到结束
                while((c = r.read()) != -1) {
                    System.out.print((char) c);
                }
                //关闭输入流
                r.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        //打印一个空行，分割不同网页
        System.out.println();
    }
}