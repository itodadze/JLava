package dependency;

import logger.Logger;

import java.io.File;

public class DependencyCacheManager {
    private final Logger logger;
    private final String dependencyDirectory;
    public DependencyCacheManager(Logger logger, String dependencyDirectory) {
        this.logger = logger;
        this.dependencyDirectory = dependencyDirectory;
    }

    public boolean isCached(String dependency) {
        return (new File(this.dependencyDirectory + "/" + dependency + ".jar")).exists();
    }

    public String getPath(String dependency) {
        return this.dependencyDirectory + "/" + dependency + ".jar";
    }
}
