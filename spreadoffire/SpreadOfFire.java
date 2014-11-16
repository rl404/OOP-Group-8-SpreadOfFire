package spreadoffire;

import java.util.Scanner;
import javax.swing.JFrame; 

/**
 * The main class of project from MVC pattern
 * 
 * @author OOPgroup8
 * @version 2014.10.29
 */
public class SpreadOfFire {
    
    static int width, height;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame myGUI=new Controller();
        //TUI();
    }
    
    public static void TUI(){
        System.out.println("Set the size of the forest (grid) : ");
        System.out.print("Width = ");     Scanner a = new Scanner(System.in);     int width = a.nextInt(); 
        System.out.print("Height = ");     Scanner b = new Scanner(System.in);     int height = b.nextInt(); 
        
        System.out.println("Set the probability of a tree in a cell catching fire : (0-100) ");
        Scanner c = new Scanner(System.in);     int probC = c.nextInt();
        
        //System.out.println("Set the density of forest : (0-1) ");
        //Scanner d = new Scanner(System.in);    int probT = d.nextInt();        
        
        Model grid = new Model(width,height,probC,0,100,0,0);     
        
        System.out.println("Initial grid");     grid.showGraph();
        
        int step=1;
        while(!grid.finish()){
            grid.checkBurn();
            System.out.println("Step "+step);
            grid.showGraph();
            step++;
            grid.resetCheck();
        }
        System.out.println("Total step = "+ (step-1));
    }
}
