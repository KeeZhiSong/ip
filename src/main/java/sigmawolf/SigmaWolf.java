package sigmawolf;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.parser.Parser;
import sigmawolf.storage.Storage;
import sigmawolf.task.Task;
import sigmawolf.task.TaskList;
import sigmawolf.task.Todo;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.ui.Ui;

/**
 * SigmaWolf is a task management chatbot with an alpha wolf personality.
 * It helps users track todos, deadlines, and events.
 */
public class SigmaWolf {
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

    public static void main(String[] args) {
        new SigmaWolf("./data/sigmawolf.txt").run();
    }
}
