import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Logger;

public class Redirector {

    //设置实力变量
    private static final Logger logger = Logger.getLogger("Redirector");

    private final int port;
    private final String newSite;

    //定义构造函数
    public Redirector(String newSite, int port) {
        this.port = port;
        this.newSite = newSite;
    }

    //定义执行重定向操作的方法
    public void start() {

        //打开服务器欢迎套接字
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("Redirecting connections on port " + server.getLocalPort() + " to " + newSite);

            while (true) {
                try {
                    Socket s = server.accept();
                    Thread t = new RedirectThread(s);
                    t.start();
                } catch (IOException ex) {
                    logger.warning("Exception accepting connection");
                } catch (RuntimeException ex) {
                    logger.log(Level.SEVERE, "Unexpected error!", ex);
                }
            }
        } catch (BindException ex) {
            logger.log(Level.SEVERE, "Could not start server!", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error opening server socket!", ex);
        }
    }

    //声明真正执行重定向的线程
    private class RedirectThread extends Thread {
        
        private final Socket connection;

        RedirectThread(Socket connection) {
            this.connection = connection;
        }

        //这个方法真正执行重定向
        public void run() {
            
            try {
                Writer out = new BufferedWriter(
                             new OutputStreamWriter(
                                 connection.getOutputStream(), "UTF-8"
                             )
                             );

                Reader in = new BufferedReader(
                            new InputStreamReader(
                                connection.getInputStream()
                            )
                            );

                //只是读取请求的第一行
                StringBuilder request = new StringBuilder(80);
                while (true) {
                    int c = in.read();
                    if (c == -1 || c == '\r' || c == '\n') break;
                    request.append((char) c);
                }

                String get = request.toString();
                String[] pieces =get.split("\\w*");
                System.out.println(pieces);
                String theFile = pieces[1];

                //如果是HTTP/1.0或以后版本，则发送一个MIME首部
                if (get.indexOf("HTTP") != -1) {
                    out.write("HTTP/1.0 302 FOUND\r\n");
                    Date now = new Date();
                    out.write("Date: " + now + "\r\n");
                    out.write("Server: Redirector 1.0" + "\r\n");
                    out.write("Location: " + newSite + theFile + "\r\n");
                    out.write("Content-type: text/html\r\n\r\n");
                    out.flush();
                }

                out.write("<HTML><HEAD><TITLE>Document moved<TITLE><HEAD>\r\n");
                out.write("<BODY><H1>Document moved<H1><BODY>\r\n");
                out.write("The document " + theFile + "\">" + newSite + theFile
                                          + "</A>.\r\n Please update your bookmark<P.");
                out.write("</BODY></HTML>\r\n");
                out.flush();
                logger.log(Level.INFO, "Redirected " + connection.getRemoteSocketAddress());
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
            }finally {
                try {
                    connection.close();
                } catch (IOException ex) { }
            }
        }
    }

    public static void main(String[] args) {

        int thePort;
        String theSite;

        //设置HTTP服务器监听端口
        try {
            thePort = Integer.parseInt(args[0]);
        } catch (RuntimeException e) {
            thePort = 80;
        }

        //设置重定向网站
        try {
            theSite = args[1];
            //删除网站最后的斜线
            if (theSite.endsWith("/")) {
                theSite = theSite.substring(0, theSite.length() - 1);
            }
        } catch (RuntimeException e) {
            System.out.println("Usage: java Redirector port site");
            return;
        }

        //创建重定向器对象
        Redirector redirector = new Redirector(theSite, thePort);
        //开始执行这个重定向器
        redirector.start();
    }


}