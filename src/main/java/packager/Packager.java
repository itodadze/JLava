package packager;

public interface Packager {
    /**
     * @param name          name of the package.
     * @param source        from which the files are taken for packaging.
     * @param directory     where the package is saved.
     * @throws Exception    if any error occurs while packaging (and in some cases
     *                      clearing the source directory).
     */
    void packageClasses(String name, String source, String directory) throws Exception;
}
