package sigmawolf.parser;

import org.junit.jupiter.api.Test;
import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Todo;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
}
