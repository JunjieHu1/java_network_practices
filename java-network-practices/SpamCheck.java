import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SpamCheck {
    /**
     * 判断一个网站是否是垃圾邮件发送者！！！
     */
    public static final String BLACKHOLE = "sbl.spamhaus.org";

    public static void main(String[] args) throws UnknownHostException {
        for(String arg: args) {
            if (isSpammer(arg)){
                System.out.println(arg+"is a spammer");
            } else {
                System.out.print(arg+"is not a spammer!!!");
            }
        }
    }

    private static boolean isSpammer(String arg) {
        try{
            InetAddress address = InetAddress.getByName(arg);
            byte[] addr = address.getAddress();
            String query = BLACKHOLE;
            for (byte add1: addr) {
                int unsingnedByte = add1 < 0 ? add1 + 256 : add1;
                query = unsingnedByte + query;
            }
            InetAddress.getByName(query);
            return true;
        } catch(UnknownHostException e) {
            return false;
        }
    }
}