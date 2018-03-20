import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;



public class IntgenServer {

    public static int DEFAULT_PORT = 1919;

    public static void main(String[] args) {

        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (RuntimeException e) {
            port = DEFAULT_PORT;
        }
        System.out.println("Listenning for connection on port of " + port);

        //创建服务器通道
        ServerSocketChannel serverChannel;
        Selector selector;
        try {
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //选择器开始工作
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        Set<SelectionKey> readKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();

            try {
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    System.out.println("Accepted connection from " + client);
                    client.configureBlocking(false);
                    //获取这个客户端通道的键
                    SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
                    ByteBuffer output = ByteBuffer.allocate(4);
                    output.putInt(0);
                    output.flip();
                    key2.attach(output);
                } else if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer output = (ByteBuffer) key.attachment();
                    if (!output.hasRemaining()) {
                        output.rewind();
                        int value = output.getInt();
                        output.clear();
                        output.putInt(value + 1);
                        output.flip();
                    }
                    client.write(output);
                }
            } catch (IOException e) {
                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException ex) { }
            }
        }
    }
}