import java.util.Scanner;

public class SigmaWolf {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        String[] tasks = new String[100];
        int taskCount = 0;
        
        System.out.println(line);
        System.out.println(" Greetings. I'm SigmaWolf, leader of the pack.");
        System.out.println(" State your business.");
        System.out.println(line);
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        while (!input.equals("bye")) {
            System.out.println(line);
            
            if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println(" added: " + input);
            }
            
            System.out.println(line);
            input = scanner.nextLine();
        }
        
        System.out.println(line);
        System.out.println(" Understood. The pack dismisses you. Run along now.");
        System.out.println(line);
    }
}
