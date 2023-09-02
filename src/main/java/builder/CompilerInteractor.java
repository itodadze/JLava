package builder;

import compiler.ClassCompiler;
import config.Configuration;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static config.ConfigurationKey.OUTPUT;
import static config.ConfigurationKey.SOURCE;

public class CompilerInteractor {

    private final ClassCompiler classCompiler;

    public CompilerInteractor(ClassCompiler classCompiler) {
        this.classCompiler = classCompiler;
    }

    public File compileClasses(Configuration configuration, List<String> dependencies)
            throws Exception {
        List<String> sources = configuration.getList(SOURCE.key());
        String output = configuration.getString(OUTPUT.key());
        String tempDirectoryPath = Paths.get(output, "temp").toString();
        File tempDirectory = new File(tempDirectoryPath);
        if (!tempDirectory.mkdir()) {
            throw new IllegalStateException("Could not create: " + tempDirectoryPath);
        }
        this.classCompiler.compileClasses(sources, dependencies, tempDirectoryPath);
        return tempDirectory;
    }

}
