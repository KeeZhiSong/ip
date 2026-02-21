package sigmawolf.ui;

import java.util.Scanner;

import sigmawolf.task.TaskList;

/**
 * Handles user interface interactions.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a new Ui instance.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Greetings. I'm SigmaWolf, leader of the pack.");
        System.out.println(" State your business.");
        showLine();
    }

    /**
     * Shows a horizontal line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(" GRRR!!! " + message);
    }

    /**
     * Shows a loading error message.
     */
    public void showLoadingError() {
        showError("The pack couldn't read from the den!");
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's input command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows a message when a task is added.
     *
     * @param task The task that was added.
     * @param taskCount The total number of tasks.
     */
    public void showTaskAdded(String task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows a message when a task is marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(String task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Shows a message when a task is unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(String task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Shows a message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @param taskCount The total number of remaining tasks.
     */
    public void showTaskDeleted(String task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows the list of all tasks.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Shows the search results for matching tasks.
     *
     * @param matchingTasks The list of matching tasks.
     */
    public void showFindResults(TaskList matchingTasks) {
        if (matchingTasks.size() == 0) {
            System.out.println(" No matching tasks found in your list.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Shows the goodbye message.
     */
    public void showGoodbye() {
        System.out.println(" Understood. The pack dismisses you. Run along now. AWOOOOOOOOOOO!");
    }
}
