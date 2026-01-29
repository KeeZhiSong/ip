package sigmawolf.parser;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Todo;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    
    public static String getCommand(String input) {
        String[] parts = input.split(" ", 2);
        return parts[0];
    }

    public static String getArguments(String input) {
        String[] parts = input.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public static int parseTaskNumber(String input) throws SigmaWolfException {
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SigmaWolfException("The pack requires a task number! Specify which task.");
            }
            return Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            throw new SigmaWolfException("That's not a valid number! Use numbers for task indices.");
        }
    }

    public static Todo parseTodo(String arguments) throws SigmaWolfException {
        String description = arguments.trim();
        if (description.isEmpty()) {
            throw new SigmaWolfException("The pack cannot track an empty task! Tell me what needs to be done.");
        }
        return new Todo(description);
    }

    public static Deadline parseDeadline(String arguments) throws SigmaWolfException {
        if (arguments.trim().isEmpty()) {
            throw new SigmaWolfException("The pack needs to know what deadline to track! Provide details.");
        }
        if (!arguments.contains("/by ")) {
            throw new SigmaWolfException("Deadlines require a /by parameter! Format: deadline <task> /by <yyyy-MM-dd HHmm>");
        }
        
        int byIndex = arguments.indexOf("/by ");
        String description = arguments.substring(0, byIndex).trim();
        String byString = arguments.substring(byIndex + 4).trim();
        
        if (description.isEmpty()) {
            throw new SigmaWolfException("The deadline description cannot be empty!");
        }
        if (byString.isEmpty()) {
            throw new SigmaWolfException("The deadline time cannot be empty!");
        }
        
        try {
            LocalDateTime by = LocalDateTime.parse(byString, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new SigmaWolfException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
        }
    }

    public static Event parseEvent(String arguments) throws SigmaWolfException {
        if (arguments.trim().isEmpty()) {
            throw new SigmaWolfException("The pack needs to know what event to track! Provide details.");
        }
        if (!arguments.contains("/from ") || !arguments.contains("/to ")) {
            throw new SigmaWolfException("Events require /from and /to parameters! Format: event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        
        int fromIndex = arguments.indexOf("/from ");
        int toIndex = arguments.indexOf("/to ");
        String description = arguments.substring(0, fromIndex).trim();
        String fromString = arguments.substring(fromIndex + 6, toIndex).trim();
        String toString = arguments.substring(toIndex + 4).trim();
        
        if (description.isEmpty()) {
            throw new SigmaWolfException("The event description cannot be empty!");
        }
        if (fromString.isEmpty() || toString.isEmpty()) {
            throw new SigmaWolfException("The event time cannot be empty!");
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            LocalDateTime from = LocalDateTime.parse(fromString, formatter);
            LocalDateTime to = LocalDateTime.parse(toString, formatter);
            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            throw new SigmaWolfException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
        }
    }

    public static String parseFind(String arguments) throws SigmaWolfException {
        String keyword = arguments.trim();
        if (keyword.isEmpty()) {
            throw new SigmaWolfException("The pack needs a keyword to search for! Provide a search term.");
        }
        return keyword;
    }
}
