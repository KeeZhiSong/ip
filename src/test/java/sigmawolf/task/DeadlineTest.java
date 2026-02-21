package sigmawolf.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    private static final LocalDateTime SAMPLE_DATE = LocalDateTime.of(2026, 3, 1, 18, 0);

    @Test
    public void constructor_validInput_storesDescriptionAndDeadline() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        assertEquals("submit report", deadline.getDescription());
        assertEquals(SAMPLE_DATE, deadline.getBy());
    }

    @Test
    public void constructor_validInput_notDoneByDefault() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        assertFalse(deadline.isDone());
        assertEquals(" ", deadline.getStatusIcon());
    }

    @Test
    public void getTypeIcon_deadline_returnsD() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        assertEquals("D", deadline.getTypeIcon());
    }

    @Test
    public void getBy_returnsCorrectDateTime() {
        LocalDateTime date = LocalDateTime.of(2026, 12, 25, 23, 59);
        Deadline deadline = new Deadline("christmas task", date);
        assertEquals(date, deadline.getBy());
    }

    @Test
    public void markAsDone_updatesStatusIcon() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        deadline.markAsDone();
        assertTrue(deadline.isDone());
        assertEquals("X", deadline.getStatusIcon());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        String result = deadline.toString();
        assertTrue(result.contains("[D][ ]"));
        assertTrue(result.contains("submit report"));
        assertTrue(result.contains("(by:"));
        assertTrue(result.contains("Mar 01 2026 18:00"));
    }

    @Test
    public void toString_done_correctFormat() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        deadline.markAsDone();
        String result = deadline.toString();
        assertTrue(result.contains("[D][X]"));
        assertTrue(result.contains("submit report"));
    }

    @Test
    public void toString_withTags_tagsNotIncluded() {
        Deadline deadline = new Deadline("submit report", SAMPLE_DATE);
        deadline.addTag("urgent");
        String result = deadline.toString();
        // Deadline.toString() overrides Task.toString() and does not include tags
        assertFalse(result.contains("#urgent"));
    }
}
