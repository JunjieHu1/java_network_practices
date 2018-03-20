import java.io.IOException;
import java.net.*;

/**
 * 创建一个代理服务器，
 * 将客户端与代理服务器连接
 * 将代理服务器与远程服务器连接
 * 
 * @version 0.1.1
 * @author Junjie Hu
 * 
 */

 public class ProxyConnector {

    private SocketAddress proxyAddress = null;
    private Proxy proxy = null;

    ProxyConnector(String host, int port) {
        proxyAddress = new InetSocketAddress(host, port);
        proxy = new Proxy(Proxy.Type.SOCKS, proxyAddress);

    }

    public void doConnect(Proxy proxy, String host, int port) throws IOException {
        Socket s = new Socket(proxy);
        SocketAddress remote = new InetSocketAddress(host, port);
        s.connect(remote);
    }
 }