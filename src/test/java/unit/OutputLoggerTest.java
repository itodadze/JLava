package unit;

import helper.FileContentProvider;
import logger.OutputLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.Directory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OutputLoggerTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "log").toString();

    @BeforeEach
    public void clearPreviousLogs() {
        try {
            new Directory(new File(RES_PATH)).clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void testWriteToOutputLogger() {
        try {
            String text = "TEXT TEXT TEXT";
            String path = Paths.get(RES_PATH, "logs.txt").toString();
            File file = new File(path);
            file.createNewFile();
            OutputLogger logger = new OutputLogger(new FileOutputStream(file));
            logger.printLine(text);
            logger.close();
            assertEquals(text + "\n", (new FileContentProvider(path)).getContent());
        } catch(Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}
