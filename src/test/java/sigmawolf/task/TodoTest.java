package sigmawolf.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
