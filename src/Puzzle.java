package src;
import java.util.ArrayList;
import java.util.List;



/* Puzzle class attributes
 * 1. row and column
 * 2. list of list of '+'
 */

public class Puzzle {

    
    // atributes
    private int rows;
    private int columns;
    private List<List<Character>> matrix;
    private char character;
    private boolean isFlipVertical;
    private boolean isFlipHorizontal;
    private boolean isTransposed;

    // constructor
    public Puzzle(List<List<Character>> rawPuzzle) {
        // reading row
        // System.out.println(rawPuzzle.size()); // for debug
        this.rows = rawPuzzle.size();
        
        // reading column
        this.columns = rawPuzzle.get(0).size(); // for debug
        // System.out.println(rawPuzzle.get(0).size());
        
        // make the puzzle in matrix format 
        this.matrix = new ArrayList<>();
        for (int i = 0; i < rawPuzzle.size(); i++) {
            List<Character> rows = new ArrayList<>();
            for (int j = 0; j < rawPuzzle.get(i).size(); j++) {
                if (rawPuzzle.get(i).get(j) != ' ') {
                    rows.add('1');
                } else {
                    rows.add('0');
                }
            }
            matrix.add(rows);
        }

        // get the character of the current matrix
        int i = 0;
        while (rawPuzzle.get(0).get(i) == ' ') {
            i++;
        }
        this.character = rawPuzzle.get(0).get(i);

        // status of puzzle
        this.isFlipHorizontal = false;
        this.isFlipVertical = false;
        this.isTransposed = false;
    }

    // Getter : rows
    public int getRows() {
        return this.rows;
    }

    // Getter : columns
    public int getColumns() {
        return this.columns;
    }

    // Getter : columns
    public char getCharacter() {
        return this.character;
    }

    // getter : matrix
    public List<List<Character>> getMatrix () {
        return this.matrix;
    }

    // getter : element in matrix
    public char getElement(int rows, int columns) {
        return this.getMatrix().get(rows).get(columns);
    }

    // getter : flipped vertical status
    public boolean getStatusFlippedVertical() {
        return this.isFlipVertical;
    }

    // getter : flipped vertical status
    public boolean getStatusFlippedHorizontal() {
        return this.isFlipHorizontal;
    }

    // getter : transposed status
    public boolean getStatusTransposed(){
        return this.isTransposed;
    }
    
    // setter : columns
    public void setColumns(int newColumns) {
        this.columns = newColumns;
    }
    
    // setter : rows
    public void setRows(int newRows) {
        this.rows = newRows;
    }
    
    // setter : characters
    public void setCharacter(char newCharacter) {
        this.character = newCharacter;
    }
    
    // setter : matrix
    public void setMatrix (List<List<Character>> newMatrix) {
        this.matrix = newMatrix;
    }
    
    // setter : matrix per rows
    public void setRowsInMatrix (Integer i, List<Character> newRows) {
        this.matrix.set(i, newRows);
    }

    // setter : set flipped vertical status
    public void setStatusFlippedVertical(boolean bool) {
        this.isFlipVertical = bool;
    }

    // setter : set flipped vertical status
    public void setStatusFlippedHorizontal(boolean bool) {
        this.isFlipHorizontal = bool;
    }

    // setter : set transposed status
    public void setTransposed(boolean bool){
        this.isTransposed = bool;
    }
    
    // setter : transpose puzzle
    public void transposePuzzle () {
        // variable
        List<List<Character>> transposed = new ArrayList<>();

        for (int i = 0; i < this.getColumns(); i++) {
            List<Character> transposedRows = new ArrayList<>();
            for (int j = 0; j < this.getRows(); j++) {
                transposedRows.add(this.getElement(j, i));
            }
            transposed.add(transposedRows);
        }
        this.setMatrix(transposed);

        // changing state of the puzzle
        int temp = this.getColumns();
        this.setColumns(this.getRows());
        this.setRows(temp);
        if (this.getStatusTransposed() == true) {
            this.setTransposed(false);
        } else {
            this.setTransposed(true);
        }

    }

    // func : flip horizontal puzzle
    public void flipHorizontalPuzzle (){
        // fill all the final matrix
        for (int i = 0; i < this.getRows(); i++) {
            List<Character> finalRows = this.getMatrix().get(i).reversed();
            this.setRowsInMatrix(i, finalRows);
        }
        // changing status of isflippedhorizontal
        if (this.getStatusFlippedHorizontal() == true) {
            this.setStatusFlippedHorizontal(false);
        } else {
            this.setStatusFlippedHorizontal(true);
        }
    }
    
    // func : flip vertical puzzle
    public void flipVerticalPuzzle(){
        // transpose, reverse, transpose
        
        // transposing
        // variable
        this.transposePuzzle();
        this.flipHorizontalPuzzle();
        this.transposePuzzle();

        // Changing status of flipping
        if (this.getStatusFlippedHorizontal() == true) {
            this.setStatusFlippedHorizontal(false);
        } else {
            this.setStatusFlippedHorizontal(true);
        }
        if (this.getStatusFlippedVertical() == true) {
            this.setStatusFlippedVertical(false);
        } else {
            this.setStatusFlippedVertical(true);
        }
    }

    public void modifyPuzzle(){
        /*
        * urutan
        * cek awal
        * cek flip vertikal
        * cek flip vertikal + horizontal
        * cek flip horizontal
        */
        if (this.getStatusFlippedVertical() == false && this.getStatusFlippedHorizontal() == false) {
            this.flipVerticalPuzzle();
        } else if (this.getStatusFlippedVertical() == true && this.getStatusFlippedHorizontal() == false){
            this.flipHorizontalPuzzle();
        } else if (this.getStatusFlippedVertical() == true && this.getStatusFlippedHorizontal() == true){
            this.flipVerticalPuzzle();
        } else if (this.getStatusTransposed() == false){ // default case
            this.flipHorizontalPuzzle();
            this.transposePuzzle();
        } else {
            this.transposePuzzle();
            this.flipVerticalPuzzle();
        }
        // System.out.println(this.getMatrix());
    }

}