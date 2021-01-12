import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

// IMPORTANT: Delete all files from "files" folder before running!
// This code tests the save functionality and will create/check for files, so make sure the
// "files" folder is empty, or otherwise failures will occur.

public class GameTest {
    
    ConnectFour cf = new ConnectFour();

    @Test
    public void playerOneTurn() {
        cf.playTurn(3,1);
        assertEquals(0, cf.getCell(3,1));
        assertEquals(1, cf.getCell(3,5));
    }
    
    @Test
    public void playerOneTurnOOB() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> cf.playTurn(7,1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> cf.playTurn(1,6));
        assertEquals(0, cf.getCell(0,5));
        assertEquals(0, cf.getCell(1,5));
        assertEquals(0, cf.getCell(2,5));
        assertEquals(0, cf.getCell(3,5));
        assertEquals(0, cf.getCell(4,5));
        assertEquals(0, cf.getCell(5,5));
        assertEquals(0, cf.getCell(6,5));
    }
    
    @Test
    public void playerTwoTurn() {
        cf.playTurn(3,1);
        cf.playTurn(3,1);
        assertEquals(0, cf.getCell(3,1));
        assertEquals(1, cf.getCell(3,5));
        assertEquals(2, cf.getCell(3,4));
    }
    
    @Test
    public void checkWinP1Hor() {
        cf.playTurn(3,1);
        cf.playTurn(3,1);
        cf.playTurn(4,1);
        cf.playTurn(5,2);
        cf.playTurn(1,5);
        cf.playTurn(5,2);
        cf.playTurn(2,1);
        assertEquals(1, cf.checkWinner());
        assertEquals(2, cf.getCell(5,4));
    }
    
    @Test
    public void checkWinP2Vert() {
        cf.playTurn(3,1);
        cf.playTurn(6,1);
        cf.playTurn(3,1);
        cf.playTurn(6,2);
        cf.playTurn(3,1);
        cf.playTurn(6,2);
        cf.playTurn(4,1);
        cf.playTurn(6,1);
        assertEquals(2, cf.checkWinner());
        assertEquals(1, cf.getCell(3,3));
    }
    
    @Test
    public void checkWinP2DiagDescend() {
        cf.playTurn(5,1);
        cf.playTurn(6,5);
        cf.playTurn(4,1);
        cf.playTurn(5,4);
        cf.playTurn(4,1);
        cf.playTurn(4,1);
        cf.playTurn(3,1);
        cf.playTurn(3,1);
        cf.playTurn(3,1);
        cf.playTurn(3,1);
        assertEquals(2, cf.checkWinner());
        assertEquals(1, cf.getCell(5,5));
    }
    
    @Test
    public void checkWinP1DiagAscend() {
        cf.playTurn(3,1);
        cf.playTurn(4,2);
        cf.playTurn(4,1);
        cf.playTurn(5,4);
        cf.playTurn(4,1);
        cf.playTurn(5,1);
        cf.playTurn(5,1);
        cf.playTurn(6,1);
        cf.playTurn(6,1);
        cf.playTurn(6,1);
        cf.playTurn(6,1);
        assertEquals(1, cf.checkWinner());
        assertEquals(1, cf.getCell(4,3));
    }
    
    @Test
    public void checkTie() {
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        assertEquals(3, cf.checkWinner());
    }
    
    @Test
    public void checkResetEmpty() {
        cf.reset();
        assertTrue(cf.getMoves().isEmpty());
        assertEquals(0, cf.getCell(0,5));
        assertEquals(0, cf.getCell(1,5));
        assertEquals(0, cf.getCell(2,5));
        assertEquals(0, cf.getCell(3,5));
        assertEquals(0, cf.getCell(4,5));
        assertEquals(0, cf.getCell(5,5));
        assertEquals(0, cf.getCell(6,5));
    }
    
    @Test
    public void checkResetNonEmpty() {
        cf.playTurn(4,0);
        cf.reset();
        assertTrue(cf.getMoves().isEmpty());
        assertEquals(0, cf.getCell(0,5));
        assertEquals(0, cf.getCell(1,5));
        assertEquals(0, cf.getCell(2,5));
        assertEquals(0, cf.getCell(3,5));
        assertEquals(0, cf.getCell(4,5));
        assertEquals(0, cf.getCell(5,5));
        assertEquals(0, cf.getCell(6,5));
    }
    
    @Test
    public void checkResetCompletedGame() {
        cf.playTurn(0,0);
        cf.playTurn(0,0);
        cf.playTurn(1,0);
        cf.playTurn(1,0);
        cf.playTurn(2,0);
        cf.playTurn(2,0);
        cf.playTurn(3,0);
        cf.reset();
        assertTrue(cf.getMoves().isEmpty());
        assertEquals(0, cf.getCell(0,5));
        assertEquals(0, cf.getCell(1,5));
        assertEquals(0, cf.getCell(2,5));
        assertEquals(0, cf.getCell(3,5));
        assertEquals(0, cf.getCell(4,5));
        assertEquals(0, cf.getCell(5,5));
        assertEquals(0, cf.getCell(6,5));
    }
    
    @Test
    public void checkUndo() {
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(6,0);
        cf.undoLast();
        assertEquals(0, cf.getCell(6,4));
        assertEquals(1, cf.getCell(6,5));
        assertTrue(!cf.getCurrentPlayer());
    }
    
    @Test
    public void checkUndoOOB() {
        cf.playTurn(4,0);
        cf.undoLast();
        cf.undoLast();
        assertEquals(0, cf.getCell(4,5));
        assertTrue(cf.getCurrentPlayer());
    }
    
    @Test
    public void getMovesEncapsulated() {
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(6,0);
        LinkedList<String> test = cf.getMoves();
        test.pop();
        test.pop();
        test.pop();
        test.pop();
        assertEquals(1, cf.getCell(4,5));
        assertEquals(2, cf.getCell(5,5));
        assertEquals(1, cf.getCell(6,5));
        assertEquals(2, cf.getCell(6,4));
        assertTrue(test.isEmpty());
    }
    
    @Test 
    public void saveEmptyAndSingle() throws IOException {
        File file = new File("files/1.txt"); // IMPORTANT: Delete '1.txt' file before running!
        cf.playTurn(4,0);
        cf.undoLast();
        cf.undoLast();
        cf.saveToFile();
        assertTrue(!file.exists()); 
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(6,0);
        cf.saveToFile();
        assertTrue(file.exists()); 
    }
    
    @Test 
    public void saveMultipleAndEndGame() throws IOException {
        File file1 = new File("files/1.txt"); // IMPORTANT: Delete '1.txt' file before running!
        File file2 = new File("files/2.txt"); // IMPORTANT: Delete '2.txt' file before running!
        File file3 = new File("files/3.txt"); // IMPORTANT: Delete '3.txt' file before running!
        assertTrue(file1.exists());
        assertTrue(!file2.exists());
        assertTrue(!file3.exists());
        cf.playTurn(4,0);
        cf.playTurn(5,0);
        cf.playTurn(6,0);
        cf.playTurn(6,0);
        cf.saveToFile();
        assertTrue(file1.exists()); 
        assertEquals(55, file1.length());
        cf.playTurn(6,0);
        cf.playTurn(6,0);
        cf.saveToFile();
        assertTrue(file2.exists()); 
        assertEquals(83, file2.length());
        cf.playTurn(3,0);
        cf.playTurn(3,0);
        cf.playTurn(2,0);
        cf.playTurn(2,0);
        cf.playTurn(1,0);
        cf.saveToFile();
        assertTrue(!file3.exists()); 
    }
    
    @Test
    public void readValidWithFLI() {
        File file = new File("files/1.txt"); // IMPORTANT: Delete '1.txt' file before running!
        assertTrue(file.exists());
        cf.readFromFile("files/1.txt");
        assertEquals(1, cf.getCell(4,5));
        assertEquals(2, cf.getCell(5,5));
        assertEquals(1, cf.getCell(6,5));
        assertEquals(2, cf.getCell(6,4));
    }
    
    @Test
    public void readInvalid() {
        File file = new File("files/3.txt"); // IMPORTANT: Delete '1.txt' file before running!
        assertTrue(!file.exists());
        cf.readFromFile("files/3.txt");
        assertEquals(0, cf.getCell(0,5));
        assertEquals(0, cf.getCell(1,5));
        assertEquals(0, cf.getCell(2,5));
        assertEquals(0, cf.getCell(3,5));
        assertEquals(0, cf.getCell(4,5));
        assertEquals(0, cf.getCell(5,5));
        assertEquals(0, cf.getCell(6,5));
    }

}
