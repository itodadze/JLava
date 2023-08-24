package unit;

import dependency.DependencyResponseProcessor;
import utility.ClearableDirectory;
import helper.FileContentProvider;
import helper.StringLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyResponseProcessorTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "processor").toString();

    @BeforeEach
    public void clearOutputDirectories() {
        (new ClearableDirectory(new File(Paths.get(RES_PATH, "basic").toString()))).clearContent();
    }

    @Test
    public void testDependencyResponseProcessorBasic() {
        StringLogger logger = new StringLogger();
        String outputDirectory = RES_PATH + "/basic";
        String repository = "repo";
        String name = "basic";
        String content = "content\n";

        DependencyResponseProcessor processor = new DependencyResponseProcessor(logger, outputDirectory);
        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        try {
            when(httpEntity.getContent()).thenReturn(inputStream);
            when(httpResponse.getEntity()).thenReturn(httpEntity);

            String path = processor.process(httpResponse, repository, name);
            httpResponse.close();

            Assertions.assertEquals(Paths.get(outputDirectory, repository, name + ".jar")
                    .toString(), path);
            Assertions.assertEquals(content, (new FileContentProvider(path)).getContent());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
