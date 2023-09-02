package builder;

import compiler.ClassCompiler;
import dependency.*;
import logger.Logger;
import org.apache.http.impl.client.CloseableHttpClient;
import packager.JarPackager;
import utility.FileGatherer;

/**
 * A class responsible for assembling a ProjectBuilder via builder pattern.
 */
public class ProjectBuilderAssembler {
    private Logger logger;
    private String cacheDirectory;
    private CloseableHttpClient httpClient;
    private String configFilePath;
    public ProjectBuilderAssembler logger(Logger logger) {
        this.logger = logger;
        return this;
    }
    public ProjectBuilderAssembler cache(String path) {
        this.cacheDirectory = path;
        return this;
    }
    public ProjectBuilderAssembler httpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }
    public ProjectBuilderAssembler config(String path) {
        this.configFilePath = path;
        return this;
    }

    /**
     * Assembles a ProjectBuilder based on previously set fields.
     *
     * @return a ProjectBuilder instance.
     * @throws IllegalStateException if any of the required fields are missing.
     */
    public ProjectBuilder assemble() throws IllegalStateException {
        if (!canAssemble()) throw new IllegalStateException(
                "Required field is missing"
        );
        CompilerInteractor compilerInteractor = new CompilerInteractor(
                new ClassCompiler(this.logger, new FileGatherer(this.logger, "java"))
        );
        DependencyInteractor dependencyInteractor = new DependencyInteractor(
              new DependencyManager(this.logger, new DependencyDownloader(
                      this.logger, new DependencyResponseProcessor(this.logger,
                      this.cacheDirectory), this.httpClient
              ), DependencyCacheManager.INSTANCE)
        );
        PackagerInteractor packagerInteractor = new PackagerInteractor(
                new JarPackager(this.logger)
        );
        return new ProjectBuilder(
                this.logger, compilerInteractor,
                dependencyInteractor, packagerInteractor
        );
    }
    private boolean canAssemble() {
        return this.logger != null && this.cacheDirectory != null &&
                this.httpClient != null && this.configFilePath != null;
    }
}
