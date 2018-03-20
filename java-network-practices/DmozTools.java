import java.io.*;
import java.net.*;

public class DmozTools {

    public static void main(String[] args) {

        String target = "";
        for(int i = 0; i< args.length; i++) {
            target += args[i] + " ";
        }
        target = target.trim();
        System.out.println(target);

        String query = "";
        query = query + target;
        System.out.println(query);

        try{
            URL url = new URL("http://dmoztools.net/"+query);
            System.out.println(url);
            try(InputStream in = new BufferedInputStream(url.openStream())) {
                InputStreamReader rin = new InputStreamReader(in);
                int c;
                while((c = rin.read()) != -1) {
                    System.out.print((char) c);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}