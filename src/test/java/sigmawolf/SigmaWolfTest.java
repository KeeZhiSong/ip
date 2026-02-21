package sigmawolf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SigmaWolfTest {

    @TempDir
    Path tempDir;

    private SigmaWolf bot;
    private String filePath;

    @BeforeEach
    public void setUp() {
        filePath = tempDir.resolve("test_data.txt").toString();
        bot = new SigmaWolf(filePath);
    }

    @Test
    public void getResponse_nullInput_returnsError() {
        String response = bot.getResponse(null);
        assertTrue(response.startsWith("GRRR!!!"));
    }

    @Test
    public void getResponse_blankInput_returnsError() {
        String response = bot.getResponse("   ");
        assertTrue(response.startsWith("GRRR!!!"));
    }

    @Test
    public void getResponse_unknownCommand_returnsError() {
        String response = bot.getResponse("foobar");
        assertTrue(response.startsWith("GRRR!!!"));
        assertTrue(response.contains("doesn't understand"));
    }

    @Test
    public void getResponse_bye_returnsFarewell() {
        String response = bot.getResponse("bye");
        assertTrue(response.contains("AWOOOOOOOOOOO"));
    }

    @Test
    public void getResponse_todoValid_returnsConfirmation() {
        String response = bot.getResponse("todo read book");
        assertTrue(response.contains("Got it"));
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("1 tasks"));
    }

    @Test
    public void getResponse_deadlineValid_returnsConfirmation() {
        String response = bot.getResponse("deadline submit /by 2026-03-01 1800");
        assertTrue(response.contains("Got it"));
        assertTrue(response.contains("submit"));
    }

    @Test
    public void getResponse_eventValid_returnsConfirmation() {
        String response = bot.getResponse(
                "event meeting /from 2026-03-01 1400 /to 2026-03-01 1600");
        assertTrue(response.contains("Got it"));
        assertTrue(response.contains("meeting"));
    }

    @Test
    public void getResponse_listEmpty_returnsEmptyMessage() {
        String response = bot.getResponse("list");
        assertTrue(response.contains("empty"));
    }

    @Test
    public void getResponse_listWithTasks_showsAllTasks() {
        bot.getResponse("todo read book");
        bot.getResponse("todo buy milk");
        String response = bot.getResponse("list");
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("buy milk"));
    }

    @Test
    public void getResponse_markValid_returnsMarkedMessage() {
        bot.getResponse("todo read book");
        String response = bot.getResponse("mark 1");
        assertTrue(response.contains("marked this task as done"));
    }

    @Test
    public void getResponse_unmarkValid_returnsUnmarkedMessage() {
        bot.getResponse("todo read book");
        bot.getResponse("mark 1");
        String response = bot.getResponse("unmark 1");
        assertTrue(response.contains("not done"));
    }

    @Test
    public void getResponse_deleteValid_returnsDeletedMessage() {
        bot.getResponse("todo read book");
        String response = bot.getResponse("delete 1");
        assertTrue(response.contains("removed"));
        assertTrue(response.contains("0 tasks"));
    }

    @Test
    public void getResponse_findMatch_returnsResults() {
        bot.getResponse("todo read book");
        bot.getResponse("todo buy milk");
        String response = bot.getResponse("find book");
        assertTrue(response.contains("read book"));
    }

    @Test
    public void getResponse_findNoMatch_returnsNoResults() {
        bot.getResponse("todo read book");
        String response = bot.getResponse("find xyz");
        assertTrue(response.contains("No matching"));
    }

    @Test
    public void getResponse_tagValid_returnsTaggedMessage() {
        bot.getResponse("todo read book");
        String response = bot.getResponse("tag 1 #urgent");
        assertTrue(response.contains("Tagged"));
        assertTrue(response.contains("#urgent"));
    }

    @Test
    public void getResponse_untagValid_returnsUntaggedMessage() {
        bot.getResponse("todo read book");
        bot.getResponse("tag 1 #urgent");
        String response = bot.getResponse("untag 1 #urgent");
        assertTrue(response.contains("Removed tag"));
    }

    @Test
    public void getResponse_markOutOfRange_returnsError() {
        String response = bot.getResponse("mark 1");
        assertTrue(response.startsWith("GRRR!!!"));
    }

    @Test
    public void getResponse_statePersistence_loadAfterRestart() {
        bot.getResponse("todo read book");

        // Create a new SigmaWolf instance with the same file path
        SigmaWolf newBot = new SigmaWolf(filePath);
        String response = newBot.getResponse("list");
        assertTrue(response.contains("read book"));
    }
}
