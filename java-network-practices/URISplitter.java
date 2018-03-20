import java.io.*;
import java.net.*;

public class URISplitter {
    
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            try{
                URI uri = new URI(args[i]);
                //将整个URI打印出来
                System.out.println("the uri is:"+uri);

                //判断这个URI是否是透明的（是否具有层次结构），如果透明则表示不具有层次结构
                if(uri.isOpaque()) {
                    //说明这个uri是透明的，只具有三部分，不在在进行细分
                    System.out.println("this uri is opaque!!!");
                    //获取模式
                    System.out.println("the scheme is:"+uri.getScheme());
                    //获取特定模式
                    System.out.println("the scheme specific part is:"+uri.getSchemeSpecificPart());
                    //获取片段
                    System.out.println("the fragment is:"+uri.getFragment());
                } else {
                    System.out.println("the uri is not opaque,it's hierarchical!!!");
                    //获取模式
                    System.out.println("the scheme is:"+uri.getScheme());
                    //下面获取特定模式的各个层次部分
                    //获取用户信息
                    System.out.println("the user info is:"+uri.getUserInfo());
                    //获取授权机构
                    System.out.println("the authority is:"+uri.getAuthority());
                    //获取端口号
                    System.out.println("the port is:"+uri.getPort());
                    //获取路径
                    System.out.println("the path is:"+uri.getPath());
                    //获取查询字符串
                    System.out.println("the query is:"+uri.getQuery());
                    //获取片段
                    System.out.println("the fargment is:"+uri.getFragment());
                }
            } catch (URISyntaxException e) {
                System.err.println(args[i]+"seems not a legal uri!!!");
                e.printStackTrace();
            } 
        }
    }
}