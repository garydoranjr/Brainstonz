Brainstonz
==========
An implementation of the board game Brainstonz in Java.

About
-----
Brainstonz is a game by McWiz that is similar to tic-tac-toe, except each player
gets two moves each turn, and can remove some of his opponent's pieces if he
places two of his pieces on spaces with matching symbols. See "Help" in the game
for more detailed rules on how to play.

The AI has also been implemented for the game. Since the game is so simple, the
entire game tree has been searched, and stored to a file for move lookup during
game-play. It turns out that like tic-tac-toe, optimal opponents will always
tie.  You can play against a computer of various skill levels, or watch the
computer play against itself (_War Games_ style).

Setup
-----
To build the game, just run `ant` in the top level of the repository. This
should create an executable JAR file (you may need to `chmod u+x` it).
