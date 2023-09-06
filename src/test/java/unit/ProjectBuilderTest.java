package unit;

import builder.CompilerInteractor;
import builder.DependencyInteractor;
import builder.PackagerInteractor;
import builder.ProjectBuilder;
import config.Configuration;
import helper.StringLogger;
import logger.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static logger.LogMessages.BUILD_ERROR;
import static logger.LogMessages.JSON_FILE_ERROR;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectBuilderTest {

    private static final String RES_PATH = Paths.get("src", "test", "resources",
            "configuration").toString();

    @Test
    public void testProjectBuilderRequirementsUnmet() {
        StringLogger logger = new StringLogger();
        try {
            String path = Paths.get(RES_PATH, "valid.json").toString();
            ProjectBuilder builder = mockProjectBuilder(logger, false);
            builder.build(path);
            assertTrue(logger.getLog().startsWith(JSON_FILE_ERROR.string()));
        } catch (Exception e) {
            fail("Exception thrown when expected: " + e.getMessage());
        }
    }

    @Test
    public void testProjectBuilderComponentError() {
        StringLogger logger = new StringLogger();
        try {
            String path = Paths.get(RES_PATH, "required.json").toString();
            ProjectBuilder builder = mockProjectBuilder(logger, true);
            builder.build(path);
            assertTrue(logger.getLog().startsWith(BUILD_ERROR.string()));
        } catch (Exception e) {
            fail("Exception thrown when expected: " + e.getMessage());
        }
    }

    @Test
    public void testProjectBuilderRequirementsMet() {
        StringLogger logger = new StringLogger();
        try {
            String path = Paths.get(RES_PATH, "required.json").toString();
            ProjectBuilder builder = mockProjectBuilder(logger, false);
            builder.build(path);
            assertTrue(logger.getLog().isEmpty());
        } catch (Exception e) {
            fail("Exception thrown when expected: " + e.getMessage());
        }
    }

    private ProjectBuilder mockProjectBuilder(Logger logger, boolean packagerErrors)
            throws Exception {
        CompilerInteractor compilerInteractor = mock(CompilerInteractor.class);
        when(compilerInteractor.compileClasses(any(Configuration.class), any()))
                .thenReturn(new File(""));
        DependencyInteractor dependencyInteractor = mock(DependencyInteractor.class);
        when(dependencyInteractor.fetchPaths(any(Configuration.class)))
                .thenReturn(List.of());
        PackagerInteractor packagerInteractor = mock(PackagerInteractor.class);
        if (packagerErrors) {
            doThrow(new Exception()).when(packagerInteractor).packageClasses(
                    any(Configuration.class), any()
            );
        } else {
            doNothing().when(packagerInteractor).packageClasses(
                    any(Configuration.class), any()
            );
        }
        return new ProjectBuilder(
                logger, compilerInteractor, dependencyInteractor, packagerInteractor
        );
    }

}
