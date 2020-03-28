# Google BigQuery Schema Generator Gradle Plugin

Gradle plugin for generating BQ schemas in json format given a Java POJO class

## Status

[![CircleCI](https://circleci.com/gh/fjbecerra/bigquery-schema-generator-gradle-plugin.svg?style=svg)](https://circleci.com/gh/fjbecerra/bigquery-schema-generator-gradle-plugin)

## Usage

**Attributes** | *Description*
--- | ---
classNameList | List of Classes name
outputName | Folder where Bigquery JSONs schemas are created.
jarLocation (Optional) | Location of the jar containing POJO classes, default gradle build location.
                           
Example.

```
bqSchemaGenerator.dependsOn build

BqSchemaGeneratorPlugin {
    classNameList = ["com.pojos.BqPojo", "com.pojos.BqPojo2"]
    outputName = "schema"
}
```

## More Information

https://plugins.gradle.org/plugin/com.pakius.bqSchemaGenerator
