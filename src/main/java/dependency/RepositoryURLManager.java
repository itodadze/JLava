package dependency;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public <T> Optional<T> firstSatisfying(Function<? super String, ? extends Stream<? extends T>> function) {
        return this.urls.stream().flatMap(function).findFirst();
    }

}
