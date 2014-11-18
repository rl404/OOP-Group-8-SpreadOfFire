package spreadoffire;

import java.awt.Color;

/**
 * The cell class that contain the properties of cell
 * 
 * @author OOPgroup8
 * @version 2014.11.16
 */

public class Cell {
    public static final int BLUE=3,GREEN=2,RED=1,YELLOW=0;
    private int color,lightningStep;

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
     * Set the lightning step
     * @param s 
     */
    public void setLightningStep(int s){
        this.lightningStep=s;
    }
    
    /**
     * Decrease the lightning step
     */
    public void stepLightning(){
        this.lightningStep--;
    }
    
    /**
     * Get the current lightning step
     * @return lightningStep
     */
    public int getLightningStep(){
        return lightningStep;
    }
    
    /**
     * Get the RGB Color of cell
     * @return RGB Color
     */
    public Color getColor(){
        if(color==BLUE)return Color.BLUE;
        if(color==GREEN)return Color.GREEN;
        if(color==RED)return Color.RED;
        return Color.YELLOW;
    }
}
