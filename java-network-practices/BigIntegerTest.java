import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Test the BigInteger
 * @author Junjie Hu
 */

 public class BigIntegerTest {

    public static void main(String[] args) {
        
        try {
            Scanner in = new Scanner(System.in);

            System.out.println("First number: ");
            int first = in.nextInt();
            BigInteger bigFirst = BigInteger.valueOf(first);

            System.out.println("Second number: ");
            int second = in.nextInt();
            BigInteger bigSecond = BigInteger.valueOf(second);
            
            System.out.println("Third number(double): ");
            BigDecimal bigDecimal = BigDecimal.valueOf(in.nextDouble());
            
            BigInteger integerSum = bigFirst.add(bigSecond);
            BigInteger multi = bigFirst.multiply(bigSecond);
           

            System.out.println(integerSum + "\r\n" + multi + "\r\n" + bigDecimal);


        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
 }