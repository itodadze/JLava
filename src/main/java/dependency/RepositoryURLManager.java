package dependency;

import java.util.List;

public class RepositoryURLManager {

    private final List<String> urls;

    public RepositoryURLManager(List<String> repositories) {
        this.urls = repositories.stream().map(it -> {
            try {
                return RepositoryCatalog.valueOf(it).url();
            } catch (IllegalArgumentException e) {
                return it;
            }
        }).toList();
    }

    public List<String> getURLs() {
        return this.urls;
    }

}
