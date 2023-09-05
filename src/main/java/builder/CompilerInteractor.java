package builder;

import compiler.ClassCompiler;
import config.Configuration;
import utility.Directory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static config.ConfigurationKey.OUTPUT;
import static config.ConfigurationKey.SOURCES;

public class CompilerInteractor {
    private final static String TEMP_DIRECTORY = "temp";
    private final ClassCompiler classCompiler;

    public CompilerInteractor(ClassCompiler classCompiler) {
        this.classCompiler = classCompiler;
    }

    /**
     * @param dependencies  required dependencies for compiling classes.
     * @return              temporary directory in which compiled classes are saved.
     * @throws Exception    if temporary directory can not be created.
     */
    public File compileClasses(Configuration configuration, List<String> dependencies)
            throws Exception {
        List<String> sources = configuration.getList(SOURCES.key());
        String output = configuration.getString(OUTPUT.key());
        File tempDirectory = Paths.get(output, TEMP_DIRECTORY).toFile();
        (new Directory(tempDirectory)).make();
        this.classCompiler.compileClasses(sources, dependencies, tempDirectory.getPath());
        return tempDirectory;
    }
}
