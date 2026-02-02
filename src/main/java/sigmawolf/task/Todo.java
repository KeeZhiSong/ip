package sigmawolf.task;

/**
 * Represents a simple todo task without any date/time.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
