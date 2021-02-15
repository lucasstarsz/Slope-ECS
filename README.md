# Slope ECS
[![Windows Build Status][Windows-Build-SVG]][Windows-Build-Action]
[![Ubuntu Build Status][Ubuntu-Build-SVG]][Ubuntu-Build-Action]
[![Mac OS Build Status][MacOS-Build-SVG]][MacOS-Build-Action]

[![javadoc][Javadoc-SVG]][Javadoc]
[![maven central][Maven-Central-SVG]][Maven-Central]

Slope is an Entity Component System written entirely in Java.

This repository is licensed under the [MIT License][MIT-License].

## Adding to your project
- **Maven**
```xml
<dependency>
  <groupId>io.github.lucasstarsz</groupId>
  <artifactId>slope-ecs</artifactId>
  <version>[Latest Version here]</version>
</dependency>
```
- **Gradle**
    - Groovy:
    ```groovy
    implementation 'io.github.lucasstarsz:slope-ecs:[Latest Version here]'
    ```
    - Kotlin:
    ```kotlin
    implementation("io.github.lucasstarsz:slope-ecs:[Latest Version here]")
    ```

## How to use
Check out [these examples](examples/).


[Windows-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Windows "Actions: Windows Build"
[Windows-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-Windows/badge.svg

[Ubuntu-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Ubuntu "Actions: Ubuntu Build"
[Ubuntu-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-Ubuntu/badge.svg

[MacOS-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-MacOS "Actions: Mac OS Build"
[MacOS-Build-SVG]: https://github.com/lucasstarsz/Slope-ECS/workflows/Build-MacOS/badge.svg

[Maven-Central]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz/slope-ecs "Slope ECS on Maven Central"
[Maven-Central-SVG]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz/slope-ecs/badge.svg

[Javadoc]: https://javadoc.io/doc/io.github.lucasstarsz/slope-ecs "Slope ECS Documentation"
[Javadoc-SVG]: https://javadoc.io/badge2/io.github.lucasstarsz/slope-ecs/javadoc.svg

[MIT-License]: LICENSE.txt "MIT Licensing"
