# Install and Play the Game

## Terminology

* Server: The one that is playing as the Enemy AI in pokemon
* Client: The one that is playing the game.

## Downloading

Download the latest release of this project.\
The zip contain:
* **PokecrystalPvP.jar:** The java executable that will be used by the Server.
* **Pokemon - Crystal.gba:** The modified ROM of pokemon crystal.

## Playing

The Server needs to have the port 8765 opened, and need to run the executable jar.\
The Client needs to run the game with bgb emulator, and link it to the ip of the Server.

When the Client starts a wild battle or a trainer battle, the data will be sent to the Server and the battle will be start for both players.\
The Server can perform this actions:
* Choose 1 from the moveset of the pokemon.
* Choose to flee if it is a wild battle.
* Choose to switch of to use an item if it is a trainer battle.