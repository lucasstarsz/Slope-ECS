# Slope-ECS Examples
This is the 'examples' section of Slope-ECS.

## How to Run
- Each example is listed in the `examples` task, which you call like so: `./gradlew examples`. That task prints out each example program available.

- To run that program, call the name of the task. For example:
    ```bash
    F:\Slope-ECS>./gradlew examples
    
    Example Tasks
    -------------
    GuessingGameExample: An example usage of Slope-ECS: a guessing game.
    
    F:\Slope-ECS>./gradlew GuessingGameExample
    # guessing game is being run!
    ```
  _Having trouble using `gradlew`? Read [this][Terminals Are Different]._

## Examples
- [Guessing Game][Guessing Game Package]

## Adding An Example
Details are still TBD, but if you would like to create an example you can make a fork of the repository
and [open a pull request][Pull Request].


[Terminals Are Different]: https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad "Terminals Access Files Differently"
[Guessing Game Package]: java/examples/guessinggame "Slope-ECS: Guessing Game Example"
[Pull Request]: https://github.com/lucasstarsz/Slope-ECS/compare "Slope-ECS: Create Pull Request"
