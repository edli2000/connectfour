/**
 * CIS 120 HW09 - ConnectFour
 * University of Pennsylvania
 * Created by Edward Li in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This class instantiates a ConnectFour object, which is the model for the game.
 * As the user clicks the game board, the model is updated.  Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with 
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private ConnectFour cf; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 350;
    public static final int BOARD_HEIGHT = 300;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        cf = new ConnectFour(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                
                // updates the model given the coordinates of the mouseclick
                cf.playTurn(p.x / 50, p.y / 50);
                
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        cf.reset();
        status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    // undo() is a helper method that calls the ConnectFour class for internal model modification
    // of the previous move. Repaint and update status are necessary since the visual display
    // of the game should reflect these internal changes.
    public void undo() {
        cf.undoLast();
        repaint();
        updateStatus();
        requestFocusInWindow();
    }
    
    // save() is a helper method that calls the ConnectFour class for internal snapshot of
    // the current game state and save to a text file.
    public void save() throws IOException {
        cf.saveToFile();
    }
    
    // read() is a helper method that takes in a file path string and feeds it to the ConnectFour
    // class for internal pre-population of the game model. Repaint and update status are
    // necessary since the visual display of the game should reflect these internal changes.
    public void read(String filePath) {
        cf.readFromFile(filePath);
        repaint();
        updateStatus();
    }
    

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (cf.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }
        
        int winner = cf.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board.  This approach
     * will not be sufficient for most games, because it is not 
     * modular.  All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper 
     * methods.  Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draws board grid
        g.drawLine(50, 0, 50, 300);
        g.drawLine(100, 0, 100, 300);
        g.drawLine(150, 0, 150, 300);
        g.drawLine(200, 0, 200, 300);
        g.drawLine(250, 0, 250, 300);
        g.drawLine(300, 0, 300, 300);
        g.drawLine(0, 50, 350, 50);
        g.drawLine(0, 100, 350, 100);
        g.drawLine(0, 150, 350, 150);
        g.drawLine(0, 200, 350, 200);
        g.drawLine(0, 250, 350, 250);
        
        // Draws X's and O's
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int state = cf.getCell(j, i);
                if (state == 1) {
                    g.setColor(Color.red);
                    g.drawOval(15 + 50 * j, 15 + 50 * i, 20, 20);
                } else if (state == 2) {
                    g.setColor(Color.blue);
                    g.drawOval(15 + 50 * j, 15 + 50 * i, 20, 20);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}