package unit;

import helper.FileContentProvider;
import logger.OutputLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.ClearableDirectory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

public class OutputLoggerTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "log").toString();

    @BeforeEach
    public void clearPreviousLogs() {
        new ClearableDirectory(new File(RES_PATH)).clearContent();
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
            Assertions.assertEquals(text + "\n", (new FileContentProvider(path)).getContent());
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}
