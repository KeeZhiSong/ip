import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Greetings. I'm SigmaWolf, leader of the pack.");
        System.out.println(" State your business.");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(" GRRR!!! " + message);
    }

    public void showLoadingError() {
        showError("The pack couldn't read from the den!");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showTaskAdded(String task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskMarked(String task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    public void showTaskUnmarked(String task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    public void showTaskDeleted(String task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    public void showGoodbye() {
        System.out.println(" Understood. The pack dismisses you. Run along now. AWOOOOOOOOOOO!");
    }
}
