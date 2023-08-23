package dependency;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;

public class DependencyRemovalListener implements RemovalListener<String, File> {
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onRemoval(@Nullable String s, @Nullable File file, RemovalCause removalCause) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}
