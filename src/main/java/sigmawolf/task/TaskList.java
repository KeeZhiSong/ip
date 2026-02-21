package sigmawolf.task;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Manages a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a new empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a new TaskList with existing tasks.
     *
     * @param tasks The list of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        assert task != null : "Task to add cannot be null";
        tasks.add(task);
    }

    /**
     * Removes a task at the specified index.
     *
     * @param index The index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        return tasks.remove(index);
    }

    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds: " + index;
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks that contain the given keyword in their description.
     *
     * @param keyword The keyword to search for.
     * @return A new TaskList containing matching tasks.
     */
    public TaskList findTasks(String keyword) {
        assert keyword != null : "Search keyword cannot be null";
        ArrayList<Task> matchingTasks = tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new TaskList(matchingTasks);
    }

    /**
     * Marks a task as done.
     *
     * @param index The zero-based index of the task to mark.
     */
    public void markTask(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks a task as not done.
     *
     * @param index The zero-based index of the task to unmark.
     */
    public void unmarkTask(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Deletes a task from the list.
     *
     * @param index The zero-based index of the task to delete.
     * @return The deleted task.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }
}
