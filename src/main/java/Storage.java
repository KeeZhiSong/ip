import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws SigmaWolfException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        
        // Create directory if it doesn't exist
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        
        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return tasks;
        }
        
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new SigmaWolfException("The pack couldn't read from the den! Error: " + e.getMessage());
        }
        
        return tasks;
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
            switch (type) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length < 4) return null;
                    task = new Deadline(description, parts[3]);
                    break;
                case "E":
                    if (parts.length < 5) return null;
                    task = new Event(description, parts[3], parts[4]);
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

    public void save(ArrayList<Task> tasks) throws SigmaWolfException {
        try {
            File file = new File(filePath);
            
            // Create directory if it doesn't exist
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            
            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(taskToString(task) + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new SigmaWolfException("The pack couldn't save to the den! Error: " + e.getMessage());
        }
    }

    private String taskToString(Task task) {
        String isDone = task.isDone ? "1" : "0";
        String type = task.getTypeIcon();
        String description = task.getDescription();
        
        if (task instanceof Todo) {
            return String.format("T | %s | %s", isDone, description);
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", isDone, description, deadline.by);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", isDone, description, event.from, event.to);
        }
        
        return "";
    }
}
