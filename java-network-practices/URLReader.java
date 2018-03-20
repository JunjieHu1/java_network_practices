import java.io.*;
import java.net.*;

public class URLReader {

    public static void main(String[] args) throws MalformedURLException {

        URL url = new URL("http://www.qq.com");
        try(BufferedReader in = new BufferedReader(
            new InputStreamReader(url.openStream()))) {
            
            
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}