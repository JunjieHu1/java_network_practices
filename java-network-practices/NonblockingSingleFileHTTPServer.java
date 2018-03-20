import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

/**
 * 该服务器向客户端返回指定的文件
 */

 public class NonblockingSingleFileHTTPServer {

    private ByteBuffer contentBuffer;
    private int port = 80;

    //构造函数：建立与HTTP首部一起发送的数据
    public NonblockingSingleFileHTTPServer(
        ByteBuffer data, String encoding, String MIMEType, int port) {
            this.port = port;
            String header = "HTTP/1.1 200 OK\r\n"
                + "Server: NonblockingSingleFileHTTPServer\r\n"
                + "Content-length: " + data.limit() + "\r\n"
                + "Content-type: " + MIMEType + "\r\n";
            byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));

            //为这个文件创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(
                data.limit() + headerData.length);
            buffer.put(headerData);
            buffer.put(data);
            buffer.flip();
            this.contentBuffer = buffer;
        }

        public void run() throws IOException {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            Selector selector = Selector.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    try {
                        if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from the client " + client);
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            //不用解析HTTP首部，只需要读取
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(4096);
                            client.read(buffer);
                            //将通道设置为只写模式
                            key.interestOps(SelectionKey.OP_WRITE);
                            key.attach(contentBuffer.duplicate());
                        } else if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            if (buffer.hasRemaining()) {
                                client.write(buffer);
                            } else {
                                client.close();
                            }
                        }
                    } catch (IOException e) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException exx ) {}
                    }
                }
            }
        }


        public static void main(String[] args) {

            if (args.length == 0) {
                System.out.println("Usage: java NonblockingSingleFileHTTPServer");
                return;
            }

            try {
                //读取将要提供的文件
                String contenType = URLConnection.getFileNameMap().getContentTypeFor(args[0]);
                Path file = FileSystems.getDefault().getPath(args[0]);
                byte[] data = Files.readAllBytes(file);
                ByteBuffer input = ByteBuffer.wrap(data);
                

                //设置要监听的端口
                int port;
                try {
                    port = Integer.parseInt(args[1]);
                    if (port < 1 || port > 65535) {
                        port = 80;
                    }
                } catch (RuntimeException e) {
                    port = 80;
                }

                //设置文件传输时所用的编码
                String encoding = "utf-8";
                if (args.length > 2) encoding = args[2];

                //创建服务器执行对象
                NonblockingSingleFileHTTPServer server = 
                    new NonblockingSingleFileHTTPServer(input, encoding, contenType, port);
                //开始执行
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
 }