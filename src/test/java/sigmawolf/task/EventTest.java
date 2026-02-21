package sigmawolf.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class EventTest {

    private static final LocalDateTime SAMPLE_FROM = LocalDateTime.of(2026, 2, 15, 14, 0);
    private static final LocalDateTime SAMPLE_TO = LocalDateTime.of(2026, 2, 15, 16, 0);

    @Test
    public void constructor_validInput_storesDescriptionAndTimes() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        assertEquals("meeting", event.getDescription());
        assertEquals(SAMPLE_FROM, event.getFrom());
        assertEquals(SAMPLE_TO, event.getTo());
    }

    @Test
    public void constructor_validInput_notDoneByDefault() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        assertFalse(event.isDone());
        assertEquals(" ", event.getStatusIcon());
    }

    @Test
    public void getTypeIcon_event_returnsE() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        assertEquals("E", event.getTypeIcon());
    }

    @Test
    public void getFrom_returnsCorrectDateTime() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        assertEquals(SAMPLE_FROM, event.getFrom());
    }

    @Test
    public void getTo_returnsCorrectDateTime() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        assertEquals(SAMPLE_TO, event.getTo());
    }

    @Test
    public void markAsDone_updatesStatus() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        event.markAsDone();
        assertTrue(event.isDone());
        assertEquals("X", event.getStatusIcon());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        String result = event.toString();
        assertTrue(result.contains("[E][ ]"));
        assertTrue(result.contains("meeting"));
        assertTrue(result.contains("(from:"));
        assertTrue(result.contains("Feb 15 2026 14:00"));
        assertTrue(result.contains("to:"));
        assertTrue(result.contains("Feb 15 2026 16:00"));
    }

    @Test
    public void toString_done_correctFormat() {
        Event event = new Event("meeting", SAMPLE_FROM, SAMPLE_TO);
        event.markAsDone();
        String result = event.toString();
        assertTrue(result.contains("[E][X]"));
        assertTrue(result.contains("meeting"));
    }
}
