package spreadoffire;

import java.awt.Color;

/**
 * The cell class that contain the properties of cell
 * 
 * @author OOPgroup8
 * @version 1.0 2014.11.18
 */

public class Cell {
    public static final int GREEN=2,RED=1,YELLOW=0;
    private int color;

    /**
     * Constructor - create the empty cell
     */
    public Cell(){
        this.color=Cell.YELLOW;
    }

    /**
     * Constructor - create the cell
     * @param color
     */
    public Cell(int color){
        this.color=color;
    }

    /**
     * Get the color of cell
     * @return color
     */
    public int get(){
        return color;
    }

    /**
     * Set the color of cell
     * @param color
     */
    public void set(int color){
        this.color=color;
    }

    /**
     * Check if the cell is empty or not
     * @return true if cell empty
     */
    public boolean isEmpty(){
        return color==YELLOW;
    }   
    
    /**
     * Get the RGB Color of cell
     * @return RGB Color
     */
    public Color getColor(){
        if(color==GREEN)return Color.GREEN;
        if(color==RED)return Color.RED;
        return Color.YELLOW;
    }
}
