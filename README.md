# Description
A twist on a classic Connect Four game

# Core Concepts

List the four core concepts, the features they implement, and why each feature
is an appropriate use of the concept. Incorporate the feedback you got after
submitting your proposal.

  1. 2-D Arrays. A standard Connect Four board consists of a 6 by 7 slot board (that is, 6 rows and 
  7 columns). I will be representing this game board using a 2-D Array of integers to store the 
  state of each of the 42 slots of the board. A 0 will represent an empty slot, a 1 will represent 
  one player and a 2 will represent a second player. Colors will need to be placed in each slot to 
  indicate a placement (eg. red and yellow, or red and blue, etc.). Further, I will need to keep 
  track and cover the cases when the desired row or column are edge cases or out of bounds (ie. a 
  fully-filled column or an out-of-bounds column shouldn’t allow any placement, etc.). At the 
  beginning of the game, every slot of the game board will be blank, meaning the 2-D array will be 
  initialized to all zeros. 

  2. Collections. In developing this game, I will be storing each of the moves that occur in the 
  game in an ordered collection of some sort so that the user can press an undo button and remove 
  the last move performed by the last player. That way, if a player misclicks or wants to pursue a 
  different strategy, they are able to do so without needing to begin a brand new game. I will be 
  storing each move made in a LinkedList because order is important in this case and I only ever 
  need to add and remove off of one end of the list (ie. the head). Whenever the undo function is 
  invoked, the last move will be popped off and the slot on the board will be emptied. The 
  collection will store entries in the form of a coordinate pair (x,y), for which a new type may 
  need to be defined.

  3. File I/O. This version of Connect Four will utilize an I/O to store the state of the game such 
  that a user can press the save button and return to the game on a later occasion without the need 
  to begin a new game from scratch or leave a previous game unfinished. In particular, the state of 
  the game board and which player’s turn will be stored in a text file when the save functionality 
  is invoked. Then, whenever a player wants to load in a saved game, the program will read a text 
  file as an input and load the data contained therein to initialize the gameboard to replicate the 
  state of the saved text file. If the file doesn’t exist/is unreadable by the game, appropriate 
  error messages should be displayed, and/or a new, blank game is initialized instead.

  4. Testable. There are several testable case for each aspect of my game. (1) I will be 
  constructing methods that take in a coordinate pair as a parameter and update the game board 
  accordingly (ie. make sure only the bottom-most slot becomes occupied). (2) I will be constructing
  methods that determine whether a winner is found horizontally, vertically, or diagonally. (3) I 
  will be covering edge cases such as when a user places a piece in a column that is already fully 
  filled in, or in a nonexistent column, etc. (4) I will be implementing a collection of correctly 
  store, in some order, a list of moves performed. Each of these can be testable via JUnit to ensure
  working order and coverage of test cases.

# My Implementation

Provide an overview of each of the classes in your code, and what their
function is in the overall game.
  
  1. ConnectFour.java: This class provides the core logic for the game's functionality, including
  how to place pieces (+ simulate gravity), how to track which player's turn it is, and laying out
  the logic for the win conditions. It also contains methods that "do the work" of the buttons. This
  class manages the state for key components of the game, such as the current player turn the 
  winner of the game, etc.
  
  2. FileLineIterator.java: This class provides a wrapper around an iterator such that it can read
  a file line-by-line, which is integral to the save/load game functionality. Whenever a user loads
  a new game, this class helps the ConnectFour class pre-populate the game board.
  
  3. Game.java: This class provides the visual and controllable aspects of the game. It consists of
  rendering a visual user interface using Java Swing, such as rendering a home screen with different
  button options, an instructions page consisting of text and a back button, a new game screen that 
  is just a blank, new game board, and a load game screen that includes a text input options for the
  file path and the resulting partially-completed game board that is generated from the text file.
  This class implements the action listeners for each button click and redirects them to a new
  method in GameBoard to modify the game state.
  
  4. GameBoard.java: This class keeps track of the game state in a visual format is provides the 
  interface by which the users can interact with the game. It contains the main 2D array game board
  as well as the pieces and the status bar to indicate the status of the gameplay. Also, it includes
  the action listeners for the mouse clicks on each column and helps modify the pieces accordingly.
  The game state can be modified by adding a piece, undoing a move, saving the state, or loading an 
  existing game and pre-populating the game board.

Were there any significant stumbling blocks while you were implementing your
game (related to your design, or otherwise)?
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Not really, implementation went relatively smoothly. The only thing that required a little more 
  effort was coordinating the game saving and loading an existing game functionalities, since both 
  needed very strict criteria in order to function properly. The save needed to save the list of 
  moves up until that point in an orderly manner, and cannot function if the game is over, and the
  file name needed to be distinct each time, and needed to override existing file names/data 
  whenever the game/home screen was re-launched. The load existing game functionality needed to make
  the right moves in the right order, while updating the status indicated on the game board and 
  keeping track of the current player. Further, exceptions needed to be handled here, which added
  another layer of complexity.

Evaluate your design. Is there a good separation of functionality? How well is
private state encapsulated? What would you refactor, if given the chance?
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Based on the above description of each class, I think this design represents a good separation of
  functionality. The Game Class implements functionality for all of the tangential components of the
  game, such as the pop up window frames, screens for user interaction, and button functionality. 
  All calls from the Game Class is made to the GameBoard Class. The GameBoard class provides the 
  ways to interact and manipulate the underlying game state, either by controllers such as mouse 
  clicks or by altering the display of the game board in response to some change in state. All calls
  from the GameBoard Class are made to either the FileLineIterator Class (once) or to the underlying
  model of the game state within the ConnectFour Class. The ConnectFour Class represents the 
  underlying model and contains the logic of the game and it is disconnected from but communicates 
  with the viewer/controller functionalities. Lastly, the FileLineIterator is a helper wrapper 
  supporting the File IO functionality of the game. 
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The private states are well encapsulated, as there isn't any way for the classes to 
  modify each other's private states, especially the private states relating to critical game states
  and logic components within the ConnectFour Class. Whenever the underlying state of the game in 
  the ConnectFour Class needs to be accessed, say, by the GameBoard Class for file saving purposes,
  the GameBoard Class only receives a clone of the list of moves made, for example, so there is no 
  way for the methods within GameBoard (or any other class, for that matter), to directly alter or 
  manipulate the state of the game in an unintended or malicious manner. Because the private states
  of the game model are insulated from outside manipulation, these states are well encapsulated. 
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Around halway through the project, I realized the methods to modify the internal state of the 
  game with respect to saving the current game state and pre-populating the game state with a loaded
  partially-complete game were both being run in the GameBoard Class, meaning I needed to access the
  list of move being held in the ConnectFour Class. This disrupted the chain of command, since 
  classes are now reaching into each other's domains. Thus, I decided to refactor my code and have 
  a helper function in GameBoard call a class in ConnectFour that actually does the altering of the
  internal game state (either saving a copy/clone of the list of moves to a text file or 
  pre-populating the current board with a list of moves from a text file). Through this refactoring,
  I was able to keep the responsibilities of each class distinct and separated, preventing any other
  encapsualtion related issues.

# External Resources

Cite any external resources (libraries, images, tutorials, etc.) that you may
have used while implementing your game.
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; I didn't use any external resources to construct this game. I looked at some code from earlier 
  homework assignments for inspiration; for example, the file path input was inspired from the 
  Pennstagram assignment, and the save to file option was inspired from the TwitterBot account.
