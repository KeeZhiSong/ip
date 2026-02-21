package sigmawolf.task;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a task with a description and completion status.
 * This is the base class for different types of tasks.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;
    protected Set<String> tags;

    /**
     * Creates a new task with the given description and type.
     *
     * @param description The description of the task.
     * @param type The type of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.tags = new HashSet<>();
    }

    /**
     * Returns the status icon representing whether the task is done.
     *
     * @return "X" if task is done, " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the type icon of this task.
     *
     * @return The single-character type icon.
     */
    public String getTypeIcon() {
        return type.getIcon();
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return true if task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Adds a tag to this task.
     *
     * @param tag The tag to add (without #).
     */
    public void addTag(String tag) {
        tags.add(tag.toLowerCase());
    }

    /**
     * Removes a tag from this task.
     *
     * @param tag The tag to remove (without #).
     * @return true if tag was removed, false if tag didn't exist.
     */
    public boolean removeTag(String tag) {
        return tags.remove(tag.toLowerCase());
    }

    /**
     * Returns all tags associated with this task.
     *
     * @return Set of tags.
     */
    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    /**
     * Returns a formatted string of all tags.
     *
     * @return String like "#fun #urgent" or empty string if no tags.
     */
    public String getTagsString() {
        if (tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .sorted()
                .map(tag -> "#" + tag)
                .collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        String tagString = getTagsString();
        String baseString = "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
        return tagString.isEmpty() ? baseString : baseString + " " + tagString;
    }
}
