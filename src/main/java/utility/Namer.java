package utility;

public enum Namer {
    DIRECTORY(input -> input.replace('/', '-').replace(':', '-')),
    JAR(input -> input.replace('/', '.') + ".jar");

    public String name(String initialName) { return this.strategy.apply(initialName); }
    private final NamingStrategy strategy;
    Namer(NamingStrategy strategy) {
        this.strategy = strategy;
    }
}
