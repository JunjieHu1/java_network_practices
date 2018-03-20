import java.io.*;
import java.net.*;
import java.util.*;

public class HeaderViewer {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                URL url = new URL(args[0]);
                URLConnection uc = url.openConnection();
                //打印内容类型
                System.out.println("Content-type:" + uc.getContentType());
                //打印内容编码方式
                if (uc.getContentEncoding() != null) {
                    System.out.println("Content-Encoding:" + uc.getContentEncoding());
                }
                //打印日期
                if (uc.getDate() != -1) {
                    System.out.println("Date:" + uc.getDate());
                }
                //打印最后修改日期
                if (uc.getLastModified() != -1) {
                    System.out.println("Last-Modified:" + uc.getLastModified());
                }
                //打印连接有效时间
                if (uc.getExpiration() != -1) {
                    System.out.println("Expiration:" + uc.getExpiration());
                }
                //打印一个空行
                System.out.println();
                //一次性打印所有的头部信息
                showAllHeaders(uc);
                System.out.println();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showAllHeaders(URLConnection uc) {
        for (int i = 0;  ; i ++) {
            //一次循环获取一个首部字段的值
            String header = uc.getHeaderField(i);
            if (header == null) break;
            System.out.println(uc.getHeaderFieldKey(i) + ":" +header );
        }
    }
}