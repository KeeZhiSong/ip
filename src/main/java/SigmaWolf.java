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
            
            try {
                if (input.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                } else if (input.startsWith("mark ")) {
                    if (input.trim().equals("mark")) {
                        throw new SigmaWolfException("The pack requires a task number to mark! Specify which task.");
                    }
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new SigmaWolfException("Invalid task number! The pack only has " + taskCount + " tasks.");
                    }
                    tasks[taskIndex].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskIndex]);
                } else if (input.startsWith("unmark ")) {
                    if (input.trim().equals("unmark")) {
                        throw new SigmaWolfException("The pack requires a task number to unmark! Specify which task.");
                    }
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new SigmaWolfException("Invalid task number! The pack only has " + taskCount + " tasks.");
                    }
                    tasks[taskIndex].markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskIndex]);
                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new SigmaWolfException("The pack cannot track an empty task! Tell me what needs to be done.");
                    }
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (input.equals("todo")) {
                    throw new SigmaWolfException("The pack cannot track an empty task! Tell me what needs to be done.");
                } else if (input.startsWith("deadline ")) {
                    String rest = input.substring(9);
                    if (rest.trim().isEmpty()) {
                        throw new SigmaWolfException("The pack needs to know what deadline to track! Provide details.");
                    }
                    if (!rest.contains("/by ")) {
                        throw new SigmaWolfException("Deadlines require a /by parameter! Format: deadline <task> /by <time>");
                    }
                    int byIndex = rest.indexOf("/by ");
                    String description = rest.substring(0, byIndex).trim();
                    String by = rest.substring(byIndex + 4).trim();
                    if (description.isEmpty()) {
                        throw new SigmaWolfException("The deadline description cannot be empty!");
                    }
                    if (by.isEmpty()) {
                        throw new SigmaWolfException("The deadline time cannot be empty!");
                    }
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (input.equals("deadline")) {
                    throw new SigmaWolfException("The pack needs to know what deadline to track! Provide details.");
                } else if (input.startsWith("event ")) {
                    String rest = input.substring(6);
                    if (rest.trim().isEmpty()) {
                        throw new SigmaWolfException("The pack needs to know what event to track! Provide details.");
                    }
                    if (!rest.contains("/from ") || !rest.contains("/to ")) {
                        throw new SigmaWolfException("Events require /from and /to parameters! Format: event <task> /from <start> /to <end>");
                    }
                    int fromIndex = rest.indexOf("/from ");
                    int toIndex = rest.indexOf("/to ");
                    String description = rest.substring(0, fromIndex).trim();
                    String from = rest.substring(fromIndex + 6, toIndex).trim();
                    String to = rest.substring(toIndex + 4).trim();
                    if (description.isEmpty()) {
                        throw new SigmaWolfException("The event description cannot be empty!");
                    }
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new SigmaWolfException("The event time cannot be empty!");
                    }
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (input.equals("event")) {
                    throw new SigmaWolfException("The pack needs to know what event to track! Provide details.");
                } else {
                    throw new SigmaWolfException("The pack doesn't understand that command. Speak clearly!");
                }
            } catch (SigmaWolfException e) {
                System.out.println(" GRRR!!! " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(" GRRR!!! That's not a valid number! Use numbers for task indices.");
            } catch (Exception e) {
                System.out.println(" GRRR!!! Something went wrong: " + e.getMessage());
            }
            
            System.out.println(line);
            input = scanner.nextLine();
        }
        
        System.out.println(line);
        System.out.println(" Understood. The pack dismisses you. Run along now. AWOOOOOOOOOOO!");
        System.out.println(line);
    }
}
