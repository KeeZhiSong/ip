package sigmawolf.task;

/**
 * Represents a simple todo task without any date/time.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo task with the given description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
