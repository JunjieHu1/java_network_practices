import java.io.*;
import java.net.*;

public class FormPoster {

    private URL url;
    private QueryString query = new QueryString();

    public FormPoster(URL url) {
        if (!url.getProtocol().toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("Posting only works for http links"); 
        }
        this.url = url;
    }

    public void add(String name, String value) {
        query.add(name, value);
    }

    public URL getURL() {
        return this.url;
    }

    public InputStream post() throws IOException {
        //打开链接准备POST
        URLConnection uc = url.openConnection();
        //配置连接，使其具有输出能力
        uc.setDoOutput(true);

        //获取输出流，自动关闭
        try (OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), "UTF-8")) {
            //http头部由URLConnection发送，我们只用发送数据
            out.write(query.toString());
            out.write("\r\n");
            out.flush();
        }

        //返回响应
        return uc.getInputStream();
    }

    public static void main(String[] args) {

        URL url;

        if (args.length > 0 ) {
            try {
                url = new URL(args[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                url = new URL("http://www.example.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        //创建FormPoster对象,收集表单信息
        FormPoster poster = new FormPoster(url);
        poster.add("name", "JunjieHu");
        poster.add("email", "2223123123@qq.com");

        //提交表单并读取响应
        try (InputStream in = poster.post()) {

            //读取响应
            Reader r = new InputStreamReader(in);
            int c = 0;
            while((c = r.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}