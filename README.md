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

[Maven-Central]: https://maven-badges.herokuapp.com/maven-central/io.github.lucasstarsz.slopeecs/slope-ecs "Slope ECS on Maven Central"
[Maven-Central-SVG]: https://img.shields.io/maven-central/v/io.github.lucasstarsz.slopeecs/slope-ecs?color=blue&label=Maven%20Central&labelColor=363e45&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAFG0lEQVRoQ9WYXWwUVRTH/2d2ty0VqjwQMBodtny0lbY0KE2IaAgkIA9oUCKK+uyDBqFVaam0pR/ypYQYEyXxxahgE0BjrEpEQzAaKBLkw35Qy/igiQ8k2JDCdmbuMefObi0EKTt3CuukzWy2e879/87537N3SrjN15XdxeylCBNf7qcwUkIFhVnoejGXdxezvK9cwE8RitZnD3HbAIbeL2ZKr84+4A+TBrmzNjuI2wLgfjCDXY8hAAwGMY0AZNuFWw7gbS5ld9owmIFEjOAp7SKodAcmrcvxDvy1YZZWPHG6D6lexkbKk71AmLQ2hwH+eKmErTwGxRmWJfa5+ipMWJjU0pOVK7L6sMn0GXyxnAfhwcoPAKTyFGPoNmQuBqZu7ctKU1YfDgtw8YVKvqgC8fkTGASCS0oDkCUgAczULdmJFz23BKB/ZTlbYpsCRlEB4ZKngi7EREFwv+ed7KyTKea4A/QurdS+l9+iO4BLvkpXPRBOVnC/973uUFpCBWVjpe5Fc9nKV7ASgBVXgBYdWEZ3wALsD8+G1hE68GYgfi5+iAumifcV4nkM1htXRMtGTkMQkOw4E1pH6MCxAE4kq1mRQsGUYPOWHT5JPUsqebRtZB/M+Oy0kQaj4BtBdCXnM8kxAcC8gS46u6CKkbaNWOiuAgt3d54wXt84wfUgjk2vDsSLZYjBSpYJRqYefTKREozJE2K4v+uYkQaj4P/qQAZAyp8A4I7M6wBKLgGo6jluvL5xgmshTiYXckoN68pLch+MmLwKfnQnMq/n/dZlvL5xgtEAx5OLOKVcxOHqSlvEUGIfCk49erE0yIMDZtYZly+yw/ajHINCDB7kPt85Skft6vSDS9CROBGqBo5GVrjIEklFvrcXaQDpANgFKA8EBQsq6IBejZEAoep8NBCRARy0l3JcTpvwIfeHnR/oiP0Ii3h5LxipjGonGuGRW+gbe1kaIOiAhzgssK5+BkC6k0+Eued/iqxwkSX6yl4+0oHFziE6ZC/hjH0CCIHxscD5MbI1IztOH0yuYldd1pWXKhdYEzCkUrr6AiHvZWAWOkdyD+CAvZITkPHpYbIVwwWVsU6wgTMghRaheuC73ALoTK7hKyqlqy8Ay50v6Qt7BYtdRu8Beb3Y+TZS8ZFYqMNercWK+CecA9Q5fRW77I5UPdOBZc7XkYuPBOBT+xkNkNAA+2mf/VR6dP5rH4F4zOnMTYC99rN62sge8JEn/6K6anSK+BXO5+MiPnQHGkobubW7WYvaPrOG73P/xNPOHvrEXsOjfZ9gwuO/d4yb+NAAErh1Tju/fqaets2s4TgrrO/fSR/bz4/M/tXOHi38o9JGfi4NO9ZTXJi/G1WnraSO475MHx9T3Iu4EC/Cuv5dOue7JQ085A/h1XNvG60xFpRR8s2z6ziP5fzjIUX5wXlHH/cZhVY+XulpMco/lngjC2n/l7Ww5w2irm87Nc+uTz8DM97o3ULtMzdw/bktuQvQMmcr+94gmnraqKmsmZVyR06c0oXG3vZxFx+6A5sqdvLw8N9IqGG09LVTXUkjW6TQ1j3+lrnWVkZVevOBNq47u5Gayrdx0+nXjHLdjN+v9xmjRTfOauC2vlajHGGFR/5AYyokbHzo6tWWNPGOnqbQ8WEFR7YHGire4tZTNf9fgKgqaJonVAVrK3fxjl/Whoo1FRyZhaIWEjZfTlQxrPhQ38Sryzbz3l835Qx41kKerGjnfafqs44zqfKNYv8BwCf2QO7de1QAAAAASUVORK5CYIIA

[JavaDoc]: https://javadoc.io/doc/io.github.lucasstarsz.slopeecs/slope-ecs "Slope ECS Documentation"
[JavaDoc-SVG]: https://javadoc.io/badge2/io.github.lucasstarsz.slopeecs/slope-ecs/Javadocs.svg?color=blue&labelColor=363e45&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFEklEQVR4Xu1bbXLbNhBdgFR6mLoWSSX2AfLVnqYZKW4mR8iHPT1NIjfJTH7Gqi1Rck5TkdjOgoJCyaQIkASoicxf8hDE7j487GIfaQYHfrEDjx/uAbhnwJ4iMOn38WQ+t85Q6wbq4vtvGOGj2dS6f9YN1AVgEkZ4cg/AgTJgEoYIwOBgGUD09xiDwfTG+ha1bsA0B0yCEIExwGQJp7e31v2zbsAYgDBCesYF/cnOXgFA1HcZ/F4BoILnjMFDB3tfMXMvGKCCd736nTNgcnyM4PnrNOFq3+fzUmcMyK961cpfhxEKRDiJZ6372/qEOllfBrQaSA6UnfmvowEiYul9HVtVY5wDkF/5B5xDcHN9x4ebaIApyoJgvRw6BSAfPALC6WyT0t+CADnjTgJ3XgXywTMh4NE83gC/q0rghAEbCa8gmVHvnxHePuW3c4J1APLBlSW8NUCWMv2uRGgdgCpqd7n6Tg5CVQBU3a8qY03vO2NAWX9/MAD4jEFU0OSYnAibrnbR8w4YkMlblQnwZ60CFNeuPj+fBFVSciGHd3IQ0mUBnf9PLTQ+nWwBZfQqjJAAKGp5r4IQGdvcja5aY+s5II860Z3+LqJ4V8nQKQAUPLW4QojC3r6LkugcAAJhGg2wsCSuJHGXPUEnAFCA8eAhbmsB9EYYuAcgEE7m7as/nSZBnUMMCSEkfemWwf7oEmXuRIT4/Pdai1nroV3BBC8vkXs+JMslLC7MnLrqh3haY+UDBQQwmL1/ZhST0eCywKO/PiOKFEjnm79/3sqcOozJj/kBAsDMwIdGzoYvL5WOASkKWJz/0Wi+ncwaXWIKCLc7qK78SYWAxYWeL7UdJqpvP8wYBzJO8s7iQo8JfQIREeiNEFDXwDngWjMGoDkFCog1VjU6+4T0LNmPz/Xs1wYgv1rHwzF6XiZmSuumF2NZoLSNEGFRM6GtGWAwRysAmMZra7wCwFkOsBVInXnDs3/kVqLdE2tuP9V91rFX+kwwGlMzB3PDEtjEiSZVqNUtoJIQneRmDQHoj8bo+T2Yvnm800dl04T2ebDtALBhIUtw9NmLSBOpDKk0Sb8Z94F4K2mzvrPJhzQRsPj7blmjPU8VYt6g/LYKgHI7evUFMU1qs1qWQiFKDzSD11/l/NO3Txr733gC3SiPXoyx5zG5xoIBcGRy1ecGCUvXlsk4ZwCYOOVy7D0ANtBWZaluZrbhU9mcVhiQb5Kk4dxRlwwKpPcE9D0kZYSsShRVASEoR5i11KbgWQGAnFAnM1OHNsYzDrN3T635aOUkuB2wFEi4B6QX7L6Y7ASpIdLp/BoBm3u4FrpSfOBUxgBiQwWmLce35zkafsTvmhpA7ZNgePZJdhuIDOJzM+nJVuBqXtpys3fmPmkzIJ/Y9im7B0PSIDlMa+YKLQD6ZGSld2zQh/uQpMvaAoYpK3598QF7ngfcW+WUkiRJW9SKInQ0+ogP/F+yhJaTre4EwjjQuz5MScVTV/ZrVfiAPpOTxZCOxeoGXz1Hc28rS1RKuQ/L5L/duqDhVtBigM5KHY/GSLqenHBV96W2pyWR0Vkgc4W6O3qGPpS81Uxqvw2pz/AkqKZ5oDUAdEBqa8zRnx+w1/Oz/3ZABMa92p1hawCQEkSr2GaC7A/HyDlRn/Y8vXXItpE6OZq+BClagNYAUJNTH6B+J8sEPI8BU5/EK9EDVSg/pJG1I7QVpEROFZdU4maCRxXrWgegyuC+3f8fARJ0X9/EzrEAAAAASUVORK5CYIIA

[MIT-License]: LICENSE.txt "MIT Licensing"

[Terminals Are Different]: https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad "Terminals Access Files Differently"