package builder;

import compiler.ClassCompiler;
import dependency.DependencyCacheProvider;
import dependency.DependencyDownloader;
import dependency.DependencyManager;
import dependency.DependencyResponseProcessor;
import logger.Logger;
import org.apache.http.impl.client.CloseableHttpClient;
import packager.JarPackager;
import utility.FileGatherer;

public class ProjectBuilderAssembler {
    private Logger logger;
    private String cacheDirectory;
    private CloseableHttpClient httpClient;
    private int cacheSize = -1;
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
    public ProjectBuilderAssembler cacheSize(int mbs) {
        this.cacheSize = mbs;
        return this;
    }
    public ProjectBuilderAssembler config(String path) {
        this.configFilePath = path;
        return this;
    }
    public ProjectBuilder assemble() throws IllegalStateException {
        if (!canAssemble()) throw new IllegalStateException(
                "Required field is missing"
        );
        return new ProjectBuilder(
                this.logger,
                new ClassCompiler(
                        this.logger,
                        new FileGatherer(this.logger, "java")
                ),
                new DependencyManager(
                        this.logger,
                        new DependencyDownloader(
                                this.logger,
                                new DependencyResponseProcessor(
                                        this.logger,
                                        this.cacheDirectory
                                ),
                                this.httpClient
                        ),
                        DependencyCacheProvider
                                .getInstance(this.logger, this.cacheSize)
                ),
                new JarPackager(this.logger)
        );
    }
    private boolean canAssemble() {
        return this.logger != null && this.cacheDirectory != null &&
                this.httpClient != null && this.cacheSize >= 0 &&
                this.configFilePath != null;
    }
}
