# BQ SCHEMA GENERATOR

Utility tool to generate BQ schemas out of JAVA POJO classes.

## Usage

1. In the main class, add your custom POJO, to generate the schema.

2. Run the app via Gradle

`./gradlew:schema-generator:run`

3. Find the json schema generated under the `schemas` folder.

## Limitations

- Collections: Only supports parameterized **List** of one argument, ex. List<AnyClass>.