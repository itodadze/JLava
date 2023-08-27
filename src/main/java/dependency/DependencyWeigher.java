package dependency;

import com.github.benmanes.caffeine.cache.Weigher;
import org.checkerframework.checker.index.qual.NonNegative;

import java.io.File;

/**
 * A class responsible for providing the weight of the file in cache based on its size.
 */
public class DependencyWeigher implements Weigher<String, File> {
    private final static long BYTE_TO_MB = 1024 * 1024;
    @Override
    public @NonNegative int weigh(String s, File file) {
        return (int) (file.length() / BYTE_TO_MB) + 1;
    }
}
