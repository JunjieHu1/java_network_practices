import java.io.Console;
import java.util.Scanner;

/**
* 演示客户端输入
* Scanner类不适用于从控制台读入密码，因为输入是可见的
* @author Junjie Hu
*/
public class InputTest {

    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);

        //get first input
        System.out.println("What's your name?");
        String name = in.nextLine();

        //get second input 
        System.out.println("What's your age?");
        int age = in.nextInt();

        //get third input 
        System.out.println("What's your weight?");
        double weight = in.nextDouble();

        //display all the inputs
        System.out.println("Hello " + name + " your age is " + age + " your weight is " + weight);

        System.out.println("************************************************************");

        PasswordReader passwordReader = new PasswordReader();
        String userName = passwordReader.getName();
        String password = passwordReader.getPassword();
        //display the username and password
        System.out.println("The user's name is " + userName + " and his/her's password is " + password);
    }
}


/**
 * 使用Console类可以实现从控制台读取密码
 * 这时读取密码是不可见的
 */

class PasswordReader {
    
    Console cons = System.console();

    String getName() {
        String name = cons.readLine("Name: ");
        return name;
    }
    
    String getPassword() {
        char[] password = cons.readPassword("Password: ");
        return password.toString();
    }

    
}