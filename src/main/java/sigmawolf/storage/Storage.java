package sigmawolf.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.task.Task;
import sigmawolf.task.Todo;

/**
 * Handles loading and saving tasks to the data file.
 */
public class Storage {
    private String filePath;

    /**
     * Creates a new Storage instance with the specified file path.
     *
     * @param filePath The path to the data file.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path cannot be null or empty";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return ArrayList of tasks loaded from the file.
     * @throws SigmaWolfException If there is an error reading the file.
     */
    public ArrayList<Task> load() throws SigmaWolfException {
        File file = new File(filePath);

        // Create directory if it doesn't exist
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return Files.lines(Paths.get(filePath))
                    .map(this::parseTask)
                    .filter(task -> task != null)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new SigmaWolfException("The pack couldn't read from the den! Error: " + e.getMessage());
        }
    }

    private Task parseTask(String line) throws SigmaWolfException {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                return null; // Corrupted line, skip it
            }

            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            Task task;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                LocalDateTime by = LocalDateTime.parse(parts[3], formatter);
                task = new Deadline(description, by);
                break;
            case "E":
                if (parts.length < 5) {
                    return null;
                }
                LocalDateTime from = LocalDateTime.parse(parts[3], formatter);
                LocalDateTime to = LocalDateTime.parse(parts[4], formatter);
                task = new Event(description, from, to);
                break;
            default:
                return null; // Unknown type, skip
            }

            if (isDone) {
                task.markAsDone();
            }

            return task;
        } catch (Exception e) {
            // Corrupted data, skip this line
            return null;
        }
    }

    /**
     * Saves the list of tasks to the file.
     *
     * @param tasks The list of tasks to save.
     * @throws SigmaWolfException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws SigmaWolfException {
        try {
            File file = new File(filePath);

            // Create directory if it doesn't exist
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            String content = tasks.stream()
                    .map(this::taskToString)
                    .collect(Collectors.joining(System.lineSeparator()));
            
            Files.write(Paths.get(filePath), (content + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            throw new SigmaWolfException("The pack couldn't save to the den! Error: " + e.getMessage());
        }
    }

    private String taskToString(Task task) {
        String isDone = task.isDone() ? "1" : "0";
        String type = task.getTypeIcon();
        String description = task.getDescription();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (task instanceof Todo) {
            return String.format("T | %s | %s", isDone, description);
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", isDone, description, deadline.getBy().format(formatter));
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", isDone, description,
                event.getFrom().format(formatter), event.getTo().format(formatter));
        }

        return "";
    }
}
