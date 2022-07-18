# Player Enemy AI

## The Project:

The idea of this program is to receive data from a modified rom of pokemon crystal during a battle, and send back the
action that the AI should do.
It's like a pvp pokemon, in which one il playing the regular game, and the other controls the AI in battle.

### Features:

* Connect to the game via bgb emulator.
* Receive battle data, and send back an action from:
    * A Move (Chosen from the Enemy Pokemon Moves).
    * Switch to another pokemon in the Enemy Party (only if in Trainer Battle).
    * Flee from Player (Only if in a Wild Battle).

### Future Feature:

* Use items of the Trainer (only if in Trainer Battle).

## How does it Work:

### The Game:

The Game is modifies for receiving and sending data via cable link during a battle (
see [Byte Received From Game](<#Byte Received From Game>))\
The client program is projected for receiving the battle data/battle update, and send back when ready a byte that
represents the action that the AI will perform during the battle.\
The files edited from the disassembled project of pokemon crystal are:

* **engine\battle\core.asm:**
    * All the feature for a linked battle are removed for gaining free space.
    * All the internal/external clock check are removed. We use only the internal clock.
* **home\serial.asm:**
    * All the Serial Interrupt handler were rewritten. Now, it only checks for a client connection with the program, and
      if the client is connected control the byte received.

### The Serial Interrupt Handler:

The Serial Interrupt Handler of the game now check the byte received from the client and, based on this, control the
state of connection and the byte received.\
Generally:

* if the byte send by the game is a byte 0xff, then it means that
  the connection is interrupted and the battle will start/continue as a normal battle against the game AI.
* If we are already in a battle and connected to the client, the handler check if the byte is a byte 0x80. If it is,
  then the Handler resend back the original byte and wait a valid action.

## Byte Received From Game

**Note that all byte code are unsigned**

* ### 0x01: Battle Started
  Tell that a battle is starting.  
  After this, the game will send a byte for the BattleMode {0: No Battle, 1: Wild, 2: Trainer}  
  Expecting the same back.  
  And Than Send:
    * **[WildMonData](#WildMonData)** for a wild battle
    * **[EnemyTrainerData](#EnemyTrainerData)** for a trainer battle  
      Plus the **[TrainerData](#TrainerData)**
* ### 0x02: Battle Update
  Send the battle data at the end of a turn:
    * **[WildMonData](#WildMonData)** for a wild battle
    * **[EnemyTrainerData](#EnemyTrainerData)** for a trainer battle  
      Plus the **[TrainerData](#TrainerData)**
* ### 0x04: Waiting Action
  Send a control byte that wants back a **BattleActionCode**.
* ### 0x08: Waiting Switch
  This byte is sent only during a trainer battle, and indicates the moment when the enemy trainer need to switch pokemon
  because the one active is fainted. It wants back the index of the pokemon that need to enter the battle (0-5).
* ### 0x80: Battle Ended
  The game sends a notice that tells that the battle is ended.
  ## WildMonData
  An array of 20 byte representing a Wild Pokemon.  
  The Array Data:
    * byte 0: The Pokemon Species
    * byte 1: The Pokemon Item
    * byte 2-5: The Pokemon Moves
    * byte 6-7: The Pokemon DV
    * byte 8-11: The Pokemon Move PPs
    * byte 12: The Pokemon Happiness
    * byte 13: The Pokemon level
    * byte 14-15: The Pokemon Status
    * byte 16-17: The Pokemon HP
    * byte 18-19: The Pokemon MAX HP
  ## EnemyTrainerData
    * byte 0: The Active Enemy Pokemon in Battle (0 - 5)
    * byte 1-2: The Active Enemy Pokemon HP
    * byte 3: The Enemy trainer first item
    * byte 4: The Enemy trainer second item
    * byte [5-21][22-38][39-55][56-72][74-89][90-107]:
        * byte 0: The Pokemon Species
        * byte 1: The Pokemon Item
        * byte 2-5: The Pokemon Moves
        * byte 6-9: The Pokemon PPs
        * byte 10: The Pokemon Level
        * byte 11-12: The Pokemon Status
        * byte 13-14: The Pokemon HP
        * byte 15-16: The Pokemon MAX HP
  ## TrainerData
    * byte 0: Active Pokemon Species
    * byte 1-2: Active Pokemon HP
    * byte 3-4: Active Pokemon MAX HP

# Byte Expected From Game

* ### 0x01: Dummy Byte
  Sent back if no particular byte is expected back.
* ### BattleActionCode
    * **0x80: Action not Prepared:**  
      If the action is not ready, a value of 0x80 is expected back.
    * **0x00: First Move Selected.**
    * **0x01: Second Move Selected.**
    * **0x02: Third Move Selected.**
    * **0x03: Fourth Move Selected.**
    * **0x04: Wild Pokemon Fled:**  
      This Value is invalid in a trainer battle.
    * **0x05: Switch with First Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x06: Switch with Second Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x07: Switch with Third Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x08: Switch with Fourth Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x09: Switch with Fifth Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x0A: Switch with Sixth Pokemon:**  
      This Value is invalid in a wild battle.
    * **0x0B: Use First Item on Active Pokemon:**\
      This Value is invalid in a wild battle.\
      This Value is invalid if the Enemy Trainer has no item at slot 1.
    * **0x0C: Use Second Item on Active Pokemon:**\
      This Value is invalid in a wild battle.\
      This Value is invalid if the Enemy Trainer has no item at slot 2.
* ### 0xff: Connection Interrupted
  Sent back if a connection is no longer available.
