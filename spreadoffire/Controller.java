package testspreadoffire;

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
    ModelClass myModel;
    View myView;
    Thread startThread;
    JButton startButton,stopButton,moveButton;
    JLabel probability,ratio,empty,size,delay;
    JSlider probabilityScale,sizeScale;

    /**
     * Create the GUI of project
     */
    public Controller(){
        //Create the main frame
        super("Spread of Fire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 430);
        setResizable(false);
        setLayout(new GridLayout(1, 2));
        
        //Create the model, the main process of project
        myModel=new ModelClass();
        
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
                stopButton=new JButton("Stop");
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
                JButton resetButton=new JButton("Reset");
                resetButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        //If There is an alive Thread, stop it
                        if(startThread!=null&&startThread.isAlive()){
                            startThread.stop();
                        }
                        //Reset the main process
                        myModel.fieldReset();
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
                    controller2_1.add(new JLabel("Probability : "));
                    
                    //Add the value label
                    probability=new JLabel(""+myModel.probability);
                    controller2_1.add(probability);
                }
                
                //Create and add the controller panel
                JPanel controller2_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller2.add(controller2_2);
                //Properties of the controller panel
                {
                    //Create and Add the Similarity Slider from 0-100
                    probabilityScale = new JSlider(JSlider.HORIZONTAL,0, 100, 50);
                    probabilityScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the probability
                            int newProbability=((JSlider)(e.getSource())).getValue();
                            myModel.setProbability(newProbability);
                            //Change the label
                            probability.setText(""+myModel.probability);
                        }
                    });
                    controller2_2.add(probabilityScale);
                }
            }
            
            //Create and Add the 3rd row to controller panel - The Size
            JPanel controller3=new JPanel();
            controller3.setLayout(new GridLayout(1,2));
            controller.add(controller3);
            //Properties of 3rd row
            {
                //Create and add the label panel
                JPanel controller3_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller3.add(controller3_1);
                {
                    //Add the name label
                    controller3_1.add(new JLabel("Size : "));
                
                    //Add the value label
                    size=new JLabel(""+myModel.width+"x"+myModel.height);
                    controller3_1.add(size);
                }
                
                //Create and add the controller panel
                JPanel controller3_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller3.add(controller3_2);
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
                    controller3_2.add(sizeScale);
                }
            }
            
        }
        //Set the frame Visible
        setVisible(true);
    }
}
