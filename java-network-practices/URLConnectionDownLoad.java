import java.io.*;
import java.net.*;

public class URLConnectionDownLoad {
    
    public static void main(String[] args) {
        
        try {
            URL url = new URL(args[0]);
            URLConnection uc = url.openConnection();
            try(
            InputStream in = uc.getInputStream();
            BufferedInputStream bin = new BufferedInputStream(in);
            Reader cin = new InputStreamReader(bin);) {
                int c = 0;
                while(( c = cin.read()) != -1) {
                    System.out.print((char)c);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}