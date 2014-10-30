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
 * @version 2014.10.28
 */
public class Controller extends JFrame{
    Model myModel;
    View myView;
    Thread startThread;
    JButton startButton,stopButton,resetButton,moveButton;
    JLabel probC,probT,probB,ratio,empty,size,delay,step;
    JSlider probCScale,probTScale,probBScale,sizeScale;

    /**
     * Create the GUI of project
     */
    public Controller(){
        //Create the main frame
        super("Spread of Fire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 470);
        setResizable(false);
        setLayout(new GridLayout(1, 2));
        
        //Create the model, the main process of project
        myModel=new Model();
        
        //Create the view, the output of main process of project
        int boxWidth=(int)((400)/myModel.width);
        int boxHeight=(int)(400/myModel.height);
        myView=new View(boxWidth,boxHeight);
        
        //Add the myView panel to the left
        add(myView);
        
        //Let myView be the observer of myModel
        myModel.addObserver(myView);
        
        //Create the controller panel
        JPanel controller=new JPanel();
        controller.setLayout(new GridLayout(6,1));
        
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
                                        myModel.checkBurn();
                                        myModel.resetCheck();
                                }  
                            };
                            startThread.start();
                        }
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
                controller1.add(resetButton);
            }
            
            //Create and Add the 2nd row to controller panel - The probability
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
                    controller2_1.add(new JLabel("ProbCatch : "));
                    
                    //Add the value label
                    probC =new JLabel(myModel.probCatch+"%");
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
                            probC.setText(myModel.probCatch+"%");
                        }
                    });
                    controller2_2.add(probCScale);
                }
            }
            
            //Create and Add the 3rd row to controller panel - The probability
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
                    controller3_1.add(new JLabel("ProbTree : "));
                    
                    //Add the value label
                    probT =new JLabel(myModel.probTree+"%");
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
                            probT.setText(myModel.probTree+"%");
                        }
                    });
                    controller3_2.add(probTScale);
                }
            }
            
            //Create and Add the 4th row to controller panel - The probability
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
                    controller4_1.add(new JLabel("ProbBurning : "));
                    
                    //Add the value label
                    probB =new JLabel(myModel.probBurning+"%");
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
                            probB.setText(myModel.probBurning+"%");
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
                    controller5_1.add(new JLabel("Size : "));
                
                    //Add the value label
                    size=new JLabel(""+myModel.width+"x"+myModel.height);
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
                            int boxSize=(int)((400)/newSize);
                            myView.setSize(boxSize, boxSize);
                            //Change the label
                            size.setText(""+myModel.width+"x"+myModel.height);
                            //Stop the Thread if alive
                            if(startThread!=null&&startThread.isAlive()){
                                startThread.stop();
                            }
                        }
                    });
                    controller5_2.add(sizeScale);
                }
            }            
        }
        //Set the frame Visible
        setVisible(true);
    }
}
