# Liquigraph Gradle Plugin

## Overview

## Usage

1. Build project `gradle build install`
2. Add the plugin to your own build.gradle
3. Create a task derived from :
    + `org.liquigraph.gradle.LiquigraphTaskDryRun` if you need to test your changelog
    + `org.liquigraph.gradle.LiquigraphTaskRun` to execute your changelog

4. Set the appropriate parameters
5. **You have to copy your changelog into the build directory**

## Parameters
Please refer to [Liquigraph documentation](https://liquigraph.github.io/) for details of options and change log syntax.

| Name      | Description         | 
|-----------|---------------------|
| changelog | path of changelog   |
| jdbcUri   | neo4j connection URI|
| username  | neo4j username      |
| password  | neo4j password      |

## Example

gradle.build
```
buildscript {
    repositories {
    	// Use 'jcenter' for resolving your dependencies.
    	// You can declare any Maven/Ivy/file repository here.
        jcenter()
    	mavenLocal()
    }
    dependencies {
        classpath group: 'org.liquigraph',
		  name: 'liquigraph-gradle-plugin',
                  version: '1.0.0-SNAPSHOT'
    }
}
apply plugin: 'org.liquigraph.gradle'

task dryRunMigrations(type: org.liquigraph.gradle.LiquigraphTaskDryRun) {
    changelog = "changelog.xml"
    jdbcUri = "jdbc:neo4j://localhost:7474"
    username = "neo4j"
    password = "changeme"
}
```
