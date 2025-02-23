package src;
import java.util.ArrayList;
import java.util.List;

public class PuzzleMap {

    // attributes
    private int rows;
    private int columns;
    private List<List<Character>> map;
    private List<Character> charInMap;

    // constructor
    public PuzzleMap(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.map = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                row.add('?');
            }
            map.add(row);
        }
        this.charInMap = new ArrayList<>();
    }
    
    // getter : rows
    public int getRows() {
        return this.rows;
    }
    
    // getter : columns
    public int getColumns() {
        return this.columns;
    }

    // getter : element
    public char getElement(int rows, int columns) {
        return this.map.get(rows).get(columns);
    }

    // getter : charInMap
    public List<Character> getCharInMap() {
        return this.charInMap;
    }
    
    // setter : rows
    public void setRows(int newRows) {
        this.rows = newRows;
    }
    
    // setter : columns
    public void setColumns (int newColumns) {
        this.columns = newColumns;
    }
    
    // setter : element
    public void setElement (int rows, int columns, Character c) {
        map.get(rows).set(columns, c);
    }
    
    // func : show map
    public void getPuzzleMap(){
        for (int i = 0; i < this.map.size(); i++) {
            List<Character> row = this.map.get(i);
            for (int j = 0; j < row.size(); j++) {
                System.out.print(row.get(j));
            }
            System.err.println();
        }
        System.out.println();
    }
    
    // func : set map empty after finding blockage
    public void resetPuzzleMap() {
        for (int i = 0; i < this.map.size(); i++) {
            for (int j = 0; j < this.map.get(i).size(); j++) {
                this.setElement(i, j, '?');
            }
        }
    }

    // func : check if the puzzle map has still slot left
    public int emptyBoxInMap(){
        int n = 0;

        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                if (this.getElement(i, j) == '?') {
                    n++;
                }
            }
        }

        return n;
    }

    // func : check if the puzle blocks can fit with current coordinate
    public boolean canBlockFit(int rows, int cols, PuzzleMap map, Puzzle puzzle) {
        
        // variables
        boolean fit = true;

        // count how many blocks puzzle will take
        int nBlocksPuzzle = 0;
        for (int i = 0; i < puzzle.getRows(); i++) {
            for (int j = 0; j < puzzle.getColumns(); j++) {
                if (puzzle.getElement(i, j) != '0') {
                    nBlocksPuzzle++;
                }
            }
        }

        // check if the block can fit
        if (map.emptyBoxInMap() > nBlocksPuzzle) {
            int i = 0;
            while (i < puzzle.getRows() && fit) {
                int j = 0;
                while (j < puzzle.getColumns() && fit) {
                    if ((i + rows) < map.getRows() && ((j + cols) < map.getColumns())) {
                        if (puzzle.getElement(i, j) == '1' && map.getElement(i + rows, j + cols) == '+') {
                            fit = false;
                        }
                        j++;
                    } else {
                        fit = false;
                    }
                }
                if (fit) {
                    i++;
                }
            }
        } else {
            fit = false;
        }
        return fit;
    }
    
    // func : set the map after puzzle blocks being placed
    public void setMapAfterPuzzle(int rows, int cols, PuzzleMap map, Puzzle puzzle) {
        for (int i = 0; i < puzzle.getRows(); i++) {
            for (int j = 0; j < puzzle.getColumns(); j++) {
                if (puzzle.getElement(i, j) == '1') {
                    map.setElement(i + rows, j + cols, '+');
                }
            }
        }
        map.charInMap.add(puzzle.getCharacter());
    }

    // func : check for the puzzle block has been placed before or not
    public Puzzle puzzleNotUsed (PuzzleMap map, Puzzle[] puzzleList) {
        
        // variables
        int i = 0;
        while (map.getCharInMap().contains(puzzleList[i].getCharacter()) && i < puzzleList.length) {
            i++;
        }
        
        if (i < puzzleList.length) {
            return puzzleList[i];
        } else {
            return null;
        }
    }

    public boolean isPuzzleUsed (PuzzleMap map, Puzzle puzzle) {
        // / variables
        boolean used = false;
        int i = 0;

        while (!used && i < map.getCharInMap().size()) {
            if (map.getCharInMap().get(i) == puzzle.getCharacter()) {
                used = true;
            } else {
                i++;
            }
        }
        return used;
    }
    
}
