package sigmawolf.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void constructor_validDescription_success() {
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    public void markAsDone_newTask_markedCorrectly() {
        Todo todo = new Todo("read book");
        assertFalse(todo.isDone());

        todo.markAsDone();
        assertTrue(todo.isDone());
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void markAsNotDone_doneTask_unmarkedCorrectly() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertTrue(todo.isDone());

        todo.markAsNotDone();
        assertFalse(todo.isDone());
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void getStatusIcon_notDone_returnsSpace() {
        Todo todo = new Todo("read book");
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void getStatusIcon_done_returnsX() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void getTypeIcon_todo_returnsT() {
        Todo todo = new Todo("read book");
        assertEquals("T", todo.getTypeIcon());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toString_done_correctFormat() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }

    // Tag functionality tests (testing Task base class via Todo)

    @Test
    public void addTag_validTag_tagAdded() {
        Todo todo = new Todo("read book");
        todo.addTag("urgent");
        assertTrue(todo.getTags().contains("urgent"));
    }

    @Test
    public void addTag_uppercaseTag_convertedToLowercase() {
        Todo todo = new Todo("read book");
        todo.addTag("URGENT");
        assertTrue(todo.getTags().contains("urgent"));
        assertFalse(todo.getTags().contains("URGENT"));
    }

    @Test
    public void addTag_nullTag_exceptionThrown() {
        Todo todo = new Todo("read book");
        assertThrows(IllegalArgumentException.class, () -> todo.addTag(null));
    }

    @Test
    public void addTag_emptyTag_exceptionThrown() {
        Todo todo = new Todo("read book");
        assertThrows(IllegalArgumentException.class, () -> todo.addTag(""));
        assertThrows(IllegalArgumentException.class, () -> todo.addTag("   "));
    }

    @Test
    public void addTag_tagWithPipe_exceptionThrown() {
        Todo todo = new Todo("read book");
        assertThrows(IllegalArgumentException.class, () -> todo.addTag("a|b"));
    }

    @Test
    public void addTag_tagWithComma_exceptionThrown() {
        Todo todo = new Todo("read book");
        assertThrows(IllegalArgumentException.class, () -> todo.addTag("a,b"));
    }

    @Test
    public void addTag_tagWithHash_exceptionThrown() {
        Todo todo = new Todo("read book");
        assertThrows(IllegalArgumentException.class, () -> todo.addTag("a#b"));
    }

    @Test
    public void addTag_duplicateTag_notDuplicated() {
        Todo todo = new Todo("read book");
        todo.addTag("fun");
        todo.addTag("fun");
        assertEquals(1, todo.getTags().size());
    }

    @Test
    public void removeTag_existingTag_returnsTrue() {
        Todo todo = new Todo("read book");
        todo.addTag("fun");
        assertTrue(todo.removeTag("fun"));
        assertTrue(todo.getTags().isEmpty());
    }

    @Test
    public void removeTag_nonExistentTag_returnsFalse() {
        Todo todo = new Todo("read book");
        assertFalse(todo.removeTag("nonexistent"));
    }

    @Test
    public void getTagsString_noTags_returnsEmpty() {
        Todo todo = new Todo("read book");
        assertEquals("", todo.getTagsString());
    }

    @Test
    public void getTagsString_multipleTags_sortedAlphabetically() {
        Todo todo = new Todo("read book");
        todo.addTag("zebra");
        todo.addTag("alpha");
        assertEquals("#alpha #zebra", todo.getTagsString());
    }
}
