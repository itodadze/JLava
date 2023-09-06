package runner;

import com.beust.jcommander.Parameter;

public class JLavaCLIParameters {
    @Parameter(names = { "-h", "--help" }, description = "Show help")
    public boolean help;
    @Parameter(names = { "-c", "--config" }, description = "JSON config file path", required = true)
    public String configFilePath;
}
