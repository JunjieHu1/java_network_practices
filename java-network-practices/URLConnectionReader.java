import java.io.*;
import java.net.*;

public class URLConnectionReader {

    public static void main(String[] args) throws MalformedURLException, IOException {

        for(int i = 0; i < args.length; i++) {
            URL url = new URL(args[i]);
            URLConnection uc = url.openConnection();

            try(InputStream in = uc.getInputStream()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    System.out.println(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //使用空行分割
        System.out.println();

    }
}