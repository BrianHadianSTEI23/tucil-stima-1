
/*  REMINDER
static means that if a datatype is declared static, then the data is fixed by that 
same value for all of the class instances.
public means that for a function that declared public, it can be accessed from outside the class
protected means that for a futnciton declared protected, it can only be accessed by the same class and its inheritor
private means that it can only be called from the same class.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.Puzzle;
import src.PuzzleMap;

public class main {
        
    // instantiation variable
    private static Integer rows;
    private static Integer columns;
    private static Integer blocks;
    private static String gameMode;
    private static File targetFile = new File("./data/test.txt");
    private static List<List<Character>> letterBlock = new ArrayList<>();
    private static List<List<List<Character>>> puzzle;
    
    // main program
    public static void main(String[] args) {
        try {
            Puzzle[] puzzleList;
            Scanner scanner = new Scanner(targetFile);
            String line = scanner.nextLine();
            
            // reading the config
            for (int i = 0; i < 2 && scanner.hasNextLine(); i++) {

                // new
                
                // old; deprecated
                List<Character> configListPerLine = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    configListPerLine.add(line.charAt(j));
                }
                
                // read the m x n and how many puzzle block
                if (i == 0) {
                    String[] parts = line.split(" ");
                    rows = Integer.parseInt(parts[0]);
                    columns = Integer.parseInt(parts[1]);
                    blocks = Integer.parseInt(parts[2]);
                    line = scanner.nextLine();
                } else { // i must be 1
                    StringBuilder modeBuilder = new StringBuilder();
                    for (char ch : configListPerLine) {
                        modeBuilder.append(ch);
                    }
                    gameMode = modeBuilder.toString();
                }
            }
            
            // debug
            // System.out.println("m = " + rows);
            // System.out.println("n = " + columns);
            // System.out.println("p = " + blocks);
            // System.out.println("mode = " + gameMode);

            // reading puzzle
            while (scanner.hasNextLine()) {
    
                // variable instantiation
                List<Character> letterBlockPerLine = new ArrayList<>();
                    
                // reading puzzle block per line
                line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    letterBlockPerLine.add(line.charAt(i));
                }
                
                // check if the next line has the same character as the list of character above it
                letterBlock.add(letterBlockPerLine);
            }
            scanner.close();

            // validation for config data
            if (blocks instanceof Integer && blocks != 0) {
                if (gameMode.equals("DEFAULT")){
                    // organize letterBlock into shapes
                    puzzle = organizeList(letterBlock);
                    
                    // standardize all the spaces and make it all in puzzle format and stored in a list[]
                    puzzleList = standardizePuzzle(puzzle);
                    
                    // debug
                    System.out.println(columns);

                    // main algorithm
                    PuzzleMap MainMap = new PuzzleMap(rows, columns);
                    
                    // fill the main map
                    boolean possible = true;
                    int operationIter = 0;
                    while (MainMap.getCharInMap().size() <= puzzleList.length && possible) {
                        if (operationIter < (8 * puzzleList.length * (factorial(operationIter)))) {
                            MainMap.fillMap(MainMap, MainMap.puzzleNotUsed(MainMap, puzzleList));
                        } else {
                            possible = false;
                        }
                    }

                    // final check to sysout
                    if (possible) {
                        MainMap.getPuzzleMap();
                    } else {
                        System.out.println("Tidak bisa dilakukan proses brute force.\nSilakan coba lagi");
                    }

                } else {
                    System.out.println("No service as such. Please try again.");
                } 
            } else {
                System.out.println("Not valid datatype for number of blocks");
            }
            
        } catch (Exception e) {
            System.out.println("Error " + e + " has occurred : " + e.getMessage());
        }
    }

    // func : get index element that is not ' ' in letterBlock
    private static char getCharFromCharMatrix (List<List<Character>> letterBlockShape){
        // search for existing alphabet
        int i = 0;

        if (letterBlockShape.isEmpty()) {
            return '~';
        } else {
            while (letterBlock.get(0).get(i) == ' ') {
                i++;
            }
            return letterBlockShape.get(0).get(i);
        }
    }

    private static List<List<List<Character>>> organizeList (List<List<Character>> letterBlock) {
        List<List<List<Character>>> puzzle = new ArrayList<>();
        List<List<Character>> letterBlockShape = new ArrayList<>();
        for (int i = 0; i < (letterBlock.size()); i++) {
            if (letterBlockShape.isEmpty()) {
                letterBlockShape.add(letterBlock.get(i));
            } else { // letterBlockShape not empty

                int k = 0;
                while (letterBlock.get(i).get(k) == ' ') {
                    k++;
                }

                // final check
                if (letterBlock.get(i).get(k) == getCharFromCharMatrix(letterBlockShape)) {
                    letterBlockShape.add(letterBlock.get(i));
                } else {
                    puzzle.add(letterBlockShape);
                    letterBlockShape = new ArrayList<>();
                    letterBlockShape.add(letterBlock.get(i));
                    if (i == (letterBlock.size() - 1)) {
                        puzzle.add(letterBlockShape);
                    }
                }
            }
        }

        return puzzle;
    }

    // func : standardize puzzle
    private static Puzzle[] standardizePuzzle (List<List<List<Character>>> rawPuzzle) {
        Puzzle[] puzzleListNew = new Puzzle[rawPuzzle.size()];
        
        // for adding remaining spaces if needed
        for (int i = 0; i < rawPuzzle.size(); i++) {
            int maxCol = rawPuzzle.get(i).get(0).size();
            for (int j = 0; j < rawPuzzle.get(i).size(); j++) {
                if (rawPuzzle.get(i).get(j).size() > maxCol) {
                    maxCol = rawPuzzle.get(i).get(j).size();
                }
            }

            // add all remaining spaces if needed
            for (int k = 0; k < rawPuzzle.get(i).size(); k++) {
                if (rawPuzzle.get(i).get(k).size() < maxCol) {
                    rawPuzzle.get(i).get(k).add(' ');
                }
            }
        }
        // status : rawPuzzle has been added with spaces if needed

        // converting each matrix into puzzle 
        for (int i = 0; i < rawPuzzle.size(); i++) {
            Puzzle puzzlePieces = new Puzzle(rawPuzzle.get(i));
            puzzleListNew[i] = puzzlePieces;
        }

        return puzzleListNew;
    }

    public static int factorial(int x){
        if (x == 0) {
            return 1;
        } else {
            return x * factorial(x - 1);
        }
    }
    
}






