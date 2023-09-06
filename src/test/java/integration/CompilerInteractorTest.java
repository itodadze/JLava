package integration;

import builder.CompilerInteractor;
import compiler.ClassCompiler;
import config.Configuration;
import helper.MapConfiguration;
import helper.StringLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.Directory;
import utility.FileGatherer;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static config.ConfigurationKey.OUTPUT;
import static config.ConfigurationKey.SOURCES;
import static org.junit.jupiter.api.Assertions.*;

public class CompilerInteractorTest {

    private final static String RES_PATH = Paths.get("src", "test", "resources",
            "interactor", "compiler").toString();

    @BeforeEach
    public void clearOutputDirectories() {
        try {
            (new Directory(Paths.get(RES_PATH, "dest").toFile())).clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testCompilerInteractorBasic() {
        List<String> sources = List.of(Paths.get(RES_PATH, "src").toString());
        String output = Paths.get(RES_PATH, "dest").toString();
        Configuration configuration = new MapConfiguration(
                Map.of(OUTPUT.key(), output), Map.of(SOURCES.key(), sources)
        );
        CompilerInteractor interactor = new CompilerInteractor(new ClassCompiler(
                new StringLogger(), new FileGatherer(new StringLogger(), "java")
        ));
        try {
            File file = interactor.compileClasses(configuration, List.of());
            assertEquals(Paths.get(output, "temp").toString(), file.getPath());
            assertTrue(Paths.get(output, "temp", "Class.class").toFile().exists());
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }
}
