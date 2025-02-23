package src;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.Matrix;


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
    }

    // Getter : rows
    public int getRows() {
        return rows;
    }

    // Getter : columns
    public int getColumns() {
        return columns;
    }

    // Getter : columns
    public char getCharacter() {
        return character;
    }

    // getter : matrix
    public List<List<Character>> getMatrix () {
        return matrix;
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
    
    // setter : transpose puzzle
    public Puzzle transposePuzzle (Puzzle newPuzzle) {
        // variable
        Puzzle finalPuzzle = newPuzzle;
        List<List<Character>> transposed = new ArrayList<>();

        for (int i = 0; i < newPuzzle.getMatrix().get(0).size(); i++) {
            List<Character> transposedRows = new ArrayList<>();
            for (int j = 0; j < newPuzzle.getMatrix().size(); j++) {
                transposedRows.add(newPuzzle.getMatrix().get(j).get(i));
            }
            transposed.add(transposedRows);
        }
        finalPuzzle.setMatrix(transposed);

        // changing state of the puzzle
        int temp = newPuzzle.getColumns();
        finalPuzzle.setColumns(finalPuzzle.getRows());
        finalPuzzle.setRows(temp);

        // returning a new value of Puzzle
        return finalPuzzle;
    }

    // func : flip horizontal puzzle
    public void flipHorizontalPuzzle (){
        // fill all the final matrix
        for (int i = 0; i < this.getRows(); i++) {
            List<Character> finalRows = this.getMatrix().get(i).reversed();
            this.setRowsInMatrix(i, finalRows);
        }
        this.setStatusFlippedHorizontal(true);
    }
    
    // func : flip vertical puzzle
    public void flipVerticalPuzzle(){
        // transpose, reverse, transpose
        
        // transposing
        // variable
        Puzzle finalPuzzle = this;
        List<List<Character>> transposed = new ArrayList<>();
        
        for (int i = 0; i < this.getMatrix().get(0).size(); i++) {
            List<Character> transposedRows = new ArrayList<>();
            for (int j = 0; j < this.getMatrix().size(); j++) {
                transposedRows.add(this.getMatrix().get(j).get(i));
            }
            transposed.add(transposedRows);
        }
        finalPuzzle.setMatrix(transposed);
        
        // changing state of the puzzle
        int temp = this.getColumns();
        finalPuzzle.setColumns(finalPuzzle.getRows());
        finalPuzzle.setRows(temp);
        finalPuzzle.setCharacter(this.getCharacter());
        
        // reversing
        finalPuzzle.flipHorizontalPuzzle();
        
        // transposing again
        for (int i = 0; i < finalPuzzle.getMatrix().get(0).size(); i++) {
            List<Character> transposedRows = new ArrayList<>();
            for (int j = 0; j < finalPuzzle.getMatrix().size(); j++) {
                transposedRows.add(finalPuzzle.getMatrix().get(j).get(i));
            }
            transposed.add(transposedRows);
        }
        finalPuzzle.setMatrix(transposed);
        
        // changing state of the puzzle
        int temp2 = finalPuzzle.getColumns();
        finalPuzzle.setColumns(finalPuzzle.getRows());
        finalPuzzle.setRows(temp2);
        finalPuzzle.setStatusFlippedVertical(true);
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
        }
    }

}