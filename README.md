# Slope ECS
[![Windows Build Status][Windows-Build-SVG]][Windows-Build-Action]
[![Ubuntu Build Status][Ubuntu-Build-SVG]][Ubuntu-Build-Action]
[![Mac OS Build Status][MacOS-Build-SVG]][MacOS-Build-Action]

[![Javadoc][JavaDoc-SVG]][JavaDoc]
[![Maven Central][Maven-Central-SVG]][Maven-Central]

Slope is an Entity Component System written entirely in Java.

This repository is licensed under the [MIT License][MIT-License].


## Adding Slope-ECS to your project
This ECS can be found on [Maven Central][Maven-Central].

### Adding the Dependency
When adding the dependency, **make sure to replace `[latest version here]` with the actual latest version** (you'll find this in the Maven Central link up above).

A few common dependencies are provided below:

- **Maven**
  ```xml
  <dependency>
    <groupId>io.github.lucasstarsz</groupId>
    <artifactId>slope-ecs</artifactId>
    <version>[latest version here]</version>
  </dependency>
  ```
- **Gradle**
    - Groovy:
      ```groovy
      implementation 'io.github.lucasstarsz:slope-ecs:[latest version here]'
      ```
    - Kotlin:
      ```kotlin
      implementation("io.github.lucasstarsz:slope-ecs:[latest version here]")
      ```
- **Apache Ivy**
  ```xml
  <dependency org="io.github.lucasstarsz" name="slope-ecs" rev="[latest version here]" />
  ```

### Using Slope-ECS

Slope-ECS requires Java 15 or later to run.

You'll also want to check out [the examples](src/examples).


## Building Slope-ECS for yourself
Building Slope-ECS is a generally simple task. You just need to follow these steps:

- Clone the Slope-ECS repository.
  ```bash
  git clone https://github.com/lucasstarsz/Slope-ECS.git
  ```
- (Optional) Switch to the latest release branch.
  ```bash
  git switch vX.Y.Z
  ```
- Build the project.
  ```bash
  ./gradlew build
  ```
  Having trouble using `gradlew`? Read [this][Terminals Are Different].

From here, you have successfully built Slope-ECS.


[Windows-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Windows "Windows Build Status"
[Windows-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-Windows/badge.svg

[Ubuntu-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Ubuntu "Ubuntu Build Status"
[Ubuntu-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-Ubuntu/badge.svg

[MacOS-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-MacOS "Mac OS Build Status"
[MacOS-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-MacOS/badge.svg

[Maven-Central]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz/slope-ecs "Slope ECS on Maven Central"
[Maven-Central-SVG]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz/slope-ecs/badge.svg

[JavaDoc]: https://javadoc.io/doc/io.github.lucasstarsz/slope-ecs "Slope ECS Documentation"
[JavaDoc-SVG]: https://javadoc.io/badge2/io.github.lucasstarsz/slope-ecs/javadoc.svg

[MIT-License]: LICENSE.txt "MIT Licensing"

[Terminals Are Different]: https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad "Terminals Access Files Differently"
