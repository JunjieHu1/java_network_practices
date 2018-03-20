import java.net.*;

public class IpCharacteristics {

    public static void main(String[] args) {

        try {
            InetAddress address = InetAddress.getByName(args[0]);

            if (address.isAnyLocalAddress()) {
                System.out.println(address + "is a local Address!");
            }

            if (address.isLoopbackAddress()) {
                System.out.println(address + "is a loopBackAddress");
            }

            if (address.isLinkLocalAddress()) {
                System.out.println(address + "is a LinkLocalAddress");
            }

            if (address.isSiteLocalAddress()) {
                System.out.println(address + "is a SiteLocalAddress");
            }

            if (address.isMulticastAddress()) {
                System.out.println(address + "is a MuticastAddress");
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());

        }
    }
}
