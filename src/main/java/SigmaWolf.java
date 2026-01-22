import java.util.Scanner;

public class SigmaWolf {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Task[] tasks = new Task[100];
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
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(" added: " + input);
            }
            
            System.out.println(line);
            input = scanner.nextLine();
        }
        
        System.out.println(line);
        System.out.println(" Understood. The pack dismisses you. Run along now. AWOOOOOOOOOOO!");
        System.out.println(line);
    }
}
