# Numerical X and O

- - -
**Name:** Oleksandr Kononov
**Student Number:** 20071032
**Course:** Entertainment Systems Year 2
**Subject:** Console Game Development

# Features Implmented

- - -

- Undo button:
    + It can be used repeatedly
    + The button changes colour when pressed down
    + If there are two human player, only undo's one previous move (if possible)
    + If there's one human player, undo's AI move and human move (if possible)

- Hint button:
    + It can be used repeatedly
    + The button changes colour when pressed down
    + Uses a temporary "hint player" to decided the next best move
    + Shows the hint on screen for 5 seconds, giving the number to use and row and column to place it in. _(Note: It counts from one not zero for rows and columns)_

- FirstSpacePlayer:
    + Places in the first available space with the first available number

- RandomSpacePlayer:
    + Places a random available number in a random available space

- MinimaxPlayer:
    + Uses the Minimax algorithm to find the next best poisition and number to use _(Note: Due to slow down, I limited Minimax skill to be no more than 5)_

- ImpactSpacePlayer:
    + Checks for the winning move with it's available numbers, if it exists, take it
    + Checks for other players winning move, if there is one, block it
    + If there is a corner taken, attempt to place a number in the opposite corner such that the center position requires a number of your player to make 15
    + If the above is not possible, place your largest number in the opposite corner
    + If all else fails, just place something anywhere that's free
    

