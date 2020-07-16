# Google BigQuery Schema Generator Gradle Plugin

Gradle plugin for generating BQ schemas in json format given a Java POJO class

## Status

[![CircleCI](https://circleci.com/gh/fjbecerra/bigquery-schema-generator-gradle-plugin.svg?style=svg)](https://circleci.com/gh/fjbecerra/bigquery-schema-generator-gradle-plugin)

## Configuration

Using the plugins DSL:

```
plugins {
  id "com.pakius.bqSchemaGenerator" version "1.1.0"
}

```

Using legacy plugin application:

```

buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.pakius:bq-schema-generator-plugin:1.1.0"
  }
}

apply plugin: "com.pakius.bqSchemaGenerator"

```

## Usage

**Attributes** | *Description*
--- | ---
classNameList | List of Classes name
output | Folder where Bigquery JSONs schemas are created.
jarLocation (Optional) | Location of the jar containing POJO classes, default gradle build location.
   
**Task:** bqSchemaGenerator  
                           
Example 1.

```
bqSchemaGenerator.dependsOn build

BqSchemaGeneratorPlugin {
    classNameList = ["com.pojos.BqPojo", "com.pojos.BqPojo2"]
    output = "schema"
}
```

Example 2.

```
bqSchemaGenerator.dependsOn build

BqSchemaGeneratorPlugin {
    classNameList = ["com.pojos.BqPojo", "com.pojos.BqPojo2"]
    output = "schema"
    jarLocation = "/home/pojos-1.0.0.jar"
}

```

`./gradlew :example:bqSchemaGenerator`

## More Information

https://plugins.gradle.org/plugin/com.pakius.bqSchemaGenerator
