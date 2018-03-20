import java.net.*;
import java.io.*;

public class AlibabaByName 
{
    
    public static void main(String[] args)
    {
        try{
            InetAddress[] addresses = InetAddress.getAllByName("wwww.ailibaba.com");
            for (InetAddress address: addresses){
                System.out.println(address);
            }
        }
        catch(UnknownHostException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}

