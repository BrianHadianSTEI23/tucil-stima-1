package src;
import java.util.ArrayList;
import java.util.List;

public class PuzzleMap {

    // attributes
    private int rows;
    private int columns;
    private int modifyPuzzleIter = 0;
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

    // getter : columns
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
                if (this.getElement(i, j) != '?') {
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
                if (puzzle.getElement(rows, cols) != '0') {
                    nBlocksPuzzle++;
                }
            }
        }

        // check if the block can fit
        if (map.emptyBoxInMap() > nBlocksPuzzle) {
            int i = 0; int j = 0;
            while (i < puzzle.getMatrix().size() && fit) {
                while (j < puzzle.getMatrix().get(i).size() && fit) {
                    if (puzzle.getMatrix().get(i).get(j) == '+' && map.getElement(i + rows, j + cols) == '+') {
                        fit = false;
                    }
                }
            }
            map.charInMap.add(puzzle.getCharacter());
        } else {
            fit = false;
        }
        return fit;
    }

    // func : set the map after puzzle blocks being placed
    public void setMapAfterPuzzle(int rows, int cols, PuzzleMap map, Puzzle puzzle) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                map.setElement(i + rows, j + cols, '+');
            }
        }
    }

    // func : check for the puzzle block has been placed before or not
    public Puzzle puzzleNotUsed (PuzzleMap map, Puzzle[] puzzleList) {
        
        // variables
        int i = 0;
        while (map.getCharInMap().contains(puzzleList[i].getCharacter())) {
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
    

    // syarat max percobaan untuk setiap map puzzle = 8 * N * P(N,N)

    // func : fill the map
    public boolean fillMap (PuzzleMap map, Puzzle puzzle){

        // variables
        int nBlockPuzzle = 0;

        // get how many block is filled by puzzle
        for (int i = 0; i < puzzle.getRows(); i++) {
            for (int j = 0; j < puzzle.getColumns(); j++) {
                if (puzzle.getElement(i, j) != '1') {
                    nBlockPuzzle++;
                }
            }
        }
        
        /*
            * algorihtm
            * 1. check are there enough empty blocks for puzzle. if no, return false
            * 2. if yes, check is the puzzle has been used before. if yes, go to next puzzle
            * 3. if no, check whether the puzzle will fit or not. if yes, place it
            * 4. if no, flip vertikal and then do recursive using that modified puzzle
            * 5. if no again, flip horizontal 
            * 6. if no again, flip vertical
            * 7. if there are no possible solution, go on to next puzzle
            * 8. in the end, check again that every puzzle has to be in the charInMap and there are no out of boundary.
            * 9. if not possible, return false. if possible, return true
            */

        // check are there enough empty blocks for puzzle
        if (map.emptyBoxInMap() > nBlockPuzzle) {
            if (isPuzzleUsed(map, puzzle) == false) { // puzzle not used
                // trace each position for availability to be filled
                int k = 0;
                int l = 0;
                boolean available = false;
                while (!available && k < map.getRows()) {
                    while (!available && l < map.getColumns()) {
                        if (canBlockFit(k, l, map, puzzle)) { // block dapat masuk
                            available = true;
                            setMapAfterPuzzle(k, l, map, puzzle);
                            return true;
                        } else { // block cannot fit
                            if (modifyPuzzleIter < 3) { // recursive of the current map
                                puzzle.modifyPuzzle();
                                modifyPuzzleIter++;
                                return fillMap(map, puzzle);
                            } else if (k < map.getRows() && l < map.getColumns()){
                                modifyPuzzleIter = 0;
                                l++;
                            } else if (k < map.getRows() && l == (map.getColumns() - 1)){
                                modifyPuzzleIter = 0;
                                k++;
                                l = 0;
                            } else { // all coordinates has been tried, k >= map.getRows && l == map.getColumns() - 1
                                modifyPuzzleIter = 0;
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                return false; // puzzle has been used
            }
        } else {
            resetPuzzleMap();
            return false;
        }
            
        
    }

}
