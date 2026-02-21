package sigmawolf.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.task.Todo;

/**
 * Parses user input and creates task objects.
 */
public class Parser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final String DATE_FORMAT_ERROR =
            "Invalid date format or non-existent date! Use: yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)";

    /**
     * Extracts the command word from user input.
     *
     * @param input The user input string.
     * @return The command word, or empty string if input is blank.
     */
    public static String getCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String[] parts = input.trim().split("\\s+", 2);
        return parts[0];
    }

    /**
     * Extracts the arguments from user input.
     *
     * @param input The user input string.
     * @return The arguments after the command word.
     */
    public static String getArguments(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String[] parts = input.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    /**
     * Parses the task number from user input.
     *
     * @param input The user input containing a task number.
     * @return The zero-based task index.
     * @throws SigmaWolfException If the input does not contain a valid task number.
     */
    public static int parseTaskNumber(String input) throws SigmaWolfException {
        try {
            String[] parts = input.trim().split("\\s+", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new SigmaWolfException("The pack requires a task number! Specify which task.");
            }
            return Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            throw new SigmaWolfException("That's not a valid number! Use numbers for task indices.");
        }
    }

    /**
     * Validates that a description does not contain pipe characters.
     *
     * @param description The description to validate.
     * @throws SigmaWolfException If the description contains a pipe character.
     */
    private static void validateDescription(String description) throws SigmaWolfException {
        if (description.contains("|")) {
            throw new SigmaWolfException("Description cannot contain the '|' character!");
        }
    }

    /**
     * Parses a todo task from the given arguments.
     *
     * @param arguments The arguments containing the task description.
     * @return A Todo task.
     * @throws SigmaWolfException If the description is empty or contains invalid characters.
     */
    public static Todo parseTodo(String arguments) throws SigmaWolfException {
        String description = arguments.trim();
        if (description.isEmpty()) {
            throw new SigmaWolfException("The pack cannot track an empty task! Tell me what needs to be done.");
        }
        validateDescription(description);
        return new Todo(description);
    }

    /**
     * Parses a deadline task from the given arguments.
     *
     * @param arguments The arguments containing the task description and deadline.
     * @return A Deadline task.
     * @throws SigmaWolfException If arguments are invalid or date format is incorrect.
     */
    public static Deadline parseDeadline(String arguments) throws SigmaWolfException {
        if (arguments.trim().isEmpty()) {
            throw new SigmaWolfException("The pack needs to know what deadline to track! Provide details.");
        }
        if (!arguments.contains("/by ")) {
            throw new SigmaWolfException(
                    "Deadlines require a /by parameter! Format: deadline <task> /by <yyyy-MM-dd HHmm>");
        }
        if (arguments.indexOf("/by ") != arguments.lastIndexOf("/by ")) {
            throw new SigmaWolfException("Only one /by parameter is allowed!");
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
        validateDescription(description);

        try {
            LocalDateTime by = parseDateTime(byString);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new SigmaWolfException(DATE_FORMAT_ERROR);
        }
    }

    /**
     * Parses an event task from the given arguments.
     *
     * @param arguments The arguments containing the task description, start and end time.
     * @return An Event task.
     * @throws SigmaWolfException If arguments are invalid, dates are incorrect, or start is not before end.
     */
    public static Event parseEvent(String arguments) throws SigmaWolfException {
        if (arguments.trim().isEmpty()) {
            throw new SigmaWolfException("The pack needs to know what event to track! Provide details.");
        }
        if (!arguments.contains("/from ") || !arguments.contains("/to ")) {
            throw new SigmaWolfException(
                    "Events require /from and /to parameters! "
                    + "Format: event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        if (arguments.indexOf("/from ") != arguments.lastIndexOf("/from ")) {
            throw new SigmaWolfException("Only one /from parameter is allowed!");
        }
        if (arguments.indexOf("/to ") != arguments.lastIndexOf("/to ")) {
            throw new SigmaWolfException("Only one /to parameter is allowed!");
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
        validateDescription(description);

        try {
            LocalDateTime from = parseDateTime(fromString);
            LocalDateTime to = parseDateTime(toString);

            if (!from.isBefore(to)) {
                throw new SigmaWolfException(
                        "Event start time must be before end time!");
            }

            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            throw new SigmaWolfException(DATE_FORMAT_ERROR);
        }
    }

    /**
     * Parses a date-time string into a LocalDateTime object using strict validation.
     *
     * @param dateTimeString The date-time string to parse.
     * @return The parsed LocalDateTime.
     * @throws DateTimeParseException If the string is not in the correct format or the date does not exist.
     */
    private static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_FORMATTER);
    }

    /**
     * Parses the find command arguments to extract the search keyword.
     *
     * @param arguments The arguments containing the keyword to search for.
     * @return The search keyword.
     * @throws SigmaWolfException If the keyword is empty.
     */
    public static String parseFind(String arguments) throws SigmaWolfException {
        String keyword = arguments.trim();
        if (keyword.isEmpty()) {
            throw new SigmaWolfException("The pack needs a keyword to search for! Provide a search term.");
        }
        return keyword;
    }

    /**
     * Parses a task index from a string.
     *
     * @param input The string containing the task index.
     * @return The zero-based task index.
     * @throws SigmaWolfException If the input does not contain a valid index.
     */
    public static int parseTaskIndex(String input) throws SigmaWolfException {
        try {
            return Integer.parseInt(input.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new SigmaWolfException("That's not a valid number! Use numbers for task indices.");
        }
    }

    /**
     * Parses tag command arguments to extract task number and tag name.
     *
     * @param arguments The arguments containing task number and tag (e.g., "1 #important").
     * @return An array where [0] is the zero-based task index and [1] is the tag name.
     * @throws SigmaWolfException If the arguments are invalid.
     */
    public static String[] parseTag(String arguments) throws SigmaWolfException {
        String[] parts = arguments.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new SigmaWolfException("Tag command needs task number and tag! Use: tag 1 #tagname");
        }

        int taskIndex = parseTaskIndex(parts[0]);
        String tag = parts[1].trim();

        if (!tag.startsWith("#")) {
            throw new SigmaWolfException("Tags must start with #! Use: tag 1 #tagname");
        }

        tag = tag.substring(1); // Remove the # prefix
        if (tag.isEmpty()) {
            throw new SigmaWolfException("Tag name cannot be empty!");
        }
        if (tag.contains("|") || tag.contains(",") || tag.contains("#")) {
            throw new SigmaWolfException(
                    "Tag name cannot contain '|', ',' or '#' characters!");
        }
        if (tag.contains(" ")) {
            throw new SigmaWolfException("Tag name cannot contain spaces! Use a single word.");
        }

        return new String[] { String.valueOf(taskIndex), tag };
    }
}
