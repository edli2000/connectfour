/**
 * CIS 120 HW09 - ConnectFour
 * University of Pennsylvania
 * Created by Edward Li in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard.  The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a ConnectFour object to serve as the game's model.
 * 
 * run() opens the home screen, with options for instructions, new game, or load game.
 */
public class Game implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Home Menu");
        frame.setLocation(350, 300);

        // Various panels
        final JPanel title_panel = new JPanel();
        frame.add(title_panel, BorderLayout.NORTH);
        final JLabel title = new JLabel("Connect Four!");
        title_panel.add(title);
        final JPanel playerOne = new JPanel();
        frame.add(playerOne, BorderLayout.WEST);
        final JLabel p1 = new JLabel("Player 1: Red");
        playerOne.add(p1);
        final JPanel playerTwo = new JPanel();
        frame.add(playerTwo, BorderLayout.EAST);
        final JLabel p2 = new JLabel("Player 2: Blue");
        playerTwo.add(p2);
        final JPanel instr_panel = new JPanel();
        frame.add(instr_panel, BorderLayout.CENTER);
        final JPanel play_panel = new JPanel();
        frame.add(play_panel, BorderLayout.SOUTH);

        // Create and add buttons to the panels. Ensure these buttons have action 
        // listeners and can respond accordingly to mouse clicks.
        final JButton instr = new JButton("Instructions");
        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instr();
            }
        });
        instr_panel.add(instr);
        
        final JButton startNew = new JButton("New Game");
        startNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        play_panel.add(startNew);
        
        final JButton loadNew = new JButton("Load Game");
        loadNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });
        play_panel.add(loadNew);
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
    // instr() opens a frame indicating the instructions of how to play the game.
    // There is a back button to close the window and return to the home screen.
    public void instr() {
        final JFrame frame = new JFrame("Instructions");
        frame.setLocation(350, 300);
        
        final JPanel title_panel = new JPanel();
        frame.add(title_panel, BorderLayout.NORTH);
        final JLabel title = new JLabel("Instructions");
        title_panel.add(title);
        
        final JPanel text_panel = new JPanel();
        frame.add(text_panel, BorderLayout.CENTER);
        final JTextArea text = 
            new JTextArea("OVERVIEW\n" + 
                          "Connect Four is a two player game that takes place on a 6x7 grid.\n" + 
                          "Player One will play red pieces and Player Two will play blue pieces\n" +
                          "on the board. Each piece played falls to the lowest unoccupied grid\n" +
                          "slot, and the first to connect four pieces of the same color in a\n" + 
                          "row, column, or diagonal wins the game!" + 
                          "\n\n" + 
                          "NEW GAME\n" + 
                          "This button creates a new, empty game board. Player 1 will go first\n" + 
                          "and will be placing a red piece." +
                          "\n\n" + 
                          "LOAD GAME\n" + 
                          "This button will ask for a file path to an existing game data file\n" + 
                          "(a .txt file). If the file path is valid, that partially-completed\n" +
                          "game will load, and the appropriate player can continue their move.\n" +
                          "If the file path is not valid, a new, blank game will load with\n" +
                          "Player One making the first move." +
                          "\n\n" + 
                          "RESET GAME\n" + 
                          "Once in-game, this button allows for the board to be reset to a new,\n" +
                          "blank game board with Player One making the starting move." + 
                          "\n\n" + 
                          "UNDO MOVE\n" + 
                          "Once in-game, this button allows for the previous move to be\n" +
                          "removed from the game board, allowing the player who placed that\n" +
                          "move to make another move. This may be useful for misclicks." + 
                          "\n\n" + 
                          "SAVE GAME\n" + 
                          "Once in-game, this button saves the current game state by creating a\n" +
                          ".txt file with the all of the moves listed in-order when the save\n" +
                          "button was pressed. This file will be named in a numerical save\n" +
                          "slot, beginning with 1, and can be reloaded into the game under the\n" +
                          "'Load Game' option on the home menu. Note that every time the home\n " +
                          "menu is closed and re-opened, the save functionality will restart\n" +
                          "all saved files at '1.txt' and may overwrite any existing files of \n" +
                          "the same name (ie. 1.txt, 2.txt, etc.). Otherwise, the saved files\n" +
                          "will be named '1.txt' and increment upwards."
                          );
        text_panel.add(text);
        
        final JPanel back_panel = new JPanel();
        frame.add(back_panel, BorderLayout.SOUTH);
        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        back_panel.add(back);
        
        frame.pack();
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    // loadGame() opens a frame that contains a text input box to input the file path
    // of the file containing the existing game to be loaded. If the file path is invalid,
    // a new, blank game will automatically be started.
    public void loadGame() {
        final JFrame frame = new JFrame("Connect Four");
        frame.setLocation(350, 300);
        
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        
        String result = (String)JOptionPane.showInputDialog(
                        frame,
                        "Type the system path of a saved game.\n",
                                "Load saved game",
                                JOptionPane.PLAIN_MESSAGE
                        ); 
        try {
            if (result != null) {
                // Game board is instantiated
                final GameBoard board = new GameBoard(status);
                frame.add(board, BorderLayout.CENTER);
                board.read(result);

                final JPanel control_panel = new JPanel();
                frame.add(control_panel, BorderLayout.NORTH);

                final JButton reset = new JButton("Reset");
                reset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.reset();
                    }
                });
                control_panel.add(reset);
        
                final JButton undo = new JButton("Undo");
                undo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        board.undo();
                    }
                });
                control_panel.add(undo);
        
                final JButton save = new JButton("Save");
                save.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            board.save();
                        } catch (IOException i) {
                            System.out.println("Error in saving file!");
                        }
                    }
                });
                control_panel.add(save);

                frame.pack();
                // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Cannot load file: " + result + "\n" + ex.getMessage(), 
                    "Alert",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // newGame() begins a new, blank game board
    public void newGame() {
        final JFrame frame = new JFrame("Connect Four");
        frame.setLocation(350, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        
        // Game board is instantiated
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);
        
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);
        
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    board.save();
                } catch (IOException i) {
                    System.out.println("Error in saving file!");
                }
            }
        });
        control_panel.add(save);

        frame.pack();
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}