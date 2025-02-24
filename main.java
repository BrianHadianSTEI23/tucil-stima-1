
/*  REMINDER
static means that if a datatype is declared static, then the data is fixed by that 
same value for all of the class instances.
public means that for a function that declared public, it can be accessed from outside the class
protected means that for a futnciton declared protected, it can only be accessed by the same class and its inheritor
private means that it can only be called from the same class.
 */

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
// import java.util.Map;
// import java.awt.Color;
// import java.awt.image.BufferedImage;
import java.util.Scanner;
// import javax.imageio.ImageIO;

import src.Puzzle;
import src.PuzzleMap;

public class main {
        
    // instantiation variable
    private static Integer rows;
    private static Integer columns;
    private static Integer blocks;
    private static String gameMode;
    private static File targetFile = new File("./data/test2.txt");
    private static List<List<Character>> letterBlock = new ArrayList<>();
    private static List<List<List<Character>>> puzzle;
    
    // main program
    public static void main(String[] args) {
        try {
            // count time elapsed
            
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

                    // main algorithm
                    PuzzleMap MainMap = new PuzzleMap(rows, columns);

                    // fill the main map

                    // variables
                    boolean full = false;
                    int operationIter = 0;             
                    int maxIter =  8 * puzzleList.length * factorial(puzzleList.length) * 100;
                    long timeStart = System.currentTimeMillis();
                    long duration = 300000;
        
                    while (MainMap.getCharInMap().size() <= puzzleList.length && !full && (System.currentTimeMillis() - timeStart) < duration) {
                        
                        // variables
                        int nBlockPuzzle = 0;
                        Puzzle puzzle = MainMap.puzzleNotUsed(MainMap, puzzleList);
                        
                        // get how many block is filled by puzzle
                        for (int i = 0; i < puzzle.getRows(); i++) {
                            for (int j = 0; j < puzzle.getColumns(); j++) {
                                if (puzzle.getElement(i, j) == '1') {
                                    nBlockPuzzle++;
                                }
                            }
                        }
                        
                        // check are there enough empty blocks for puzzle
                        System.out.println(MainMap.isThereAValidPosition(MainMap, puzzle));
                        if (MainMap.emptyBoxInMap() >= nBlockPuzzle && MainMap.isThereAValidPosition(MainMap, puzzle)) { 
                            // trace each position for availability to be filled
                            int k = 0;
                            boolean found = false;
                            int modifyPuzzleIter = 0;
                            while (!found && k < MainMap.getRows()) {
                                int l = 0;
                                while (!found && l < MainMap.getColumns()) {
                                    if (MainMap.canBlockFit(k, l, MainMap, puzzle)) { // block dapat masuk
                                        found = true;
                                        MainMap.setMapAfterPuzzle(k, l, MainMap, puzzle);
                                    } else { // block cannot fit

                                        // check every possibility
                                        while (modifyPuzzleIter < 7 && !found) {
                                            puzzle.modifyPuzzle();
                                            if (MainMap.canBlockFit(k, l, MainMap, puzzle)) { // block dapat masuk
                                                found = true;
                                                MainMap.setMapAfterPuzzle(k, l, MainMap, puzzle);
                                            } else {
                                                modifyPuzzleIter++;
                                            }
                                        }
                                        puzzle.modifyPuzzle(); // reset to first position

                                        // resetting variables
                                        if (!found) {
    
                                            // check other coordinate
                                            l++;
                                            modifyPuzzleIter = 0;
                                        }
                                    }
                                }
                                if (!found) {
                                    k++;
                                }
                            }
                        } else { // there are no enough space in main map
                            MainMap.resetPuzzleMap();
                        }
                        
                        // check if it's full
                        if (MainMap.getCharInMap().size() == puzzleList.length) {
                            full = true;
                        } else {
                            operationIter++;
                        }
                        
                        MainMap.getPuzzleMap();
                        System.out.println(MainMap.getCharInMap());
                        System.out.println(operationIter);
                    }
                    
                    // final check to sysout
                    if (MainMap.emptyBoxInMap() == 0) {
                        MainMap.getPuzzleMap();
                    } else {
                        System.out.println("Tidak bisa dilakukan proses brute force.\nSilakan gunakan file lain.");
                    }
                    long timeStop = System.currentTimeMillis();
                    System.out.println("Elapsed time : " + (timeStop - timeStart) + " ms.");


                    // converting into image
                    // int[][] matrix = {
                    //     {0, 1, 0, 1, 0},
                    //     {1, 0, 1, 0, 1},
                    //     {0, 1, 1, 1, 0},
                    //     {1, 0, 1, 0, 1},
                    //     {0, 1, 0, 1, 0}
                    // };

                    // int width = matrix[0].length;
                    // int height = matrix.length;

                    // // Create a BufferedImage
                    // BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                    // // Convert matrix to image pixels
                    // for (int y = 0; y < height; y++) {
                    //     for (int x = 0; x < width; x++) {
                    //         if (matrix[y][x] == 1) {
                    //             image.setRGB(x, y, Color.BLACK.getRGB()); // Set pixel to black
                    //         } else {
                    //             image.setRGB(x, y, Color.WHITE.getRGB()); // Set pixel to white
                    //         }
                    //     }
                    // }

                    // // Save the image as a PNG file
                    // try {
                    //     File output = new File("output.png");
                    //     ImageIO.write(image, "png", output);
                    //     System.out.println("Image saved as output.png");
                    // } catch (Exception e) {
                    //     e.printStackTrace();
                    // }

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






