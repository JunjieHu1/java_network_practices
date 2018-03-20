import java.net.InetAddress;
import java.net.UnknownHostException;

public class FindLocalAddress {

    public static void main(String[] args){
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println(localAddress);
            String hostNameAndIp = localAddress.getHostName();
            System.out.println(hostNameAndIp);
            String hostNameAndIp2 = localAddress.getCanonicalHostName();
            System.out.println(hostNameAndIp2);
            String hostName = localAddress.getHostAddress();
            System.out.println(hostName);
            byte[] ip = localAddress.getAddress(); 
            System.out.println(ip);
            System.out.println("")
        }
        catch(UnknownHostException e){
            System.out.println(e.getMessage());
        }
    }
}