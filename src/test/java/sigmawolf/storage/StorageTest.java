package sigmawolf.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import sigmawolf.exception.SigmaWolfException;
import sigmawolf.task.Deadline;
import sigmawolf.task.Event;
import sigmawolf.task.Task;
import sigmawolf.task.Todo;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void constructor_nullPath_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Storage(null));
    }

    @Test
    public void constructor_emptyPath_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Storage(""));
    }

    @Test
    public void load_nonExistentFile_returnsEmptyList() throws SigmaWolfException {
        String path = tempDir.resolve("nonexistent.txt").toString();
        Storage storage = new Storage(path);
        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_emptyFile_returnsEmptyList() throws SigmaWolfException, IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.createFile(file);
        Storage storage = new Storage(file.toString());
        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_createsDirectoryIfNeeded() throws SigmaWolfException {
        Path subDir = tempDir.resolve("subdir");
        String path = subDir.resolve("data.txt").toString();
        Storage storage = new Storage(path);
        storage.load();
        assertTrue(Files.isDirectory(subDir));
    }

    @Test
    public void saveAndLoad_todo_roundtripPreservesData() throws SigmaWolfException {
        String path = tempDir.resolve("todo.txt").toString();
        Storage storage = new Storage(path);

        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new Todo("read book"));
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertEquals("read book", loaded.get(0).getDescription());
    }

    @Test
    public void saveAndLoad_deadline_roundtripPreservesData() throws SigmaWolfException {
        String path = tempDir.resolve("deadline.txt").toString();
        Storage storage = new Storage(path);

        LocalDateTime by = LocalDateTime.of(2026, 3, 1, 18, 0);
        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new Deadline("submit report", by));
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Deadline);
        assertEquals("submit report", loaded.get(0).getDescription());
        assertEquals(by, ((Deadline) loaded.get(0)).getBy());
    }

    @Test
    public void saveAndLoad_event_roundtripPreservesData() throws SigmaWolfException {
        String path = tempDir.resolve("event.txt").toString();
        Storage storage = new Storage(path);

        LocalDateTime from = LocalDateTime.of(2026, 2, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 2, 15, 16, 0);
        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new Event("meeting", from, to));
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Event);
        assertEquals("meeting", loaded.get(0).getDescription());
        assertEquals(from, ((Event) loaded.get(0)).getFrom());
        assertEquals(to, ((Event) loaded.get(0)).getTo());
    }

    @Test
    public void saveAndLoad_doneStatus_preserved() throws SigmaWolfException {
        String path = tempDir.resolve("done.txt").toString();
        Storage storage = new Storage(path);

        Todo todo = new Todo("read book");
        todo.markAsDone();
        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(todo);
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0).isDone());
    }

    @Test
    public void saveAndLoad_tags_preserved() throws SigmaWolfException {
        String path = tempDir.resolve("tags.txt").toString();
        Storage storage = new Storage(path);

        Todo todo = new Todo("read book");
        todo.addTag("urgent");
        todo.addTag("fun");
        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(todo);
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0).getTags().contains("urgent"));
        assertTrue(loaded.get(0).getTags().contains("fun"));
    }

    @Test
    public void saveAndLoad_multipleTasks_allPreserved() throws SigmaWolfException {
        String path = tempDir.resolve("multi.txt").toString();
        Storage storage = new Storage(path);

        LocalDateTime by = LocalDateTime.of(2026, 3, 1, 18, 0);
        LocalDateTime from = LocalDateTime.of(2026, 2, 15, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 2, 15, 16, 0);

        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new Todo("read book"));
        toSave.add(new Deadline("submit report", by));
        toSave.add(new Event("meeting", from, to));
        storage.save(toSave);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertTrue(loaded.get(2) instanceof Event);
    }

    @Test
    public void load_corruptedLine_skipped() throws SigmaWolfException, IOException {
        Path file = tempDir.resolve("corrupted.txt");
        Files.write(file, "GARBAGE LINE\n".getBytes(StandardCharsets.UTF_8));
        Storage storage = new Storage(file.toString());
        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_partiallyCorruptedFile_validTasksPreserved()
            throws SigmaWolfException, IOException {
        Path file = tempDir.resolve("partial.txt");
        String content = "T | 0 | read book | \nGARBAGE LINE\n";
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        Storage storage = new Storage(file.toString());
        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.get(0).getDescription());
    }

    @Test
    public void save_createsDirectoryIfNeeded() throws SigmaWolfException {
        Path subDir = tempDir.resolve("newdir");
        String path = subDir.resolve("data.txt").toString();
        Storage storage = new Storage(path);

        ArrayList<Task> toSave = new ArrayList<>();
        toSave.add(new Todo("test"));
        storage.save(toSave);

        assertTrue(Files.isDirectory(subDir));
        assertTrue(Files.exists(subDir.resolve("data.txt")));
    }
}
