import java.net.*;
import java.util.Enumeration;

public class NetworkIn {
    //找到本机的所有网络接口（物理硬件：以太网卡或者虚拟接口）

    private static void networkInterfaceLister() {
        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            System.out.println(ni);
            }
        } catch(SocketException e) {
            e.printStackTrace();
        }
    }
    

/**
 * 一个网络接口可以绑定多个ip地址，但是不太常见，但是仍然是存在的
 */
    public static void main(String[] arga) {

        try{
            InetAddress localAddress = InetAddress.getByName("127.0.0.1");
            NetworkInterface ni = NetworkInterface.getByInetAddress(localAddress);
            if (ni == null){
                System.err.println("No such interface!!");
            } else {
                System.out.println("Hava found the networkInterface:"+ni);
            }
            //调用上面的类方法，将本机的所有网络接口列出来
            networkInterfaceLister();

        } catch(SocketException e) {
            System.err.print(e.getMessage());
        } catch(UnknownHostException e) {
            e.printStackTrace(); 
        }
    }
}