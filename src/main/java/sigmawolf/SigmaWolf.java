package sigmawolf;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.parser.Parser;
import sigmawolf.storage.Storage;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.task.Task;
import sigmawolf.task.TaskList;
import sigmawolf.task.Todo;
import sigmawolf.ui.Ui;

/**
 * SigmaWolf is a task management chatbot with an alpha wolf personality.
 * It helps users track todos, deadlines, and events.
 */
public class SigmaWolf {
    private static final String ERROR_PREFIX = "GRRR!!! ";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a new SigmaWolf instance with the specified data file path.
     *
     * @param filePath The path to the data file for storing tasks.
     */
    public SigmaWolf(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (SigmaWolfException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main loop of the chatbot, processing user commands until exit.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String input = ui.readCommand();
                ui.showLine();
                String command = Parser.getCommand(input);
                String arguments = Parser.getArguments(input);

                switch (command) {
                case "bye":
                    isExit = true;
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                case "list":
                    ui.showTaskList(tasks);
                    ui.showLine();
                    break;
                case "mark":
                    handleMark(input);
                    ui.showLine();
                    break;
                case "unmark":
                    handleUnmark(input);
                    ui.showLine();
                    break;
                case "delete":
                    handleDelete(input);
                    ui.showLine();
                    break;
                case "todo":
                    handleTodo(arguments);
                    ui.showLine();
                    break;
                case "deadline":
                    handleDeadline(arguments);
                    ui.showLine();
                    break;
                case "event":
                    handleEvent(arguments);
                    ui.showLine();
                    break;
                case "find":
                    handleFind(arguments);
                    ui.showLine();
                    break;
                case "tag":
                    handleTag(arguments);
                    ui.showLine();
                    break;
                case "untag":
                    handleUntag(arguments);
                    ui.showLine();
                    break;
                default:
                    throw new SigmaWolfException("The pack doesn't understand that command. Speak clearly!");
                }
            } catch (SigmaWolfException e) {
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
    }

    private void handleMark(String input) throws SigmaWolfException {
        int index = Parser.parseTaskNumber(input);
        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }
        tasks.get(index).markAsDone();
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.get(index).toString());
    }

    private void handleUnmark(String input) throws SigmaWolfException {
        int index = Parser.parseTaskNumber(input);
        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }
        tasks.get(index).markAsNotDone();
        storage.save(tasks.getTasks());
        ui.showTaskUnmarked(tasks.get(index).toString());
    }

    private void handleDelete(String input) throws SigmaWolfException {
        int index = Parser.parseTaskNumber(input);
        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }
        Task removed = tasks.remove(index);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removed.toString(), tasks.size());
    }

    private void handleTodo(String arguments) throws SigmaWolfException {
        Todo todo = Parser.parseTodo(arguments);
        tasks.add(todo);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(todo.toString(), tasks.size());
    }

    private void handleDeadline(String arguments) throws SigmaWolfException {
        Deadline deadline = Parser.parseDeadline(arguments);
        tasks.add(deadline);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(deadline.toString(), tasks.size());
    }

    private void handleEvent(String arguments) throws SigmaWolfException {
        Event event = Parser.parseEvent(arguments);
        tasks.add(event);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(event.toString(), tasks.size());
    }

    private void handleFind(String arguments) throws SigmaWolfException {
        String keyword = Parser.parseFind(arguments);
        TaskList matchingTasks = tasks.findTasks(keyword);
        ui.showFindResults(matchingTasks);
    }

    private void handleTag(String arguments) throws SigmaWolfException {
        String[] parsed = Parser.parseTag(arguments);
        int index = Integer.parseInt(parsed[0]);
        String tag = parsed[1];

        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }

        tasks.get(index).addTag(tag);
        storage.save(tasks.getTasks());
        System.out.println("  Tagged task: " + tasks.get(index).toString());
    }

    private void handleUntag(String arguments) throws SigmaWolfException {
        String[] parsed = Parser.parseTag(arguments);
        int index = Integer.parseInt(parsed[0]);
        String tag = parsed[1];

        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }

        tasks.get(index).removeTag(tag);
        storage.save(tasks.getTasks());
        System.out.println("  Removed tag from task: " + tasks.get(index).toString());
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user's input command.
     * @return The chatbot's response.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommand(input);
            String arguments = Parser.getArguments(input);

            switch (command) {
            case "bye":
                return "Understood. The pack dismisses you. Run along now. AWOOOOOOOOOOO!";
            case "list":
                return getTaskListString();
            case "mark":
                return handleMarkForGui(arguments);
            case "unmark":
                return handleUnmarkForGui(arguments);
            case "delete":
                return handleDeleteForGui(arguments);
            case "todo":
                return handleTodoForGui(arguments);
            case "deadline":
                return handleDeadlineForGui(arguments);
            case "event":
                return handleEventForGui(arguments);
            case "find":
                return handleFindForGui(arguments);
            case "tag":
                return handleTagForGui(arguments);
            case "untag":
                return handleUntagForGui(arguments);
            default:
                throw new SigmaWolfException("The pack doesn't understand that command. Speak clearly!");
            }
        } catch (SigmaWolfException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String getTaskListString() {
        if (tasks.size() == 0) {
            return "Your task list is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String handleMarkForGui(String input) throws SigmaWolfException {
        int taskIndex = Parser.parseTaskIndex(input);
        Task task = tasks.get(taskIndex);
        tasks.markTask(taskIndex);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Nice! I've marked this task as done:\n  " + task;
    }

    private String handleUnmarkForGui(String input) throws SigmaWolfException {
        int taskIndex = Parser.parseTaskIndex(input);
        Task task = tasks.get(taskIndex);
        tasks.unmarkTask(taskIndex);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    private String handleDeleteForGui(String input) throws SigmaWolfException {
        int taskIndex = Parser.parseTaskIndex(input);
        Task task = tasks.deleteTask(taskIndex);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleTodoForGui(String arguments) throws SigmaWolfException {
        Todo todo = Parser.parseTodo(arguments);
        tasks.addTask(todo);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Got it. I've added this task:\n  " + todo
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadlineForGui(String arguments) throws SigmaWolfException {
        Deadline deadline = Parser.parseDeadline(arguments);
        tasks.addTask(deadline);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Got it. I've added this task:\n  " + deadline
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEventForGui(String arguments) throws SigmaWolfException {
        Event event = Parser.parseEvent(arguments);
        tasks.addTask(event);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Got it. I've added this task:\n  " + event
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFindForGui(String arguments) throws SigmaWolfException {
        String keyword = Parser.parseFind(arguments);
        TaskList matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.size() == 0) {
            return "No matching tasks found in your list.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append((i + 1)).append(".").append(matchingTasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String handleTagForGui(String arguments) throws SigmaWolfException {
        String[] parsed = Parser.parseTag(arguments);
        int index = Integer.parseInt(parsed[0]);
        String tag = parsed[1];

        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }

        tasks.get(index).addTag(tag);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Tagged task: " + tasks.get(index).toString();
    }

    private String handleUntagForGui(String arguments) throws SigmaWolfException {
        String[] parsed = Parser.parseTag(arguments);
        int index = Integer.parseInt(parsed[0]);
        String tag = parsed[1];

        if (index < 0 || index >= tasks.size()) {
            throw new SigmaWolfException("Invalid task number! The pack only has " + tasks.size() + " tasks.");
        }

        tasks.get(index).removeTag(tag);
        try {
            storage.save(tasks.getTasks());
        } catch (SigmaWolfException e) {
            // Ignore save errors in GUI
        }
        return "Removed tag from task: " + tasks.get(index).toString();
    }

    /**
     * Starts the SigmaWolf CLI chatbot.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new SigmaWolf("./data/sigmawolf.txt").run();
    }
}
