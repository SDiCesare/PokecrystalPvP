# PokecrystalPvP

This project was realized thanks to the repository [pret/pokecrystal](https://github.com/pret/pokecrystal) which contains the pokemon crystal didassembly.

## The Project

The idea of this program is to receive data from a modified rom of pokemon crystal during a battle, and send back the
action that the AI should do, like a player vs. player pokemon.

### Features:

* Connect to the game via bgb emulator.
* Receive battle data, and send back an action from:
    * A Move (Chosen from the Enemy Pokemon Moves).
    * Switch to another pokemon in the Enemy Party (only if in Trainer Battle).
    * Flee from Player (Only if in a Wild Battle).

### Future Feature:

* Decode and Print Pokemon statuses.
* Print selected choice of Enemy.
* Possibility to change Enemy move after selected but before the player choice.
* Print Pokemon levels.

## Playing the Game

For playing the game with your friend, pls see [INSTALL.MD](https://github.com/SDiCesare/PokecrystalPvP/blob/main/INSTALL.md)

## How does it Work

### The Game:

The Game is modifies for receiving and sending data via cable link during a battle
(see [Battle Protocol.MD](https://github.com/SDiCesare/PokecrystalPvP/blob/main/ServerProject/out/production/ServerProject/com/ike/enemyai/Battle%20Protocol.MD))\
The client program is projected for receiving the battle data/battle update, and send back when ready a byte that
represents the action that the AI will perform during the battle.\
The files edited from the disassembled project of pokemon crystal are:

* **engine\battle\core.asm:**
    * All the feature for a linked battle are removed for gaining free space.
    * All the internal/external clock check are removed. We use only the internal clock.
* **home\serial.asm:**
    * All the Serial Interrupt handler were rewritten. Now, it only checks for a client connection with the program, and
      if the client is connected control the byte received.
* **engine\battle\ai\move.asm:**\
  The choice of a move by the game AI is skipped is a client is connected.
* **engine\battle\ai\items.asm:**\
  If a client is connected, the battle action received is decoded, and eventually it performs a switch or use an item.

### The Serial Interrupt Handler:

The Serial Interrupt Handler of the game now check the byte received from the client and, based on this, control the
state of connection and the byte received.\
Generally:

* if the byte send by the game is a byte 0xff, then it means that
  the connection is interrupted and the battle will start/continue as a normal battle against the game AI.
* If we are already in a battle and connected to the client, the handler check if the byte is a byte 0x80. If it is,
  then the Handler resend back the original byte and wait a valid action.
