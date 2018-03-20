
import java.net.*;

public class SameAddress{
    /**
     * 判断两个不同的主机名是否有相同的IP地址
     * 
     */

     public static void main(String[] args){
         
        try{
            InetAddress address1 = InetAddress.getByName(args[0]);
            InetAddress address2 = InetAddress.getByName(args[1]);
            System.out.println(address1);
            System.out.println(address2);

            if(address1.equals(address2)){
                System.out.println("www.alibaa.com has the same ip address :"+address1+"www.taobao.com");
            } else{
                System.out.println("they don't have the same ip address!!!!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
     }
}