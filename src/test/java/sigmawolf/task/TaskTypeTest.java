package sigmawolf.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskTypeTest {

    @Test
    public void todo_getIcon_returnsT() {
        assertEquals("T", TaskType.TODO.getIcon());
    }

    @Test
    public void deadline_getIcon_returnsD() {
        assertEquals("D", TaskType.DEADLINE.getIcon());
    }

    @Test
    public void event_getIcon_returnsE() {
        assertEquals("E", TaskType.EVENT.getIcon());
    }

    @Test
    public void values_returnsAllThreeTypes() {
        assertEquals(3, TaskType.values().length);
    }
}
