import java.io.*;
import java.net.*;

public class URLSplitter {

    public static void main(String[] args) {

        for(int i = 0; i < args.length; i++) {
            try {
                URL url = new URL(args[i]);
                //获取整个url
                System.out.println("the url is:"+url);
                //获取协议
                System.out.println("the scheme is:"+url.getProtocol());
                //获取权威机构
                System.out.println("the authority is:"+url.getAuthority());
                //获取用户信息
                System.out.println("thet usr info is:"+url.getUserInfo());
                //解析主机名
                String host = url.getHost();
                if (host != null) {
                    int index = host.indexOf('@');
                    if (index != -1) {
                        host = host.substring(index+1);
                        System.out.println("the host is:"+host);
                    }
                } else {
                    System.out.println("the host is null!!!");
                }

                //获得端口号
                System.out.println("the port is:"+url.getPort());
                //获得路径
                System.out.println("the path is:"+url.getPath());
                //获得查询字符串
                System.out.println("the query string is:"+url.getQuery());
                //获取片段标识符
                System.out.println("the fragment ref is:"+url.getRef());
            } catch (MalformedURLException e) {
                System.err.println("I can't handle this url"+args[i]);
                e.printStackTrace();
            }
            System.out.println(); 
        }
    }
}