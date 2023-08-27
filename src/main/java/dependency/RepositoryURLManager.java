package dependency;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A class responsible for managing repositories of dependencies.
 */
public class RepositoryURLManager {

    private final List<String> urls;

    /**
     * Constructs an instance of the RepositoryURLManager.
     *
     * @param repositories  the repositories ordered by priority.
     */
    public RepositoryURLManager(List<String> repositories) {
        this.urls = repositories.stream().map(it -> {
            try {
                return RepositoryCatalog.valueOf(it).url();
            } catch (IllegalArgumentException e) {
                return it;
            }
        }).toList();
    }

    /**
     * Applies the given function to each repository and then returns the first element.
     *
     * @param function a function which takes the repository as an argument and returns
     *                 a stream of elements of the desired type.
     * @return         the first element in the stream after applying function.
     * @param <T>      the desired type.
     */
    public <T> Optional<T> firstSatisfying(Function<? super String, ? extends Stream<? extends T>> function) {
        return this.urls.stream().flatMap(function).findFirst();
    }

}
