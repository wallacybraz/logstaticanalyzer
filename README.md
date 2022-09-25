# logstaticanalyzer
Java static analyzer for detecting logging related issues

It check java projects searching for a given set of commom logging issues. For now, only SLF4J is supported.

# build

This project use maven as it build tool.

## generate jar file

`mvn clean package`

# Use

`java -jar logseer-1.0.0-jar-with-dependencies.jar --path=<project_dir> --output="<report_output_dir | file_dir>" --libraries="slf4j"`


