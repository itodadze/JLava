package dependency;

public enum RepositoryCatalog {
    MAVEN_CENTRAL("https://repo.maven.apache.org/maven2"),
    J_CENTER("https://jcenter.bintray.com"),
    JIT_PACK("https://jitpack.io"),
    MAVEN_GOOGLE("https://maven.google.com"),
    APACHE("https://repository.apache.org"),
    SPRING("https://repo.spring.io/release"),
    GUAVA_GOOGLE("https://search.maven.org/remotecontent?filepath=com/google/guava/");

    private final String url;
    public String url() { return this.url; }
    RepositoryCatalog(String url) { this.url = url; }
}
