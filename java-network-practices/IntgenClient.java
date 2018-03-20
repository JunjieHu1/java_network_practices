import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

/**
 * 这个客户端采用了同步和阻塞模式
 * 
 */

 public class IntgenClient {

    public static int DEFAULT_PORT = 1919;

    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out.println("Usage: java IntgenClient host [port]");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[1]);
            System.out.println(port);
        } catch (RuntimeException e) {
            port = DEFAULT_PORT;
            System.out.println(port);
        }

        try {
            SocketAddress address = new InetSocketAddress(args[0], port);
            System.out.println(address);
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(address);
            System.out.println(client);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            //创建缓冲区视图
            IntBuffer view = buffer.asIntBuffer();

            for (int expected = 0; ; expected++) {
                //客户端通道必须和ByteBuffer配合使用（只能这样）
                client.read(buffer);
                int actual = view.get();
                System.out.println(actual);
                buffer.clear();
                view.rewind();

                //检测接受到的信息是否出错
                if (actual != expected) {
                    System.err.println("Expected" + expected + "was" + actual);
                    break;
                }
                System.out.println(actual);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }