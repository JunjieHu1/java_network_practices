import java.io.*;
import java.net.*;

public class SourceViewer {
    
    public static void main(String[] args) {
       try{
        if(args.length > 0 ) {
            URL url = new URL(args[0]);
            try(InputStream in = url.openStream();
            BufferedInputStream bin = new BufferedInputStream(in);) {
                //缓冲，从而提高性能
                
                Reader cin = new InputStreamReader(bin);
                int c;
                while((c = cin.read()) != -1) {
                    System.out.print((char) c);
                }
            } catch(MalformedURLException e) {
                e.printStackTrace();
            }
        }
    } catch(IOException e) {
        e.printStackTrace();
    }
    }
}