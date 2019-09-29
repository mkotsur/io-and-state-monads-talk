# Coding exercise for Embracing The Chaos with State and IO monads

Sides can be found [here](https://www.dropbox.com/s/legjh03g3i8sdga/EmbracingTheChaos.pdf?dl=1)

## Structure of the project

There are 3 modules: 
* `naive` contains two "naive" solutions to the problem. The functions in that module fail to comply with the referential transparency principle.
* `cats` contains `Bike3` with TODOs and `Bike3Final` - the version with all implemented methods. 
* `constants` contains stuff shared between `naive` and `cats`.

How to run code:

* `mill naive.run1` or
* `mill naive.run3` or
* `mill cats.run3` or
* `mill cats.run3Final`

## TODO:

* Add an example of debugging with IO Monad;
* Check Bike/Bicycle renaming;
* List all the other tools in `cats-effect`
* Explain how state flatmaps