package sigmawolf.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {
    private TaskList tasks;
    private Todo todo1;
    private Todo todo2;
    private Todo todo3;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        todo1 = new Todo("read book");
        todo2 = new Todo("return book");
        todo3 = new Todo("buy groceries");
    }

    @Test
    public void add_singleTask_success() {
        tasks.add(todo1);
        assertEquals(1, tasks.size());
        assertEquals(todo1, tasks.get(0));
    }

    @Test
    public void add_multipleTasks_success() {
        tasks.add(todo1);
        tasks.add(todo2);
        tasks.add(todo3);
        assertEquals(3, tasks.size());
        assertEquals(todo1, tasks.get(0));
        assertEquals(todo2, tasks.get(1));
        assertEquals(todo3, tasks.get(2));
    }

    @Test
    public void remove_existingTask_success() {
        tasks.add(todo1);
        tasks.add(todo2);
        Task removed = tasks.remove(0);
        assertEquals(todo1, removed);
        assertEquals(1, tasks.size());
        assertEquals(todo2, tasks.get(0));
    }

    @Test
    public void get_validIndex_success() {
        tasks.add(todo1);
        tasks.add(todo2);
        assertEquals(todo1, tasks.get(0));
        assertEquals(todo2, tasks.get(1));
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, tasks.size());
    }

    @Test
    public void size_nonEmptyList_returnsCorrectSize() {
        tasks.add(todo1);
        tasks.add(todo2);
        assertEquals(2, tasks.size());
    }

    @Test
    public void findTasks_matchingKeyword_returnsMatches() {
        tasks.add(todo1);  // "read book"
        tasks.add(todo2);  // "return book"
        tasks.add(todo3);  // "buy groceries"
        
        TaskList results = tasks.findTasks("book");
        assertEquals(2, results.size());
        assertEquals(todo1, results.get(0));
        assertEquals(todo2, results.get(1));
    }

    @Test
    public void findTasks_noMatches_returnsEmptyList() {
        tasks.add(todo1);
        tasks.add(todo2);
        
        TaskList results = tasks.findTasks("meeting");
        assertEquals(0, results.size());
    }

    @Test
    public void findTasks_caseInsensitive_returnsMatches() {
        tasks.add(todo1);  // "read book"
        tasks.add(todo2);  // "return book"
        
        TaskList results = tasks.findTasks("BOOK");
        assertEquals(2, results.size());
    }

    @Test
    public void findTasks_partialMatch_returnsMatches() {
        tasks.add(todo1);  // "read book"
        tasks.add(todo3);  // "buy groceries"
        
        TaskList results = tasks.findTasks("gro");
        assertEquals(1, results.size());
        assertEquals(todo3, results.get(0));
    }

    @Test
    public void getTasks_returnsUnderlyingList() {
        tasks.add(todo1);
        tasks.add(todo2);
        
        ArrayList<Task> taskList = tasks.getTasks();
        assertEquals(2, taskList.size());
        assertTrue(taskList.contains(todo1));
        assertTrue(taskList.contains(todo2));
    }
}
