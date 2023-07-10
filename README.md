# LP-Engine
LP-Engine is very basic 2D engine made in Java with Java-FX using Maven. It was made as a school assignment and it was my first java project, so it is very clunky.

The engine is made for 2D games with platformer overworld and turn based combat and comes with small demo game to check it out.

## How to run it
Firstly, project is poorly managed in Maven. To run it you first need to run clean, compile and then javafx:run. If that doesn't work, then I wish you best of luck adventurer. If you don't want to go through all the trouble I have added an executable JAR file to run it `LPEngine.jar`

If you managed to start the silly little game, you can move around with W, A, S, D and pick up items with E. If you bump into an enemy you begin a turn based combat.

## Engine usage
Game objects are in `game` package, there you can implement your objects or mess with mine. 
To put objects into the game you need to add it to `LevelManager.generateLevel()` in `engine.logic` package. 
Engine used to have functional loading from Json files, but alas, it somehow no longer works and I have no plan of investigating why.

For any bigger changes you need to figure it out yourself, hopefully the documentation is decent enough.

## How does the catastrophy looks
.![Screenshot](https://github.com/TerryBool/LP-Engine/assets/139129641/105c7af5-fb8f-434d-a32c-928b9a9f59bb)
