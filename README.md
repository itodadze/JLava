# JLava

A simple and lightweight build automation tool for Java projects. JLava makes it easier to manage your dependencies, compile classes, and package them into JAR files.

#### Project JAR files

- [JLava-1.0.0](https://www.dropbox.com/scl/fi/vlcyelvsyvrwnevhvs1is/jlava.jar?rlkey=yjzwksqys4canxcp3u8owmltv&dl=0)

### Getting Started

1. Create a separate folder for JLava.
2. Download the JLava JAR file into the folder.

### Usage

1. Open the terminal in JLava's folder.
2. Use the following command: 
``` bash
java -jar jlava.jar -c path/to/jlava-config.json
```

### Additional Information

- An example of a configuration file:
``` json
{
  "name": "name.of.project",
  "sourceDirs": ["path/to/src1", "path/to/src2"],
  "outputDir": "path/to/output",
  "repositories": ["repo1", "repo2", "repo3"],
  "dependencies": ["dependency1", "dependency2"]
}
```
- If you provide no repositories, JLava will automatically use Maven (Central).
- JLava provides shortcuts for some common dependency repositories which you can find in RepositoryCatalog.

## License

This project is licensed under [MIT LICENSE](https://github.com/itodadze/JLava/blob/main/LICENSE).
