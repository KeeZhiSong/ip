import java.util.Scanner;

public class SigmaWolf {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println(" Hello! I'm SigmaWolf");
        System.out.println(" What can I do for you?");
        System.out.println(line);
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        while (!input.equals("bye")) {
            System.out.println(line);
            System.out.println(" " + input);
            System.out.println(line);
            input = scanner.nextLine();
        }
        
        System.out.println(line);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
