package sigmawolf.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.task.Todo;

public class ParserTest {

    @Test
    public void getCommand_validInput_success() {
        assertEquals("todo", Parser.getCommand("todo read book"));
        assertEquals("deadline", Parser.getCommand("deadline return book /by 2026-02-15 1800"));
        assertEquals("list", Parser.getCommand("list"));
        assertEquals("bye", Parser.getCommand("bye"));
    }

    @Test
    public void getArguments_validInput_success() {
        assertEquals("read book", Parser.getArguments("todo read book"));
        assertEquals("", Parser.getArguments("list"));
        assertEquals("return book /by 2026-02-15 1800",
                Parser.getArguments("deadline return book /by 2026-02-15 1800"));
    }

    @Test
    public void parseTaskNumber_validInput_success() throws SigmaWolfException {
        assertEquals(0, Parser.parseTaskNumber("mark 1"));
        assertEquals(4, Parser.parseTaskNumber("delete 5"));
        assertEquals(9, Parser.parseTaskNumber("unmark 10"));
    }

    @Test
    public void parseTaskNumber_emptyNumber_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTaskNumber("mark "));
        assertThrows(SigmaWolfException.class, () -> Parser.parseTaskNumber("delete"));
    }

    @Test
    public void parseTaskNumber_invalidNumber_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTaskNumber("mark abc"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseTaskNumber("delete xyz"));
    }

    @Test
    public void parseTodo_validInput_success() {
        try {
            Todo todo = Parser.parseTodo("read book");
            assertEquals("read book", todo.getDescription());
        } catch (SigmaWolfException e) {
            fail("Should not throw exception for valid input");
        }
    }

    @Test
    public void parseTodo_emptyDescription_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTodo(""));
        assertThrows(SigmaWolfException.class, () -> Parser.parseTodo("   "));
    }

    @Test
    public void parseDeadline_validInput_success() {
        try {
            Deadline deadline = Parser.parseDeadline("return book /by 2026-02-15 1800");
            assertEquals("return book", deadline.getDescription());
            assertEquals(LocalDateTime.of(2026, 2, 15, 18, 0), deadline.getBy());
        } catch (SigmaWolfException e) {
            fail("Should not throw exception for valid input");
        }
    }

    @Test
    public void parseDeadline_missingBy_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseDeadline("return book"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseDeadline("return book /at 2026-02-15"));
    }

    @Test
    public void parseDeadline_invalidDateFormat_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseDeadline("return book /by 15-02-2026"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseDeadline("return book /by tomorrow"));
    }

    @Test
    public void parseEvent_validInput_success() {
        try {
            Event event = Parser.parseEvent("meeting /from 2026-02-15 1400 /to 2026-02-15 1600");
            assertEquals("meeting", event.getDescription());
            assertEquals(LocalDateTime.of(2026, 2, 15, 14, 0), event.getFrom());
            assertEquals(LocalDateTime.of(2026, 2, 15, 16, 0), event.getTo());
        } catch (SigmaWolfException e) {
            fail("Should not throw exception for valid input");
        }
    }

    @Test
    public void parseEvent_missingFromTo_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseEvent("meeting"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseEvent("meeting /from 2026-02-15 1400"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseEvent("meeting /to 2026-02-15 1600"));
    }

    @Test
    public void parseFind_validInput_success() {
        try {
            assertEquals("book", Parser.parseFind("book"));
            assertEquals("meeting", Parser.parseFind("  meeting  "));
        } catch (SigmaWolfException e) {
            fail("Should not throw exception for valid input");
        }
    }

    @Test
    public void parseFind_emptyKeyword_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseFind(""));
        assertThrows(SigmaWolfException.class, () -> Parser.parseFind("   "));
    }

    // New tests for null/blank input handling

    @Test
    public void getCommand_nullInput_returnsEmpty() {
        assertEquals("", Parser.getCommand(null));
    }

    @Test
    public void getCommand_blankInput_returnsEmpty() {
        assertEquals("", Parser.getCommand("   "));
    }

    @Test
    public void getArguments_nullInput_returnsEmpty() {
        assertEquals("", Parser.getArguments(null));
    }

    @Test
    public void getArguments_blankInput_returnsEmpty() {
        assertEquals("", Parser.getArguments("   "));
    }

    @Test
    public void getCommand_multipleSpaces_extractsCorrectly() {
        assertEquals("todo", Parser.getCommand("todo    read book"));
    }

    // New tests for duplicate parameter detection

    @Test
    public void parseDeadline_duplicateBy_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseDeadline("task /by 2026-01-01 1200 /by 2026-02-01 1200"));
    }

    @Test
    public void parseEvent_duplicateFrom_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseEvent("task /from 2026-01-01 1200 /from 2026-01-01 1300 /to 2026-01-01 1400"));
    }

    @Test
    public void parseEvent_duplicateTo_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseEvent("task /from 2026-01-01 1200 /to 2026-01-01 1300 /to 2026-01-01 1400"));
    }

    // New tests for event time validation

    @Test
    public void parseEvent_startAfterEnd_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseEvent("task /from 2026-01-01 1800 /to 2026-01-01 1200"));
    }

    @Test
    public void parseEvent_startEqualsEnd_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseEvent("task /from 2026-01-01 1200 /to 2026-01-01 1200"));
    }

    // New tests for pipe character validation

    @Test
    public void parseTodo_descriptionWithPipe_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTodo("read|book"));
    }

    @Test
    public void parseDeadline_descriptionWithPipe_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseDeadline("read|book /by 2026-01-01 1200"));
    }

    @Test
    public void parseEvent_descriptionWithPipe_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseEvent("read|book /from 2026-01-01 1200 /to 2026-01-01 1300"));
    }

    // New test for strict date parsing (non-existent dates)

    @Test
    public void parseDeadline_nonExistentDate_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () ->
                Parser.parseDeadline("task /by 2026-02-30 1200"));
    }

    // New tests for parseTaskIndex

    @Test
    public void parseTaskIndex_validInput_returnsZeroBasedIndex() throws SigmaWolfException {
        assertEquals(0, Parser.parseTaskIndex("1"));
        assertEquals(4, Parser.parseTaskIndex("5"));
    }

    @Test
    public void parseTaskIndex_invalidInput_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTaskIndex("abc"));
    }

    // New tests for parseTag

    @Test
    public void parseTag_validInput_returnsCorrectArray() throws SigmaWolfException {
        String[] result = Parser.parseTag("1 #important");
        assertEquals("0", result[0]); // zero-based index
        assertEquals("important", result[1]);
    }

    @Test
    public void parseTag_missingTag_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTag("1"));
    }

    @Test
    public void parseTag_tagWithoutHash_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTag("1 important"));
    }

    @Test
    public void parseTag_tagWithInvalidChars_exceptionThrown() {
        assertThrows(SigmaWolfException.class, () -> Parser.parseTag("1 #a|b"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseTag("1 #a,b"));
        assertThrows(SigmaWolfException.class, () -> Parser.parseTag("1 #a#b"));
    }
}
