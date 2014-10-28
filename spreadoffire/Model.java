package spreadoffire;

import java.util.*;
import java.awt.Color;

/**
 * The model class of project from MVC pattern
 * 
 * @author OOPgroup8
 * @version 2014.10.14
 */
public class Model {
    
    private View observer;
    public int width, height;
    public int probCatch, probBurning, probTree;
    private Cell cell[][];
    private boolean cellCheck[][];

    /**
     * Constructor create the grid
     */
    public Model() {
        this(30,30,50,0,100);
    }
    
    /**
     * Constructor, create the field
     * @param width
     * @param height
     */
    public Model(int width, int height){
        this(width,height,50,0,100);
    }
    
    /**
     * Constructor create the grid
     * @param width
     * @param height
     * @param probC
     * @param probB
     * @param probT
     */
    public Model(int width, int height,int probC, int probB, int probT) {
        this.width = width;
        this.height = height;
        this.probCatch = probC;
        this.probBurning = probB;
        this.probTree = probT;
        //Reset the field
        resetGrid();
    }    
    
    /**
     * Set the burning probability of the forest
     * @param probCatch
     */
    public void setProbCatch(int probC) {
        this.probCatch = probC;
    }
    
    /**
     * Set the burning probability each tree
     * @param probBurning
     */
    public void setProbBurning(int probB) {
        this.probBurning = probB;
    }
    
    /**
     * Set the density of forest
     * @param probT
     */
    public void setProbTree(int probT) {
        this.probTree = probT;
    }
    
    /**
     * set size of grid
     * @param width
     * @param height 
     */
    public void setSize(int width,int height){
        this.width=width;
        this.height=height;
        //Reset the field after set
        resetGrid();
    }
    
    /**
     * Get the RGB Color of cell at x,y
     * @param x
     * @param y
     * @return RGB Color
     */
    public Color getColor(int x,int y){
        try{
            return cell[x][y].getColor();
        }
        catch(Exception e){
            //If x,y out of bound, treat as empty
            return Color.WHITE;
        }
    }
    
    /**
     * Get the state of cell at x,y
     * @param x
     * @param y
     * @return Cell state
     */
    private int get(int x,int y){
        try{
            return cell[x][y].get();
        }
        catch(Exception e){
            //If x,y out of bound, treat as empty
            return Cell.YELLOW;
        }
    }
    
    /**
     * Reset the field
     */
    public void resetGrid(){
        //Create new grid with current size which
        // 0 - empty - on boundary
        // 1 - tree - inside
        // 2 - burning tree - in middle
        cell = new Cell[width][height];        
        cellCheck=new boolean[width][height]; resetCheck();
        for(int i = 0; i < cell.length; i++){
            for(int j=0; j<cell[0].length; j++){
                if(i==0 || j==0 || i==cell.length-1 || j==cell[0].length-1 ){
                    cell[i][j] = new Cell(Cell.YELLOW);
                }else{
                     if(random(probTree)){
                        cell[i][j] = new Cell(Cell.GREEN);
                    }else{cell[i][j] = new Cell(Cell.YELLOW);}
                }
            } 
        }
        // Set middle for the starting point
        cell[width/2][height/2] = new Cell(Cell.RED);
        
        //Update the field
        update();
    }
        
    /**
     * reset the cell for check
     */
    public  void resetCheck(){
       for(int i=0;i<cellCheck.length;i++){
           for(int j=0;j<cellCheck[0].length;j++){
               cellCheck[i][j]=false;
           }
       }
    }     
    
    /**
     * Spread the fire from x,y 
     * @param s
     * @param x
     * @param y
     */
    public void spread(String s,int x, int y){               
        if(s.equals("north")  && get(x,y-1) == Cell.GREEN && random(probCatch)==true){       
                cell[x][y-1].set(Cell.RED); cellCheck[x][y-1] = true;
        }
        if(s.equals("south") && get(x,y+1) == Cell.GREEN && random(probCatch)==true){  
                cell[x][y+1].set(Cell.RED); cellCheck[x][y+1] = true;  
        }
        if(s.equals("west") && get(x-1,y) == Cell.GREEN && random(probCatch)==true){
                cell[x-1][y].set(Cell.RED); cellCheck[x-1][y] = true;      
        }
        if(s.equals("east") && get(x+1,y) == Cell.GREEN && random(probCatch)==true){
                cell[x+1][y].set(Cell.RED); cellCheck[x+1][y] = true;              
        }  
        update();   
    }
    
    /**
     * Search for the fire 
     */
    public void checkBurn(){
        update();
        try {
            Thread.sleep(100);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        for(int i = 0; i < cell.length-1; i++){
            for(int j=0; j<cell[0].length-1; j++){
                if(get(i,j) == Cell.RED && cellCheck[i][j] == false){
                    cell[i][j].set(Cell.YELLOW);
                    spread("north",i,j);    spread("south",i,j);  
                    spread("west",i,j);     spread("east",i,j);     
                }
            }
        }
    }             
         
     /**
     * Check if there is no burning finish (no 2 anymore)
     * @return true if there is still burning tree in th forest
     */
    public boolean finish(){
        for(int i=0;i<cell.length;i++){
            for(int j=0;j<cell[0].length;j++){
                if(get(i,j) == Cell.RED)
                    return false;
            }
        } return true;
    }
    
    /**
     * random a number
     * @return boolean of random number
     */
     public boolean random(double a){
        double rnd = Math.random()*100;
        if(rnd<a)
            return true;
        return false;
    }

    /**
     * print grid 
     */
    public void showGraph(){
        for(int i = 0; i < cell.length; i++){
           for(int j = 0; j < cell[0].length; j++){
              System.out.printf("%2s ","");
              if(cell[i][j].get()==Cell.GREEN)System.out.print(1);
                else if(cell[i][j].get()==Cell.RED)System.out.print(2);
                else if(cell[i][j].get()==Cell.YELLOW)System.out.print(0);
           }
           System.out.println();
        }
    } 
    
    /**
     * Add the observer for this model
     * @param view
     */
    public void addObserver(View view){
        observer=view;
        update();
    }

    /**
     * Update this field
     */
    public void update(){
        if(observer!=null)observer.update(cell);    
    }
}


