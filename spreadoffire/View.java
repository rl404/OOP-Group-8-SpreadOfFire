package spreadoffire;

import java.awt.*;
import javax.swing.*;

/**
 * The view class of project from MVC pattern
 * 
 * @author OOPgroup8
 * @version 2014.10.28
 */
public class View extends JPanel {
    Cell cell[][];
    int blockWidth,blockHeight;
    private int step;
    
    /**
     * Paint the cell
     */
    @Override
    public void paintComponent(Graphics g){
        //Fill the back ground with black
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 500);
        
        //Calculate the border width
        int border=(500-(blockWidth*cell.length))/2;
        
        //Don't paint if there is no cell
        if(cell==null)return;
        
        for(int i=0;i<cell.length;i++){
            for(int j=0;j<cell[0].length;j++){
                //Paint the cell
                g.setColor(cell[i][j].getColor());
                g.fillRect(border+j*blockWidth,border+i*blockHeight,blockWidth,blockHeight);
                //Paint the cell border
                g.setColor(Color.BLACK);
                g.drawRect(border+j*blockWidth,border+i*blockHeight,blockWidth,blockHeight);
            }
        }

        //Paint the "Step" label 
        g.fillRect(0, 500, 500, 25);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.drawString("Step: "+step, 225, 520);
    }
    
    /**
     * Constructor - create the view panel
     * @param blockWidth
     * @param blockHeight
     */
    public View(int blockWidth,int blockHeight) {
        this.blockWidth=blockWidth;
        this.blockHeight=blockHeight;
        cell=null;
    }
    
    /**
     * Set the block size
     * @param blockWidth
     * @param blockHeight
     */
    public void setSize(int blockWidth,int blockHeight) {
        this.blockWidth=blockWidth;
        this.blockHeight=blockHeight;
    }

    /**
     * Update the cell
     * @param cell
     */
    public void update(Cell cell[][]){
        this.cell=cell;
        repaint();
    }

    public void updateStep(int step) {
        this.step=step;
    }
}
