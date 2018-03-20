import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.*;



public class SingleFileHTTPServer {

    //初始化前的准备工作
    //创建日志记录器
    private static final Logger logger = Logger.getLogger("SingleFileHTTPServer");
    
    //声明端口
    private final int port;
    //声明HTTP首部
    private final byte[] header;
    //声明编码格式
    private final String encoding;
    //声明HTTP主体
    private final byte[] content;

    //创建构造函数
    public SingleFileHTTPServer(
        String data, String encoding, String mimeType, int port) throws UnsupportedEncodingException {
            this(data.getBytes(encoding), encoding,mimeType, port);
        } 
    

    //最基本的构造函数
    public SingleFileHTTPServer(
        byte[] data, String encoding, String mimeType, int port) {
            this.port = port;
            this.content = data;
            this.encoding = encoding;
            String header = "HTTP/1.1 200 OK\r\n"
                + "Server: SingleFileHTTPServer 1.0 \r\n"
                +"Content-type: " + mimeType + "; Charset：" + this.encoding + "\r\n"
                + "Content-length: " + this.content.length + "\r\n\r\n";
            this.header = header.getBytes(Charset.forName("UTF-8"));
        }

    //服务器启动操作将要执行的内容
    public void start() {
        //创建一个固定线程池
        ExecutorService pool = Executors.newFixedThreadPool(100);
        
        //在一个指定端口打开一个服务器欢迎套接字
        try (ServerSocket server = new ServerSocket(this.port)) {
            //使用日志记录器，将连接情况记录下来
            logger.info("Accepting connection on port: " + server.getLocalPort());
            logger.info("Date to be sent: ");
            logger.info(new String(this.content, encoding));

            //开始监听这个指定端口，一旦有请求连接出现，接受此请求，经过三次握手建立连接，然后返回一个服务套接字Socket
            while (true) {
                try {
                    Socket connection = server.accept();
                    pool.submit(new HTTPHandler(connection));
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Expecting accepting connection ", e);
                } catch (RuntimeException e) {
                    logger.log(Level.SEVERE, "Unexpected error ", e);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "could not start server ", ex);
        }
    }

    //内部类：此类的作用是提供一个处理Socket连接的方法
    private class HTTPHandler implements Callable<Void> {

        //为构造函数初始化实例准备所需要的东西
        private final Socket connection;

        //构造函数
        HTTPHandler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() throws IOException {

            try {
                //获取输出流，并缓冲
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                //获取输入流，并缓冲
                InputStream in = new BufferedInputStream(connection.getInputStream());

                //只读取请求的第一行，这就是我们想要的内容，其余暂时不需要
                StringBuilder request = new StringBuilder(80);
                while (true) {
                    int c = in.read();
                    if (c == -1 || c == '\r' || c == '\n') break;
                    request.append((char) c);
                }

                //如果是HTTP/1.0或者以后的版本，则发送一个MIME首部
                if (request.toString().indexOf("HTTP/") != -1) {
                    out.write(header);
                }

                out.write(content);
                out.flush();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error writing to client!", e);
            } finally {
                connection.close();
            }
            return null;
        }

    }

    public static void main(String[] args) {

        //设置将要监听的端口
        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port < 1 || port > 65535) port = 80;
        } catch (RuntimeException e) {
            port = 80;
        }

        //设置文件传输时的编码方式
        String encoding = "UTF-8";
        if (args.length > 2) encoding = args[2];

        //从文件系统中获得指定的文件
        try {
            Path path = Paths.get(args[1]);
            //将指定的文件全部读入一个字节数组中
            byte[] data = Files.readAllBytes(path);

            //获得指定文件的内容类型
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(args[1]);
            //创建服务器对象
            SingleFileHTTPServer server = new SingleFileHTTPServer(data, encoding, contentType, port);
            //启动服务器
            server.start();
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Usage: java SingleFileHTTPServer port filename encoding");
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }

    }
}