package helper;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileContentProvider {
    private final String path;
    public FileContentProvider(String path) {
        this.path = path;
    }
    public String getContent() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}
