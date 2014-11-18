package spreadoffire;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The controller class of project from MVC pattern
 * 
 * @author OOPgroup8
 * @version 1.0 2014.11.18
 */
public class Controller extends JFrame {
    Model myModel;
    View myView;
    Thread startThread;
    JButton startButton,stopButton,resetButton,moveButton;
    JLabel probC,probT,probB,ratio,empty,size,delay,step,note;
    JLabel description = new JLabel("Hover the mouse to see the description.");
    JSlider probCScale,probTScale,probBScale,sizeScale;

    /**
     * Create the GUI of project
     */
    public Controller (){
        //Create the main frame
        super("Spread of Fire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setResizable(true);
        setLayout(new GridLayout(1, 2));
        
        //Create the model, the main process of project
        myModel=new Model();
        
        //Create the view, the output of main process of project
        int boxWidth=(int)((getWidth()/2-25)/myModel.width);
        myView=new View(boxWidth,boxWidth);
        
        //Add the myView panel to the left
        add(myView);
        
        //Let myView be the observer of myModel
        myModel.addObserver(myView);       
        
        //Create the controller panel
        JPanel controller=new JPanel();
        controller.setLayout(new GridLayout(7,1));
        
        //Add the controller panel to the right
        add(controller);
        
        //Properties of controller panel
        {
            //Create and Add the 1st row to controller panel
            JPanel controller1=new JPanel();
            controller.add(controller1);
            //Properties of 1st row
            {                    
                //Create and Add the moveButton
                startButton=new JButton("Move");
                startButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        //If there is no Thread or Thread is dead, create new Thread and start
                        if(startThread==null||!startThread.isAlive()){
                            startThread=new Thread() {  
                                public void run() { 
                                    if(!myModel.finish()){
                                        myModel.checkBurn();
                                        myModel.resetCheck();
                                    }
                                }  
                            };
                            startThread.start();
                        }
                    }
                });
                startButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        //Hover the mouse to see the description
                        description.setText("Move Button : Run the fire step by step.");
                    }
                });
                controller1.add(startButton);
                
                //Create and Add the startButton
                startButton=new JButton("Start");
                startButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        //If there is no Thread or Thread is dead, create new Thread and start
                        if(startThread==null||!startThread.isAlive()){
                            startThread=new Thread() {  
                                public void run() { 
                                    while(!myModel.finish()){ 
                                        myModel.checkBurn();
                                        myModel.resetCheck();
                                    }
                                }  
                            };
                            startThread.start();
                        }
                    }
                });                
                startButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        //Hover the mouse to see the description                        
                        description.setText("Start Button : Run the fire automatically.");
                    }
                });
                controller1.add(startButton);
                
                //Create and Add the stopButton
                stopButton=new JButton("Pause");
                stopButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        //If There is an alive Thread, stop it
                        if(startThread!=null&&startThread.isAlive()){
                            startThread.stop();
                        }
                    }
                });                
                stopButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {                        
                        //Hover the mouse to see the description
                        description.setText("Pause Button : Pause the movement of the fire.");
                    }
                });
                controller1.add(stopButton);
                
                //Create and Add the resetButton
                resetButton=new JButton("Reset");
                resetButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        //If There is an alive Thread, stop it
                        if(startThread!=null&&startThread.isAlive()){
                            startThread.stop();
                        }
                        //Reset the main process
                        myModel.resetGrid();
                        myModel.resetCheck();
                    }
                });                
                resetButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        //Hover the mouse to see the description
                        description.setText("Reset Button : Reset the state of the forest or apply the new setting.");
                    }
                });
                controller1.add(resetButton);
            }   
            
            //Create and Add the 2nd row to controller panel - The probCatch
            JPanel controller2=new JPanel();
            controller2.setLayout(new GridLayout(1,2));
            controller.add(controller2);
            //Properties of 2nd row
            {
                //Create and add the label panel
                JPanel controller2_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller2.add(controller2_1);
                //Properties of the label panel
                {
                    //Add the name label               
                    probC =new JLabel("ProbCatch : "+myModel.probCatch+"%");                       
                    probC.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("ProbCatch :  The probability of a tree in a cell catching fire if a tree in a neighboring cell is on fire. ");
                        }
                    });
                    controller2_1.add(probC);                    
                }
                
                //Create and add the controller panel
                JPanel controller2_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller2.add(controller2_2);
                //Properties of the controller panel
                {
                    //Create and Add the Similarity Slider from 0-100
                    probCScale = new JSlider(JSlider.HORIZONTAL,0, 100, 50);
                    probCScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the probability
                            int newProbability=((JSlider)(e.getSource())).getValue();
                            myModel.setProbCatch(newProbability);
                            //Change the label
                            probC.setText("ProbCatch : "+myModel.probCatch+"%");
                        }
                    });
                    controller2_2.add(probCScale);
                }
            }
            
            //Create and Add the 3rd row to controller panel - The probTree
            JPanel controller3=new JPanel();
            controller3.setLayout(new GridLayout(1,2));
            controller.add(controller3);
            //Properties of 3rd row
            {
                //Create and add the label panel
                JPanel controller3_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller3.add(controller3_1);
                //Properties of the label panel
                {
                    //Add the name label
                    probT =new JLabel("ProbTree : "+myModel.probTree+"%");
                    probT.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("ProbTree : The probability that a tree initially occupies a site.");
                        }
                    });
                    controller3_1.add(probT);                    
                }
                
                //Create and add the controller panel
                JPanel controller3_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller3.add(controller3_2); 
                //Properties of the controller panel
                {
                    //Create and Add the Similarity Slider from 0-100
                    probTScale = new JSlider(JSlider.HORIZONTAL,0, 100, 100);
                    probTScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the probability
                            int newProbability=((JSlider)(e.getSource())).getValue();
                            myModel.setProbTree(newProbability);
                            //Change the label
                            probT.setText("ProbTree : "+myModel.probTree+"%");
                        }
                    });
                    controller3_2.add(probTScale);
                }
            }
            
            //Create and Add the 4th row to controller panel - The probBurning
            JPanel controller4=new JPanel();
            controller4.setLayout(new GridLayout(1,2));
            controller.add(controller4);
            //Properties of 4th row
            {
                //Create and add the label panel
                JPanel controller4_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller4.add(controller4_1);
                //Properties of the label panel
                {
                    //Add the name label
                    probB =new JLabel("ProbBurning : "+myModel.probBurning+"%");
                    probB.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("ProbBurning : The probability that the tree is initially burning, if a site has a tree.");
                        }
                    });
                    controller4_1.add(probB);                    
                }
                
                //Create and add the controller panel
                JPanel controller4_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller4.add(controller4_2); 
                //Properties of the controller panel
                {
                    //Create and Add the Similarity Slider from 0-100
                    probBScale = new JSlider(JSlider.HORIZONTAL,0, 100,0);
                    probBScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the probability
                            int newProbability=((JSlider)(e.getSource())).getValue();
                            myModel.setProbBurning(newProbability);
                            //Change the label
                            probB.setText("ProbBurning : "+myModel.probBurning+"%");
                        }
                    });
                    controller4_2.add(probBScale);
                }
            }      
             
            //Create and Add the 5th row to controller panel - The Size
            JPanel controller5=new JPanel();
            controller5.setLayout(new GridLayout(1,2));
            controller.add(controller5);
            //Properties of 5th row
            {
                //Create and add the label panel
                JPanel controller5_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller5.add(controller5_1);
                {
                    //Add the name label
                    size=new JLabel("Size : "+myModel.width+"x"+myModel.height);
                     size.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Size : Size of the forest (width x height).");
                        }
                    });
                    controller5_1.add(size);
                }
                
                //Create and add the controller panel
                JPanel controller5_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller5.add(controller5_2);
                //Properties of the controller panel
                {
                    //Create and Add the Size Slider from 10x10 - 50x50
                    sizeScale = new JSlider(JSlider.HORIZONTAL,10, 50, 30);
                    sizeScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the Size in myModel
                            int newSize=((JSlider)(e.getSource())).getValue();
                            myModel.setSize(newSize,newSize);
                            //Set the Size in myView
                            int boxSize=(int)((getWidth()/2-25)/newSize);
                            myView.setSize(boxSize, boxSize);
                            //Change the label
                            size.setText("Size : "+myModel.width+"x"+myModel.height);
                            //Stop the Thread if alive
                            if(startThread!=null&&startThread.isAlive()){
                                startThread.stop();
                            }
                        }
                    });
                    controller5_2.add(sizeScale);
                }
            }
            
            //Create and Add the 6th row to controller panel - Note
            JPanel note=new JPanel();
            note.setLayout(new GridLayout(1,1));
            note.add(new JLabel("*click reset button after change the setting."));              
            controller.add(note);
            
            //Create and Add the 7th row to controller panel - The description
            JPanel desc=new JPanel();
            desc.setLayout(new GridLayout(1,1));
            desc.add(description);
            controller.add(desc);
        }
        //Set the frame Visible
        setVisible(true);        
    }      
}
