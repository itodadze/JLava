package packager;

public interface Packager {
    void packageClasses(String name, String source, String directory) throws Exception;
}
