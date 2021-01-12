import java.util.Iterator;
import java.io.*;
import java.util.*;


public class LineIterator implements Iterator<String> {

    private FileReader file;
    private BufferedReader reader;
    private String currLine;
    private boolean valid;

    /**
     * Creates a LineIterator for the file located at filePath.
     * If an IOException is thrown by the BufferedReader or FileReader, then hasNext returns false.
     *
     * @param filePath - the path to the text file to be iterator over.
     * @throws IllegalArgumentException if filePath is invalid (null or DNE).
     */
    public LineIterator(String filePath) {
        try {
            if (filePath != null) {
                this.file = new FileReader(filePath);
                this.reader = new BufferedReader(file);
                this.valid = true;
            } else {
                this.valid = false;
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            this.valid = false;
            throw new IllegalArgumentException();
        } 
        

    }

    /**
     * Returns true if there are still lines left to read in the file, and false otherwise. If 
     * there are no more lines left, the BufferedReader is closed.
     *
     * @return a boolean indicating whether the iterator can produce another line from the file
     */
    @Override
    public boolean hasNext() {
        try {
            if (!this.valid) {
                return false;
            }
            currLine = reader.readLine();
            if (currLine == null) {
                reader.close();
            }
        } catch (IOException e) {
            currLine = null;
        } 
        return currLine != null;
    }

    /**
     * Returns the next line from the file and advances the iterator to prepare for the next
     * invocation of this method. Also, this method throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false) and iff an IOException 
     * is thrown, the next time next() is called, it throws a NoSuchElementException.
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the file
     */
    @Override
    public String next() throws NoSuchElementException {
        String nextVal;
        try {
            if (!this.valid) {
                throw new NoSuchElementException();
            }
            
            if (currLine == null) {
                nextVal = reader.readLine();
            } else {
                nextVal = currLine;
            } 
        } catch (IOException e) {
            nextVal = null;
        }
        
        if (nextVal == null) {
            throw new NoSuchElementException();
        }
        
        currLine = null;
        return nextVal;
    }
}
