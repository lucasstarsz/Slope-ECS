<div align="center">

# Slope ECS

### Games, in Java. Crazy thought, isn't it?

[![Windows Build Status][Windows-Build-SVG]][Windows-Build-Action]
[![Ubuntu Build Status][Ubuntu-Build-SVG]][Ubuntu-Build-Action]
[![Mac OS Build Status][MacOS-Build-SVG]][MacOS-Build-Action]

[![Javadoc][JavaDoc-SVG]][Javadoc-Link]
[![Maven Central][Maven-Central-SVG]][Maven-Central]
</div>

## About Slope
Slope is a developer-friendly Entity Component System written in pure Java. It enables developers of all kinds to create games and other software built on a no-nonsense, cross-platform, open-source code library.

## Highlights
- [x] **Cross Platform** - Fully supports all major desktop platforms (Linux, macOS, Windows)
- [x] **Developer Friendly**
    - Robust testing for all supported platforms, ensuring your programs work as you expect.
    - Well-documented source code, ensuring each part of the library's purpose to be easily understood.
- [x] **Open Source**
    - Under the [MIT License][MIT-License], you're free to use and modify this library for your own needs.

## Adding Slope-ECS to your project
This repository can be found on [Maven Central][Maven-Central] -- as such, it can be easily added to your project as a dependency through the use of a build tool.

### Requirements
- [Java 15][AdoptOpenJDK-Link] or later.
    - **IMPORTANT**: The eventual target Java version will be Java 17.
- A build tool, such as [Maven][Maven-Link] or [Gradle][Gradle-Link].

### Adding the Dependency
When adding the dependency, **make sure to replace `[latest version here]` with the actual latest version** (you'll find this in the Maven Central link up above).

A few common dependencies are provided below:

- **Maven**
  ```xml
  <dependency>
    <groupId>io.github.lucasstarsz.slopeecs</groupId>
    <artifactId>slope-ecs</artifactId>
    <version>[latest version here]</version>
  </dependency>
  ```
- **Gradle**
    - Groovy:
      ```groovy
      implementation 'io.github.lucasstarsz.slopeecs:slope-ecs:[latest version here]'
      ```
    - Kotlin:
      ```kotlin
      implementation("io.github.lucasstarsz.slopeecs:slope-ecs:[latest version here]")
      ```
- **Apache Ivy**
  ```xml
  <dependency org="io.github.lucasstarsz.slopeecs" name="slope-ecs" rev="[latest version here]" />
  ```

## Learning Slope-ECS

### The Wiki
I highly recommend Slope's [wiki][Wiki-Link], as it will almost always contain the most up-to-date information on understanding how Slope-ECS works. It _is_ still a work in progress -- as I have more time to work on it, much more will be added to it.

### Code Examples
After getting an understanding of how Slope works, [these example programs](src/examples) are the best way to learn about using Slope in an actual project.

## Building the Source Code
Building Slope's source code is a very easy task. You just need to follow these steps:

1. If you don't already have it, download [Git][Git-Link]. It's a powerful source control tool, and is the same source control that Slope-ECS uses.
    - You don't need to have Gradle installed to build `Slope-ECS` -- Gradle projects come with the build tool.

2. Using Git, clone the Slope-ECS repository.
    ```bash
    git clone https://github.com/lucasstarsz/Slope-ECS.git
    ```

3. Once you've entered the top directory of the project (`cd Slope-ECS`), build the project using the `gradlew` file.
    - On Windows, you'll want to use the `gradlew.bat` file. 
    - For Unix-based systems, you'll need to use the `gradlew.sh` file (after giving it proper execution permissions).
    ```bash
    ./gradlew build
    ```
  _Having trouble accessing the `gradlew` file? Read [this][Terminals Are Different]._

## Legal Information
This repository is licensed under the [MIT License][MIT-License].


[Windows-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Windows "Windows Build Status"
[Windows-Build-SVG]: https://img.shields.io/github/workflow/status/lucasstarsz/Slope-ECS/Build-Windows?label=Windows%20Build&labelColor=363e45&logo=windows&logoColor=0078D6&style=for-the-badge

[Ubuntu-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-Ubuntu "Ubuntu Build Status"
[Ubuntu-Build-SVG]: https://img.shields.io/github/workflow/status/lucasstarsz/Slope-ECS/Build-Ubuntu?label=Ubuntu%20Build&labelColor=363e45&logo=ubuntu&logoColor=E95420&style=for-the-badge

[MacOS-Build-Action]: https://github.com/lucasstarsz/Slope-ECS/actions?query=workflow%3ABuild-MacOS "Mac OS Build Status"
[MacOS-Build-SVG]: https://img.shields.io/github/workflow/status/lucasstarsz/Slope-ECS/Build-MacOS?label=Mac%20OS%20Build&labelColor=363e45&logo=apple&logoColor=000000&style=for-the-badge

[Maven-Central]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz.slopeecs/slope-ecs "Slope ECS on Maven Central"
[Maven-Central-SVG]: https://img.shields.io/maven-central/v/io.github.lucasstarsz.slopeecs/slope-ecs?style=for-the-badge&color=blue&label=Maven%20Central&labelColor=363e45&logo=data:image/png;base64,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

[Javadoc-Link]: https://javadoc.io/doc/io.github.lucasstarsz.slopeecs/slope-ecs "Slope ECS Documentation"
[JavaDoc-SVG]: https://javadoc.io/badge2/io.github.lucasstarsz.slopeecs/slope-ecs/Javadocs.svg?style=for-the-badge&color=blue&labelColor=363e45&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFEklEQVR4Xu1bbXLbNhBdgFR6mLoWSSX2AfLVnqYZKW4mR8iHPT1NIjfJTH7Gqi1Rck5TkdjOgoJCyaQIkASoicxf8hDE7j487GIfaQYHfrEDjx/uAbhnwJ4iMOn38WQ+t85Q6wbq4vtvGOGj2dS6f9YN1AVgEkZ4cg/AgTJgEoYIwOBgGUD09xiDwfTG+ha1bsA0B0yCEIExwGQJp7e31v2zbsAYgDBCesYF/cnOXgFA1HcZ/F4BoILnjMFDB3tfMXMvGKCCd736nTNgcnyM4PnrNOFq3+fzUmcMyK961cpfhxEKRDiJZ6372/qEOllfBrQaSA6UnfmvowEiYul9HVtVY5wDkF/5B5xDcHN9x4ebaIApyoJgvRw6BSAfPALC6WyT0t+CADnjTgJ3XgXywTMh4NE83gC/q0rghAEbCa8gmVHvnxHePuW3c4J1APLBlSW8NUCWMv2uRGgdgCpqd7n6Tg5CVQBU3a8qY03vO2NAWX9/MAD4jEFU0OSYnAibrnbR8w4YkMlblQnwZ60CFNeuPj+fBFVSciGHd3IQ0mUBnf9PLTQ+nWwBZfQqjJAAKGp5r4IQGdvcja5aY+s5II860Z3+LqJ4V8nQKQAUPLW4QojC3r6LkugcAAJhGg2wsCSuJHGXPUEnAFCA8eAhbmsB9EYYuAcgEE7m7as/nSZBnUMMCSEkfemWwf7oEmXuRIT4/Pdai1nroV3BBC8vkXs+JMslLC7MnLrqh3haY+UDBQQwmL1/ZhST0eCywKO/PiOKFEjnm79/3sqcOozJj/kBAsDMwIdGzoYvL5WOASkKWJz/0Wi+ncwaXWIKCLc7qK78SYWAxYWeL7UdJqpvP8wYBzJO8s7iQo8JfQIREeiNEFDXwDngWjMGoDkFCog1VjU6+4T0LNmPz/Xs1wYgv1rHwzF6XiZmSuumF2NZoLSNEGFRM6GtGWAwRysAmMZra7wCwFkOsBVInXnDs3/kVqLdE2tuP9V91rFX+kwwGlMzB3PDEtjEiSZVqNUtoJIQneRmDQHoj8bo+T2Yvnm800dl04T2ebDtALBhIUtw9NmLSBOpDKk0Sb8Z94F4K2mzvrPJhzQRsPj7blmjPU8VYt6g/LYKgHI7evUFMU1qs1qWQiFKDzSD11/l/NO3Txr733gC3SiPXoyx5zG5xoIBcGRy1ecGCUvXlsk4ZwCYOOVy7D0ANtBWZaluZrbhU9mcVhiQb5Kk4dxRlwwKpPcE9D0kZYSsShRVASEoR5i11KbgWQGAnFAnM1OHNsYzDrN3T635aOUkuB2wFEi4B6QX7L6Y7ASpIdLp/BoBm3u4FrpSfOBUxgBiQwWmLce35zkafsTvmhpA7ZNgePZJdhuIDOJzM+nJVuBqXtpys3fmPmkzIJ/Y9im7B0PSIDlMa+YKLQD6ZGSld2zQh/uQpMvaAoYpK3598QF7ngfcW+WUkiRJW9SKInQ0+ogP/F+yhJaTre4EwjjQuz5MScVTV/ZrVfiAPpOTxZCOxeoGXz1Hc28rS1RKuQ/L5L/duqDhVtBigM5KHY/GSLqenHBV96W2pyWR0Vkgc4W6O3qGPpS81Uxqvw2pz/AkqKZ5oDUAdEBqa8zRnx+w1/Oz/3ZABMa92p1hawCQEkSr2GaC7A/HyDlRn/Y8vXXItpE6OZq+BClagNYAUJNTH6B+J8sEPI8BU5/EK9EDVSg/pJG1I7QVpEROFZdU4maCRxXrWgegyuC+3f8fARJ0X9/EzrEAAAAASUVORK5CYIIA

[AdoptOpenJDK-Link]: https://adoptopenjdk.net/ "Download Java from AdoptOpenJDK"
[Maven-Link]: https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html "Maven Build Tool"
[Gradle-Link]: https://docs.gradle.org/current/userguide/installation.html "Gradle Build Tool"
[Git-Link]: https://git-scm.com/ "Git Source Control Manager"
[Wiki-Link]: https://github.com/lucasstarsz/Slope-ECS/wiki "Slope-ECS Wiki"

[Terminals Are Different]: https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad "Terminals Access Files Differently"

[MIT-License]: LICENSE.txt "MIT Licensing"
