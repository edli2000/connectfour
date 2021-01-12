/**
 * CIS 120 HW09 - ConnectFour
 * University of Pennsylvania
 * Created by Edward Li in Fall 2020.
 */

/**
 * This class is a model for ConnectFour.  
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. 
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 */
import java.util.*;
import java.io.*;
import java.nio.file.Paths;

public class ConnectFour {

    private int[][] board;
    private int numTurns;
    private boolean player1;
    private boolean gameOver;
    private int saveStatus = 1;
    private LinkedList<String> moves = new LinkedList<String>();

    /**
     * Constructor sets up the game state.
     */
    public ConnectFour() {
        reset();
    }

    /**
     * playTurn allows players to play a turn. Returns true 
     * if the move is successful and false if a player tries
     * to play in a location that is taken or after the game
     * has ended. If the turn is successful and the game has 
     * not ended, the player is changed. If the turn is 
     * unsuccessful or the game has ended, the player is not 
     * changed.
     * 
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (board[r][c] != 0 || r > 5 || c > 6 || r < 0 || c < 0 || gameOver) {
            return false;
        }

        if (player1) {
            for (int i = 5; i >= 0; i--) {
                if (board[i][c] == 0) {
                    board[i][c] = 1;
                    moves.addLast("" + i + c);
                    numTurns++;
                    if (checkWinner() == 0) {
                        player1 = !player1;
                    }
                    return true;
                }
            }
        } else {
            for (int i = 5; i >= 0; i--) {
                if (board[i][c] == 0) {
                    board[i][c] = 2;
                    moves.addLast("" + i + c);
                    numTurns++;
                    if (checkWinner() == 0) {
                        player1 = !player1;
                    }
                    return true;
                } 
            }
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win 
     * condition. Checks for horizontal, vertical, and diagonal win conditions
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won, and
     *            2 if player 2 has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (vertical() != 0 || horizontal() != 0 || diagonal() != 0) {
            gameOver = true;
            if (player1) {
                return 1;
            } else {
                return 2;
            }
        }
        
        if (numTurns >= 42) {
            gameOver = true;
            return 3;
        } else {
            return 0;
        }
    }
    
    /**
     * undoLast() checks the last move made and resets that grid slot to 0.
     * If there is no valid move detected, a message is printed to the console
     * indicating there is no undo-able move. This method also decrements the 
     * number of turns made and reverses parity of the current player.
     * 
     * @return none.
     */
    public void undoLast() {
        try {
            String lastMove = moves.removeLast();
            if (lastMove != null && !gameOver) {
                char[] coords = new char[lastMove.length()];
                for (int i = 0; i < lastMove.length(); i++) {
                    coords[i] = lastMove.charAt(i);
                }
                int row = Integer.parseInt(String.valueOf(coords[0]));
                int col = Integer.parseInt(String.valueOf(coords[1]));
                board[row][col] = 0;
                numTurns--;
                player1 = !player1;
            }
        } catch (NoSuchElementException e) {
            System.out.println("No move to undo!");
        }
    }
    
    /**
     * getMoves() is a helper method that returns a clone of the internal list of 
     * moves made up until this point in the game.
     * 
     * @return a cloned LinkedList of the moves made in the game until this point.
     */
    @SuppressWarnings("unchecked")
    public LinkedList<String> getMoves() {
        return (LinkedList) moves.clone();
    }
    
    /**
     * saveToFile() takes a copy of the list of moves made and processes it into a 
     * text file as a snapshot of the progress of the current game (if the current
     * game is not over). If the game is over, nothing occurs. This should keep track
     * of the following information for each turn made: the row, the column, and the 
     * player making the move (color and location of each move). The name of the file
     * increments every invokation to prevent overwriting within a session; overwriting
     * should only occur if a brand new session begins (ie. rebooting the game). Relevant
     * exceptions should be caught.
     * 
     * @return none.
     */
    public void saveToFile() throws IOException {
        String path = "files/" + saveStatus + ".txt";
        BufferedWriter bw = null;
        LinkedList<String> movesToSave = getMoves();
        try {
            if (!gameOver && !movesToSave.isEmpty()) {
                PrintWriter pw = new PrintWriter(path);
                pw.close();
                File file = Paths.get(path).toFile();
                FileWriter fw = new FileWriter(file);
                boolean player = true;
                Iterator<String> it = movesToSave.iterator();
                bw = new BufferedWriter(fw);
                while (it.hasNext()) {
                    bw.write(it.next());
                    if (player) {
                        bw.write("  Player: 1");
                        player = !player;
                    } else {
                        bw.write("  Player: 2");
                        player = !player;
                    }
                    bw.flush();
                    if (it.hasNext()) {
                        bw.newLine();
                    }
                }
                saveStatus++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
            } catch (Exception e) {
                System.out.println("Error in closing BufferedWriter: " + e);
            }
        }
    }
    
    /**
     * readFromFile() takes in a string denoting the path of the save file and 
     * pre-populates the board accordingly. It performs validation on the file
     * path and game state to ensure the input is not illegal. It also uses
     * LineIterator as a helper to iterate through each line of the save 
     * file, representing each turn of the saved game. Relevant exceptions should
     * be caught.
     * 
     * @param a String denoting the path of the save file.
     * @return none.
     */
    public void readFromFile(String filePath) {
        LinkedList<String> loadedMoves = new LinkedList<String>();
        if (filePath == null || filePath == "" || (checkWinner() != 0)) {
            throw new IllegalArgumentException();
        }
        boolean firstPlayer = true;
        try {
            LineIterator li = new LineIterator(filePath);
            while (li.hasNext()) {
                String move = li.next();
                char[] coords = new char[move.length()];
                for (int i = 0; i < move.length(); i++) {
                    coords[i] = move.charAt(i);
                }
                int row = Integer.parseInt(String.valueOf(coords[0]));
                int col = Integer.parseInt(String.valueOf(coords[1]));
                playTurn(col, row);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error populating the board!");
            reset();
        }
    }
    
    /**
     * vertical() is a helper method that checks whether the game has reached a win 
     * condition in the vertical direction.
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won vertically, and
     *            2 if player 2 has won vertically
     */
    public int vertical() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == board[i + 1][j] &&
                    board[i + 1][j] == board[i + 2][j] &&
                    board[i + 2][j] == board[i + 3][j] &&
                    board[i][j] != 0) {
                    return board[i][j];
                }
            }
        }
        return 0;
    }
    
    /**
     * horizontal() is a helper method that checks whether the game has reached a win 
     * condition in the horizontal direction.
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won horizontally, and
     *            2 if player 2 has won horizontally
     */
    public int horizontal() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i][j + 1] &&
                    board[i][j + 1] == board[i][j + 2] && 
                    board[i][j + 2] == board[i][j + 3] &&
                    board[i][j] != 0) {
                    return board[i][j];
                }
            }
        }
        return 0;
    }
    
    /**
     * diagonal() is a helper method that checks whether the game has reached a win 
     * condition in either diagonal direction (descending or ascending).
     * 
     * @return 0 if nobody has won yet, 1 if player 1 has won diagonally, and
     *            2 if player 2 has won diagonally
     */
    public int diagonal() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i + 1][j + 1] &&
                    board[i + 1][j + 1] == board[i + 2][j + 2] &&
                    board[i + 2][j + 2] == board[i + 3][j + 3] &&
                    board[i][j] != 0) {
                    return board[i][j];
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 3; j < 7; j++) {
                if (board[i][j] == board[i + 1][j - 1] &&
                    board[i + 1][j - 1] == board[i + 2][j - 2] &&
                    board[i + 2][j - 2] == board[i + 3][j - 3] &&
                    board[i][j] != 0) {
                    return board[i][j];
                }
            }
        }
        return 0;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) { 
                    System.out.print(" | "); 
                }
            }
            if (i < 2) {
                System.out.println("\n---------"); 
            }
        }
    }
    
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[6][7];
        numTurns = 0;
        player1 = true;
        gameOver = false;
        moves.clear();
    }
    
    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     * false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }
    
    /**
     * getCell is a getter for the contents of the
     * cell specified by the method arguments.
     * 
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents
     *         of the corresponding cell on the 
     *         game board.  0 = empty, 1 = Player 1,
     *         2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }
    
    /**
     * This main method illustrates how the model is
     * completely independent of the view and
     * controller.  We can play the game from start 
     * to finish without ever creating a Java Swing 
     * object.
     * 
     * This is modularity in action, and modularity 
     * is the bedrock of the  Model-View-Controller
     * design framework.
     * 
     * Run this file to see the output of this
     * method in your console.
     */
    public static void main(String[] args) {
        ConnectFour t = new ConnectFour();

        t.playTurn(1, 1);
        t.printGameState();
        
        t.playTurn(0, 0);
        t.printGameState();

        t.playTurn(0, 2);
        t.printGameState();
        
        t.playTurn(2, 0);
        t.printGameState();

        t.playTurn(1, 0);
        t.printGameState();
        
        t.playTurn(1, 2);
        t.printGameState();
        
        t.playTurn(0, 1);
        t.printGameState();
        
        t.playTurn(2, 2);
        t.printGameState();
        
        t.playTurn(2, 1);
        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + t.checkWinner());
    }
}
