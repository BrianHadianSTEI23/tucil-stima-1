package src;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    // attributes
    private List<List<Integer>> data;
    private int rows, cols;

    // constructor
    public Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.data = new ArrayList<>();
    }

    public int getRow() {
        return rows;
    }
    
    public int getCol() {
        return cols;
    }

    public void setElement(int row, int col, int e){
        data.get(row).set(col, e);
    }

    // show matrix
    public void showMatrix () {
        for (List<Integer> row : data){
            System.out.println(row);
        }
    }
}
