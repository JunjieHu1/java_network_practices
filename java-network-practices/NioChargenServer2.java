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

public class NioChargenServer2 {

    public static int DEFAULT_PORT = 19;

    public static void main(String[] args) {

        //设置将要绑定的端口
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (RuntimeException e) {
            port = DEFAULT_PORT;
        }
        System.out.println("Listenning for connection on port of "+ port);

        byte[] rotation = new byte[95*2];
        for (byte i = ' ';i <= '~'; i++) {
            rotation[i - ' '] = i;
            rotation[i + 95 - ' '] = i;
        }

        //创建ServerSocketChannel对象
        ServerSocketChannel serverChannel;
        Selector selector;

        try {
            //只是创建ServerSocketChannel对象
            serverChannel = ServerSocketChannel.open();
            //创建服务器端socket: ServerSocket
            ServerSocket ss = serverChannel.socket();
            //创建将要绑定的网络端口
            InetSocketAddress address = new InetSocketAddress(port);
            //绑定端口，服务器现在在19端口监听连接
            ss.bind(address);
            //将通道设置为非阻塞
            serverChannel.configureBlocking(false);
            //调用静态工厂方法创建选择器
            selector = Selector.open();
            //将服务器套接字通道注册到选择器，并设定通道注册的操作类型（选择器将会只关注通道注册的这些类型操作）
            //对于服务器通道，唯一关心的操作就是：服务器通道是否准备好接受一个新连接
            //register方法会返回一个Selectionkey对象，这个对象就相当于这个通道的指针，这个对象还可以保存一个附件，
            //这个附件一般保存这个通道的连接状态
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //循环检查是否又可以操作的数据（通道是否处于就绪状态）
        //select()方法是阻塞的，只有当一个注册的通道就绪时，这个的方法才会返回
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                //中断这个程序
                break;
            }

            //当知道有通道已经就绪时，就可以获取就绪通道的键组成的集合
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            //然后遍历处理这个集合中的键
            //获取这集合的迭代器
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            //开始循环遍历处理这个集合中的键
            while (iterator.hasNext()) {
                
                //获取一个键
                SelectionKey key = iterator.next();
                //删除这个取出来的键，表示这个就绪的通道已经处理过了
                iterator.remove();

                //判断这个就绪通道的键可以进行哪些操作
                try {
                    //如果这个键的通道可以接收连接(是服务器通道：服务器通道只准备接受，客户端通道只准备写入)
                    //处理请求连接
                    if (key.isAcceptable()) {
                        //获取一个这个可以接受连接的服务器通道
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        //将服务器通道设置为非阻塞,之后accept()方法会立即返回
                        //server.configureBlocking(false);
                        //监听入站连接在之前绑定端口的时候已经开始
                        //接受一个连接，返回一个连接到远程客户端的SocketChannel对象
                        //通过ServerSocketChannel的accept创建SocketChannel实例  
                        //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                        SocketChannel connection = server.accept();
                        //将客户端通道连接设置为非阻塞， 从而允许服务器处理多个并发连接
                        connection.configureBlocking(false);
                        //将这个客户端通道向选择器注册，只关注写出功能,因为客户端将向服务器缓冲区写入数据
                        SelectionKey key2 = connection.register(selector, SelectionKey.OP_WRITE);
                        //创建缓冲区，为了存储客户端发送来的数据
                        ByteBuffer buffer = ByteBuffer.allocate(74);
                        //向服务器缓冲区中写入数据
                        buffer.put(rotation, 0, 72);
                        buffer.put((byte) '\r');
                        buffer.put((byte) '\n');
                        //将当前位置设置为0，将limit设置为原来的当前位置
                        buffer.flip();
                        //每个SelectionKey都有一个Object类型的附件，它通常用来保存通道的连接状态
                        //在这里，将通道将要写入网络的缓冲区保存在这个附件中
                        key2.attach(buffer);
                    } else if (key.isWritable()) {
                        //如果这个通道可以写入数据，（服务器输出响应）
                        //返回客户端通道
                        SocketChannel client = (SocketChannel) key.channel();
                        /*
                        向通道写入数据：
                        1.获取通道的附件
                        2.将它转换为ByteBuffer
                        3.调查缓冲区是否还有剩余未写的数据，如果有，就写入，否则重新填充缓冲区
                         */

                         ByteBuffer buffer = (ByteBuffer) key.attachment();
                         if (!buffer.hasRemaining()) {
                             //用下一行重新填充缓冲区
                             buffer.rewind();
                             //得到上一次的首字符
                             int first = buffer.get();
                             //转呗改变缓冲区中的数据
                             buffer.rewind();
                             //寻找rotation中新的首字符位置
                            int position = first - ' ' + 1;
                            //将数据从数组复制到缓冲区中
                            buffer.put(rotation, position, 72);
                            buffer.put((byte) '\r');
                            buffer.put((byte) '\n');
                            //准备缓冲区，等待再次写入
                            buffer.flip();
                         }
                         client.write(buffer);
                    } 
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {}
                }
            }

        }
    }
}