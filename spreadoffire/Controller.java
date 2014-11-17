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
 * @version 2014.11.17
 */
public class Controller extends JFrame {
    Model myModel;
    View myView;
    Thread startThread;
    JCheckBox windBox,windBoxN,windBoxS,windBoxW,windBoxE,lightingBox;
    JRadioButton wind0,wind1,wind2;
    JButton startButton,stopButton,resetButton,moveButton;
    JLabel probC,probT,probB,probL,ratio,empty,size,delay,step,note;
    JLabel description = new JLabel("Hover the mouse to see the description.");
    JSlider probCScale,probTScale,probBScale,probLScale,sizeScale;
    JTabbedPane settingTab;
    JTextField lightingStep;

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
        controller.setLayout(new GridLayout(15,1));
        
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
                                    myModel.setLightingStep(Integer.parseInt(lightingStep.getText()));                                    
                                    myModel.lighting();
                                    myModel.checkBurn();
                                    myModel.resetCheck();
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
                                        myModel.setLightingStep(Integer.parseInt(lightingStep.getText()));                                        
                                        myModel.lighting();
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
                            description.setText("ProbTree : The probability that a tree (burning or not burning) initially occupies a site.");
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
            
             //Create and Add the 5th row to controller panel - The probLighting
            JPanel controller5=new JPanel();
            controller5.setLayout(new GridLayout(1,2));
            controller.add(controller5);
            //Properties of 5th row
            {
                //Create and add the label panel
                JPanel controller5_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller5.add(controller5_1);
                //Properties of the label panel
                {                
                    //Add the name label
                    probL =new JLabel("ProbLighting : "+myModel.probLighting+"%");
                    probL.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("ProbLighting : The probability that the tree is struck by lightning.");
                        }
                    });
                    controller5_1.add(probL);                    
                }
                
                //Create and add the controller panel
                JPanel controller5_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller5.add(controller5_2); 
                //Properties of the controller panel
                {
                    //Create and Add the Similarity Slider from 0-100
                    probLScale = new JSlider(JSlider.HORIZONTAL,0, 100,0);
                    probLScale.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            //Set the probability
                            int newProbability=((JSlider)(e.getSource())).getValue();
                            myModel.setProbLighting(newProbability);
                            //Change the label
                            probL.setText("ProbLighting : "+myModel.probLighting+"%");
                        }
                    });
                    controller5_2.add(probLScale);
                }
            }         
            
            //Create and Add the 6th row to controller panel - The lighting steps
            JPanel controller6 = new JPanel();                
            controller6.setLayout(new GridLayout(1,2));
            controller.add(controller6);            
            //Properties of 6th row
            {
                //Create and add the label and checkbox panel
                final JPanel controller6_1=new JPanel(new FlowLayout(FlowLayout.LEFT));                     
                final JPanel controller6_2=new JPanel(new FlowLayout(FlowLayout.CENTER));                
                controller6.add(controller6_1);                          
                controller6.add(controller6_2);
                controller6_2.setVisible(false);
                //Properties of the label panel
                {                    
                    //Add the checkbox
                    lightingBox = new JCheckBox("Lighting step ");
                    lightingBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //If selected, the textfield for step will appear
                            if(lightingBox.isSelected()){
                                controller6_2.setVisible(true);
                            }else{
                                controller6_2.setVisible(false);
                            }
                        }
                    });
                    lightingBox.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Lighting step : Number of step that a tree needs to burn after hit by lighting. ");
                        }
                    });    
                    controller6_1.add(lightingBox);
                }                
                //Property of the step textfield panel
                {
                    //Add the step textfield
                    lightingStep = new JTextField("1");
                    lightingStep.setPreferredSize(new Dimension(50, 25));
                    lightingStep.setHorizontalAlignment(JTextField.CENTER);
                    controller6_2.add(lightingStep);
                    controller6_2.add(new JLabel("steps"));
                }
            }
            
            //Create and Add the 7th and 8th row to controller panel - The wind 
            final JPanel controller7=new JPanel();            
            final JPanel controller8=new JPanel();
            controller7.setLayout(new GridLayout(1,2));    
            controller8.setLayout(new GridLayout(1,4));            
            controller8.setVisible(false);
            controller.add(controller7);
            controller.add(controller8);
             //Properties of 7th row
            {
                //Create and add the label and wind level radiobox panel
                final JPanel controller7_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                final JPanel controller7_2=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller7_2.setLayout(new GridLayout(1,3));
                controller7_2.setVisible(false);
                controller7.add(controller7_1);
                controller7.add(controller7_2);
                {
                    //Add the checkbox
                    windBox = new JCheckBox("Wind");
                    windBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //If selected, 4 wind direction options will appear
                            if(windBox.isSelected()){                               
                                controller7_2.setVisible(true);
                                controller8.setVisible(true);
                                myModel.setWind("all", false);
                            }else{
                                controller7_2.setVisible(false);
                                controller8.setVisible(false);
                                myModel.setWind("all", true);
                            }
                        }
                    });
                    windBox.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Wind : Wind that control the direction of the fire.");
                        }
                    });    
                    controller7_1.add(windBox);
                }
                
                //Create panel for each wind level                
                ButtonGroup windGroup = new ButtonGroup();
                {    
                    //Wind level 0
                    JPanel controller7_2_1=new JPanel(new FlowLayout(FlowLayout.CENTER));
                    controller7_2.add(controller7_2_1);
                    {
                        wind0 = new JRadioButton("Level 0",true);
                        wind0.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(wind0.isSelected()){
                                    myModel.setWindLevel(0);
                                }
                            }
                        });
                        wind0.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Level 0 : No wind(will only change the direction of fire).");
                        }
                    });
                        windGroup.add(wind0);
                        controller7_2_1.add(wind0);
                    }
                    
                    //Wind level 1
                    JPanel controller7_2_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                    controller7_2.add(controller7_2_2);
                    {
                        wind1 = new JRadioButton("Level 1",false);
                        wind1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(wind1.isSelected()){
                                    myModel.setWindLevel(1);
                                }
                            }
                        });
                        wind1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Level 1 : Low level of wind.");
                        }
                    });
                        windGroup.add(wind1);
                        controller7_2_2.add(wind1);
                    }
                    
                    //Wind level 2
                    JPanel controller7_2_3=new JPanel(new FlowLayout(FlowLayout.CENTER));
                    controller7_2.add(controller7_2_3);
                    {
                        wind2 = new JRadioButton("Level 2",false);
                        wind2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(wind2.isSelected()){
                                    myModel.setWindLevel(2);
                                }
                            }
                        });
                        wind2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Level 2 : High level of wind.");
                        }
                    });
                        windGroup.add(wind2);
                        controller7_2_3.add(wind2);
                    }
                }
            }
            //Property of 8th row
            {
                //Create and add the north wind panel
                JPanel controller8_1=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller8.add(controller8_1);
                {
                    //Add the checkbox
                    windBoxN = new JCheckBox("North");
                    windBoxN.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(windBoxN.isSelected()){
                                myModel.setWind("north", true);
                            }else{
                                myModel.setWind("north", false);
                            }
                        }
                    });  
                    windBoxN.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Wind to the North.");
                        }
                    });   
                    controller8_1.add(windBoxN);
                }
                
                //Create and add the South wind panel
                JPanel controller8_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller8.add(controller8_2);
                {
                    //Add the checkbox
                    windBoxS = new JCheckBox("South");
                    windBoxS.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                             if(windBoxS.isSelected()){
                                myModel.setWind("south", true);
                            }else{
                                myModel.setWind("south", false);
                            }
                        }
                    });   
                    windBoxS.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Wind to the South.");
                        }
                    }); 
                    controller8_2.add(windBoxS);
                }
                
                //Create and add the West wind panel
                JPanel controller8_3=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller8.add(controller8_3);
                {
                    //Add the checkbox
                    windBoxW = new JCheckBox("West");
                    windBoxW.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                             if(windBoxW.isSelected()){
                                myModel.setWind("west", true);
                            }else{
                                myModel.setWind("west", false);
                            }
                        }
                    });                     
                    windBoxW.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Wind to the West.");
                        }
                    }); 
                    controller8_3.add(windBoxW);
                }
                
                 //Create and add the West wind panel
                JPanel controller8_4=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller8.add(controller8_4);
                {
                    //Add checkbox
                    windBoxE = new JCheckBox("East");
                    windBoxE.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                             if(windBoxE.isSelected()){
                                myModel.setWind("east", true);
                            }else{
                                myModel.setWind("east", false);
                            }
                        }
                    }); 
                    windBoxE.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            //Hover the mouse to see the description
                            description.setText("Wind to the East.");
                        }
                    }); 
                    controller8_4.add(windBoxE);
                }
            }            
            
            //Create and Add the 9th row to controller panel - The Size
            JPanel controller9=new JPanel();
            controller9.setLayout(new GridLayout(1,2));
            controller.add(controller9);
            //Properties of 9th row
            {
                //Create and add the label panel
                JPanel controller9_1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                controller9.add(controller9_1);
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
                    controller9_1.add(size);
                }
                
                //Create and add the controller panel
                JPanel controller9_2=new JPanel(new FlowLayout(FlowLayout.CENTER));
                controller9.add(controller9_2);
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
                    controller9_2.add(sizeScale);
                }
            }
            
            //Create and Add the 10th row to controller panel - Note
            JPanel note=new JPanel();
            note.setLayout(new GridLayout(1,1));
            note.add(new JLabel("*click reset button after change the setting."));              
            controller.add(note);
            
            //Create and Add the 11th row to controller panel - The description
            JPanel desc=new JPanel();
            desc.setLayout(new GridLayout(1,1));
            desc.add(description);
            controller.add(desc);
        }
        //Set the frame Visible
        setVisible(true);        
    }      
}
