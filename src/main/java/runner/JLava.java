package runner;

import builder.ProjectBuilder;
import com.beust.jcommander.JCommander;
import compiler.ClassCompiler;
import dependency.*;
import logger.Logger;
import logger.OutputLogger;
import logger.TextOutputStreamProvider;
import org.apache.http.impl.client.HttpClients;
import packager.JarPackager;
import packager.Packager;
import utility.FileGatherer;

import java.nio.file.Paths;

public class JLava {
    public final static int DEFAULT_CACHE_SIZE = 2048;

    public static void main(String[] args) {
        JLavaCLIParameters cliParameters = new JLavaCLIParameters();
        JCommander jCommander = new JCommander.Builder()
                .addObject(cliParameters).build();
        jCommander.parse(args);
        if (cliParameters.help) {
            jCommander.usage();
        } else {
            buildProject(cliParameters.configFilePath);
        }
    }

    private static void buildProject(String configFilePath) {
        try {
            Logger logger = new OutputLogger((new TextOutputStreamProvider(
                    Paths.get(".logger", "logs.txt").toString())).get()
            );
            FileGatherer javaFileGatherer = new FileGatherer(logger, "java");
            ClassCompiler compiler = new ClassCompiler(logger, javaFileGatherer);
            DependencyResponseProcessor processor = new DependencyResponseProcessor(
                    logger, DependencyCacheProvider.CACHE_DIRECTORY
            );
            DependencyDownloader downloader = new DependencyDownloader(
                    logger, processor, HttpClients.createDefault()
            );
            DependencyCacheManager cache = DependencyCacheProvider.getInstance(
                    logger, DEFAULT_CACHE_SIZE
            );
            DependencyManager dependencyManager = new DependencyManager(
                    logger, downloader, cache
            );
            Packager packager = new JarPackager(logger);
            ProjectBuilder builder = new ProjectBuilder(
                    logger, compiler, dependencyManager, packager
            );
            builder.build(configFilePath);
            logger.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

}
