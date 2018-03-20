import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.awt.SecondaryLoop;
import java.io.*;

public class NioChargenServer {

    public static int DEFAULT_PORT = 19;

    public static void main(String[] args) {

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (RuntimeException e) {
            port = DEFAULT_PORT;
        }
        System.out.println("Listenning for connections on port " + port);

        byte[] rotation = new byte[95*2];
        for (byte i = ' ';i <= '~'; i++) {
            rotation[i - ' '] = i;
            rotation[i + 95 - ' '] = i;
        }

        //创建ServerSocketChannel对象
        ServerSocketChannel serverChannel;
        Selector selector;
        try {
            //打开服务器监听通道
            serverChannel = ServerSocketChannel.open();
            //创建ServerSocket对象
            ServerSocket ss = serverChannel.socket();
            //创建服务器将要绑定的端口地址
            InetSocketAddress address = new InetSocketAddress(port);
            //绑定端口 backlog设置为1024
            ss.bind(address, 1024);
            //将通道设置为非阻塞
            serverChannel.configureBlocking(false);
            //创建选择器（多路复用器）作用：选择就绪任务
            selector = Selector.open();
            //将服务器通道向选择器注册，开始监听客户端连接请求
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } 

        /**
         * Selector会不断轮询注册在其上的Channel，
         * 如果某个Channel上面发生读或者写事件
         * ，这个Channel就处于就绪状态，会被Selector轮询出来，
         * 然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作。
         * 
         * 一个Selector可以同时轮询多个Channel，
         * 因为JDK使用了epoll()代替传统的select实现，
         * 所以没有最大连接句柄1024/2048的限制。所以，只需要一个线程负责Selector的轮询，就可以接入成千上万的客户端。
         */
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            //获得一个持有SelectionKey的集合
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //从集合中删除这个键，告诉选择器这个键已经处理过
                //然后selector.select() 在这个通道再次就绪之前，就不会将这个通道的键返回给我们了
                iterator.remove();

                try {
                    //如果是请求消息，那么处理它
                    if (key.isAcceptable()) {
                        //key.channel() 返回创建key的通道
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        //通过ServerSocketChannel的accept创建SocketChannel实例  
                        //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                        SocketChannel client = ssc.accept();
                        System.out.println("Accepted connection from" + client);
                        //设置连接为非阻塞
                        client.configureBlocking(false);
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_READ);
                        //为客户端请求建立缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(74);
                        buffer.put(rotation, 0, 72);
                        buffer.put((byte) '\r');
                        buffer.put((byte) '\n');
                        //将当前位置设置为0，limit限制位置设置为原当前位置74
                        buffer.flip();
                        key2.attach(buffer);
                    } else if (key.isWritable()) {
                        //返回客户端通道，准备向客户端发送响应
                        SocketChannel client = (SocketChannel) key.channel();
                        //创建缓冲区
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        //判断缓冲区是否还有剩余
                        if (buffer.hasRemaining()) {
                            //只是将当前位置设置为0，为重新填充缓冲区做准备
                            buffer.rewind();
                            //得到上一次的首字符
                            int first = buffer.get();
                            //准备改变缓冲区中的数据
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
                        //将buffer中的数据写入输出连接
                        client.write(buffer);
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } 
    }
}