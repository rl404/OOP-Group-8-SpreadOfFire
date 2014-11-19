package spreadoffire;

import java.util.*;
import java.awt.Color;

/**
 * The model class of project from MVC pattern
 * 
 * @author OOPgroup8
 * @version v2.0 2014.11.18
 */
public class Model {
    
    private Cell cell[][];
    private View observer;
    private boolean cellCheck[][],windN,windS,windW,windE;
    public int width, height, probCatch, probBurning, probTree,probLightning,lightningStep,windLevel,step;

    /**
     * Constructor create the grid
     */
    public Model() {
        this(30,30,50,0,100,0);
    }
    
    /**
     * Constructor, create the field
     * @param width width of the array
    * @param height height of the array
     */
    public Model(int width, int height){
        this(width,height,50,0,100,0);
    }
    
    /**
     * Constructor create the grid
     * @param width width of the array
     * @param height height of the array
     * @param probC the probCatch
     * @param probB the probBurning
     * @param probT the probTree
     * @param probL the probLightning
     */
    public Model(int width, int height,int probC, int probB, int probT, int probL) {
        this.width = width;                 this.height = height;                       windN = true;      windS = true;
        this.probCatch = probC;       this.probBurning = probB;            windE = true;      windW = true;
        this.probTree = probT;          this.probLightning = probL;            lightningStep = 1; windLevel=0;
        //Reset the field
        resetGrid();
    }    
    
    /**
     * Set the burning probability of the forest
     * @param probC the probCatch
     */
    public void setProbCatch(int probC) {
        this.probCatch = probC;
    }
        
    /**
     * Set the density of forest
     * @param probT the probTree
     */
    public void setProbTree(int probT) {
        this.probTree = probT;
    }
    
    /**
     * Set the burning probability each tree
     * @param probB the probBurning
     */
    public void setProbBurning(int probB) {
        this.probBurning = probB;
    }
        
    /**
     * Set the probability that the tree is struck by lightning
     * @param probL the probLightning
     */
    public void setProbLightning(int probL) {
        this.probLightning = probL;
    }
    
    /**
     * Set the wind level
     * @param windL the wind level
     */
    public void setWindLevel(int windL) {
        this.windLevel = windL;
    }
    
    /**
     * set the wind direction
     * @param s the string of the wind direction(north,south,west,east)
     * @param b the boolean for the direction
     */
    public void setWind(String s,boolean b){
        if(s.equals("north")) windN = b;
        if(s.equals("south")) windS = b;
        if(s.equals("east")) windE = b;
        if(s.equals("west")) windW = b;
        //reset all wind state
        if(s.equals("all")){windN = b;windS = b;windE = b;windW = b;}
    }
    
    /**
     * set size of grid
     * @param width width of the array
     * @param height height of the array
     */
    public void setSize(int width,int height){
        this.width=width;
        this.height=height;
        //Reset the field after set
        resetGrid();
    }
    
     /**
     * set the number of step of lightning to burn a tree
     * @param s number of step
     */
    public void setLightningStep(int s){
        this.lightningStep=s;
    }
    
    /**
     * Get the RGB Color of cell at x,y
     * @param x x position of the tree
     * @param y y position of the tree
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
     * @param x x position of the tree
     * @param y y position of the tree
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
                         if(random(probBurning)){
                             cell[i][j] = new Cell(Cell.RED);
                         }else{cell[i][j] = new Cell(Cell.GREEN);}
                    }else{cell[i][j] = new Cell(Cell.YELLOW);}
                }
            } 
        }
        // Set middle for the starting point
        cell[width/2][height/2] = new Cell(Cell.RED);
        
        //reset step
        step=0;
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
     * @param s string of fire direction
     * @param x x position of the fire
     * @param y y position of the fire
     * @param windL the wind level
     */
    public void spread(String s,int x,int y,int windL){        
        //The normal spread direction without wind
        if(windL==0){
            if(s.equals("north") && windN && get(x-1,y) == Cell.GREEN && random(probCatch)==true){       
                    cell[x-1][y].set(Cell.RED); cellCheck[x-1][y] = true;
            }
            if(s.equals("south") && windS && get(x+1,y) == Cell.GREEN && random(probCatch)==true){  
                    cell[x+1][y].set(Cell.RED); cellCheck[x+1][y] = true;  
            }
            if(s.equals("west") && windW && get(x,y-1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y-1].set(Cell.RED); cellCheck[x][y-1] = true;      
            }
            if(s.equals("east") && windE && get(x,y+1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y+1].set(Cell.RED); cellCheck[x][y+1] = true;              
            }       
        }
        
        //When wind level is low
        if(windL==1){
            if(s.equals("north") && get(x-1,y) == Cell.GREEN && random(probCatch)==true){       
                    cell[x-1][y].set(Cell.RED); cellCheck[x-1][y] = true;
                    
                    //Spread to the next tree
                    if(windN && get(x-2,y) == Cell.GREEN && random(probCatch)==true){
                        cell[x-2][y].set(Cell.RED); cellCheck[x-2][y] = true;
                    }
            }
            if(s.equals("south") && get(x+1,y) == Cell.GREEN && random(probCatch)==true){  
                    cell[x+1][y].set(Cell.RED); cellCheck[x+1][y] = true;  
                    
                    //Spread to the next tree
                    if(windS && get(x+2,y) == Cell.GREEN && random(probCatch)==true){
                        cell[x+2][y].set(Cell.RED); cellCheck[x+2][y] = true;
                    }
            }
            if(s.equals("west") && get(x,y-1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y-1].set(Cell.RED); cellCheck[x][y-1] = true;   
                    
                    //Spread to the next tree
                    if(windW && get(x,y-2) == Cell.GREEN && random(probCatch)==true){
                        cell[x][y-2].set(Cell.RED); cellCheck[x][y-2] = true;
                    }
            }
            if(s.equals("east") && get(x,y+1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y+1].set(Cell.RED); cellCheck[x][y+1] = true;  
                    
                    //Spread to the next tree
                    if(windE && get(x,y+2) == Cell.GREEN && random(probCatch)==true){
                        cell[x][y+2].set(Cell.RED); cellCheck[x][y+2] = true;
                    }
            }          
        }
        
        //When wind level is high   
        if(windL==2){
            if(s.equals("north") && !windS && get(x-1,y) == Cell.GREEN && random(probCatch)==true){       
                    cell[x-1][y].set(Cell.RED); cellCheck[x-1][y] = true;
                    
                    //Spread to the next tree
                    if(windN && get(x-2,y) == Cell.GREEN && random(probCatch)==true)
                        cell[x-2][y].set(Cell.RED); cellCheck[x-2][y] = true;
            }
            if(s.equals("south") && !windN && get(x+1,y) == Cell.GREEN && random(probCatch)==true){  
                    cell[x+1][y].set(Cell.RED); cellCheck[x+1][y] = true; 
                    
                    //Spread to the next tree
                    if(windS && get(x+2,y) == Cell.GREEN && random(probCatch)==true)
                        cell[x+2][y].set(Cell.RED); cellCheck[x+2][y] = true;
            }
            if(s.equals("west") && !windE && get(x,y-1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y-1].set(Cell.RED); cellCheck[x][y-1] = true;    
                    
                    //Spread to the next tree
                    if(windW && get(x,y-2) == Cell.GREEN && random(probCatch)==true)
                        cell[x][y-2].set(Cell.RED); cellCheck[x][y-2] = true;
            }
            if(s.equals("east") && !windW && get(x,y+1) == Cell.GREEN && random(probCatch)==true){
                    cell[x][y+1].set(Cell.RED); cellCheck[x][y+1] = true;   
                    
                    //Spread to the next tree
                    if(windE && get(x,y+2) == Cell.GREEN && random(probCatch)==true)
                        cell[x][y+2].set(Cell.RED); cellCheck[x][y+2] = true;
            } 
        }
        update();   
    }
    
    /**
     * Search for the fire 
     */
    public void checkBurn(){
        step++;
        update();
        try {
            Thread.sleep(100);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //Go through the field
        for(int i = 0; i < cell.length-1; i++){
            for(int j=0; j<cell[0].length-1; j++){  
                
                //If found lightning tree and probCatch is true
                if(get(i,j) == Cell.BLUE && random(probCatch)){
                    
                    //If current step is the step when the lightning tree burn
                    if(cell[i][j].getLightningStep()==0){
                        
                        //Burn the tree
                        cell[i][j].set(Cell.RED);
                        
                    //Else decrease the lightning step
                    }else{
                        cell[i][j].stepLightning();
                    }
                    
               //If the probCatch is false                         
                }else if(get(i,j) == Cell.BLUE){
                    //Change back to normal tree
                    cell[i][j].set(Cell.GREEN);
                }
                
                //Burn and spread if found a fire
                if((get(i,j) == Cell.RED && cellCheck[i][j] == false)){
                    cell[i][j].set(Cell.YELLOW);  
                    try{
                    spread("north",i,j,windLevel);      spread("south",i,j,windLevel);  
                    spread("west",i,j,windLevel);        spread("east",i,j,windLevel);  
                    }catch(Exception e){}
                }
            }
        }
    }     
    
    /**
     * Start the lightning to hit at random tree
     */
    public void lightning(){        
        int x = (int)(Math.random()*cell.length);
        int y = (int)(Math.random()*cell[0].length);
        if(random(probLightning) && get(x,y) == Cell.GREEN){
            cell[x][y].set(Cell.BLUE); 
            cell[x][y].setLightningStep(lightningStep);
        }
    }
        
     /**
     * Check if there is no burning or lightning tree anymore
     * @return true if there is still burning or lightning tree in the forest
     */
    public boolean finish(){
        for(int i=0;i<cell.length;i++){
            for(int j=0;j<cell[0].length;j++){
                if(get(i,j) == Cell.RED || get(i,j) ==Cell.BLUE)
                    return false;
            }
        } return true;
    }
    
    /**
     * Random a number
     * @param a double of probability
     * @return boolean of random number
     */
     public boolean random(double a){
        double rnd = Math.random()*100;
        return rnd<a;
    }

    /**
     * Print grid 
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
        if(observer!=null){
            observer.update(cell);
            if(!finish()){
                observer.updateStep(step);
            }
        } 
    }
}
