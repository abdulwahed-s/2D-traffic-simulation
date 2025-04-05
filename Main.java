import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Main extends JFrame {

    private static final int FRAME_WIDTH = 1920;//
    private static final int FRAME_HEIGHT = 1080;

    public Main() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);//maximise the frame state to make it fullscreen
        ImageIcon icon = new ImageIcon("assets/spider.png");//Create variable type ImageIcon named icon
        JMenuBar menuBar = getBackgroundMenuBar();//use the function getBackgroundMenuBar() to create menu bar
        setJMenuBar(menuBar);//set the menu bar to be (menuBar)
        setIconImage(icon.getImage());//set app to image to be (icon)
        setLayout(new BorderLayout());//set the layout to be BorderLayout()
        setSize(FRAME_WIDTH, FRAME_HEIGHT);//set frame size to be width: FRAME_WIDTH,height: FRAME_HEIGHT
        setTitle("Computer Graphics Course Project");//set frame title
        setLocationRelativeTo(null); //to put the frame in the center of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sit the closing button to close the app on clicked
        AnimationPanel panel = new AnimationPanel();// Create a custom panel
        add(panel, BorderLayout.CENTER);// Add the panel to the center of the frame using BorderLayout
        pack();// Sizes the frame so that all its contents are at or above their preferred sizes
        setVisible(true);//set frame to be visible
    }

    //function to create Menu Bar
    private JMenuBar getBackgroundMenuBar() {
        JMenuBar menuBar = new JMenuBar();// Create an instance of JMenuBar
        JMenu fileMenu = new JMenu("File");//create new JMenu named fileMenu
        JMenu helpMenu = new JMenu("Help");//create new JMenu named helpMenu

        JMenuItem exitItem = new JMenuItem("Exit");//create new JmenuItem named exitItem
        exitItem.addActionListener(e -> System.exit(0));//set function on clicked on the exitItem to close the frame
        fileMenu.add(exitItem);//add the exitItem to fileMenu

        JMenuItem minimizeButton = new JMenuItem("Minimize");//create new JmenuItem named minimizeButton
        minimizeButton.addActionListener(e  -> setState(Frame.ICONIFIED));//set function on clicked on the minimizeButton to minimize the frame
        fileMenu.add(minimizeButton);//add minimizeButton to fileMenu

        JMenuItem aboutItem = new JMenuItem("About");//create new JmenuItem named aboutItem
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Computer Graphics Course Project\nBy Abdulwahed Rifaat"));//set function on clicked on the aboutItem to show message as alert
        helpMenu.add(aboutItem);//add aboutItem to helpMenu

        menuBar.add(fileMenu);//add File section to the menu bar
        menuBar.add(helpMenu);//add Help section to the menu bar
        return menuBar;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());//change the frame look with FlatLaf
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");//if FlatLaf isn't installed itll return this message
        }

        new Main();
    }
}

class AnimationPanel extends JPanel implements Runnable {
    private int carX = 30;
    private int car2X = -400;
    private int trafficLightState = 0;
    private int person1Y = 380;
    private int person2Y = 380;
    private int person3Y = 380;
    private int person4Y = 380;
    private boolean person1Moving = true;
    private boolean person2Moving = false;
    private boolean person3Moving = false;
    private boolean person4Moving = false;
    private boolean carMoving = false;
    private boolean car2Moving = false;
    private int person4HitY = 0;
    private int person4HitX = 1520;
    private double person4Angle = 0;
    private boolean person4Hit = false;

    private Thread animationThread;// Thread to handle the animation logic

    public AnimationPanel() {
        setPreferredSize(new Dimension(1920, 1080));// set panel size
        this.setBackground(Color.white);//set color to be white
        animationThread = new Thread(this);//Create a new thread with this panel as the Runnable
        animationThread.start();//start the animation thread
    }

    public void run() {
        try {
            // Person 1 crosses
            while (person1Y < 880) {
                person1Y += 2;
                repaint();
                Thread.sleep(30);
            }
            person1Moving = false;
            Thread.sleep(1000);

            // Person 2 crosses
            person2Moving = true;
            while (person2Y < 880) {
                person2Y += 2;
                repaint();
                Thread.sleep(30);
            }
            person2Moving = false;
            Thread.sleep(1000);

            // Traffic light to yellow
            trafficLightState = 1;
            repaint();
            Thread.sleep(500);

            // Traffic light to green
            trafficLightState = 2;
            repaint();
            Thread.sleep(500);

            // Car moves
            carMoving = true;
            while (carX < 2000) {
                carX += 5;
                repaint();
                Thread.sleep(30);
            }
            carMoving = false;
            Thread.sleep(500);

            // Traffic light to red
            trafficLightState = 0;
            repaint();
            Thread.sleep(1000);

            // Person 3 crosses
            person3Moving = true;
            while (person3Y < 880) {
                person3Y += 2;
                repaint();
                Thread.sleep(30);
            }
            person3Moving = false;
            Thread.sleep(1000);

            // Person 4 starts crossing
            person4Moving = true;

            while (person4Y < 880) {
                person4Y += 2;

                // Start car2 when person4 reaches middle of road
                if (person4Y > 500 && !car2Moving) {
                    car2Moving = true;
                }

                // Move car2 very fast once started
                if (car2Moving) {
                    car2X += 30;
                }

                // Check for collision at impact point
                if (!person4Hit && person4Y > 600 && car2X > 1400 && car2X < 1600) {
                    person4Hit = true;
                    person4HitY = person4Y;
                    person4HitX = 1520;
                }

                // If hit, make person4 fly away
                if (person4Hit) {
                    person4Angle += 0.5;
                    person4HitX += 35;
                    person4HitY -= 20;

                    // Stop when completely out of scene
                    if (person4HitX > 2200 || person4HitY < -200) {
                        break;
                    }
                }

                repaint();
                Thread.sleep(30);

                // Stop if car2 exits scene
                if (car2X > 2500) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw gradient blue sky
        GradientPaint skyGradient = new GradientPaint(0, 0, new Color(135, 206, 250), 0, 413, new Color(25, 25, 112));//sky color
        g2.setPaint(skyGradient);
        g2.fillRect(0, 0, 1920, 413);//draw sky


        GradientPaint grassGradient = new GradientPaint(0, 413, new Color(34, 139, 34),0, 540, new Color(0, 100, 0));//grass color
        g2.setPaint(grassGradient);
        g2.fillRect(0, 413, 1920, 127);//draw grass

        GradientPaint sidewalkGradient = new GradientPaint(0, 588, new Color(240, 240, 240),0, 753, new Color(200, 200, 200));//street sidewalk color
        g2.setPaint(sidewalkGradient);
        g2.fillRect(0, 540, 1920, 48);//draw street sidewalk

        GradientPaint streetGradient = new GradientPaint(0, 540, new Color(20, 20, 20), 0, 588, new Color(50, 50, 50));//street color
        g2.setPaint(streetGradient);
        g2.fillRect(0, 588, 1920, 165);//draw street

        g2.setColor(Color.white);//color of lane lines
        int x = 20;
        for(int i = 0; i < 20; i++) {
            g2.fillRect(x, 650, 140, 40);
            x += 200;
        }//draw the lane lines

        g2.setPaint(sidewalkGradient);//set color as same as first sidewalk
        g2.fillRect(0, 753, 1920, 48);//draw street sidewalk 2

        GradientPaint roadBaseGradient = new GradientPaint(0, 801, new Color(80, 80, 80),0, 1080, new Color(40, 40, 40));// Road base color
        g2.setPaint(roadBaseGradient);
        g2.fillRect(0, 801, 1920, 279);//draw the road base



        g2.setColor(new Color(220, 220, 220)); // Light gray for traffic light body
        g2.fillRect(380, 820, 300, 100);//draw traffic light body

        //red light
        g2.setColor(trafficLightState == 0 ? Color.red : new Color(80, 80, 80));
        g2.fillOval(395, 840, 60, 60);

        // Yellow light
        g2.setColor(trafficLightState == 1 ? Color.yellow : new Color(80, 80, 80));
        g2.fillOval(495, 840, 60, 60);

        // Green light
        g2.setColor(trafficLightState == 2 ? Color.green : new Color(80, 80, 80));
        g2.fillOval(595, 840, 60, 60);

        g2.setColor(new Color(200, 200, 200));// traffic light stick color
        g2.fillRect(517, 920, 20, 120);//draw  traffic light stick

        drawTrees(g2);//draw trees using  drawTrees() function
        // Draw people
        g2.setStroke(new BasicStroke(12.0f));
        drawPerson(g2, 1720, person1Y, 1);
        drawPerson(g2, 1620, person2Y, 2);
        drawPerson(g2, 1820, person3Y, 3);

        // Draw person4 normally
        if (!person4Hit) {
            drawPerson(g2, 1520, person4Y, 4);
        } else {
        // Draw person4 flying
            AffineTransform oldTransform = g2.getTransform(); // Save the current graphics transformation
            g2.rotate(person4Angle, person4HitX, person4HitY); // Rotate the canvas around the person's position
            drawPerson(g2, person4HitX, person4HitY, 4); // Draw the rotated person
            g2.setTransform(oldTransform); // Restore the original transformation to avoid affecting other drawings
        }

        // Draw cars
        g2.setStroke(new BasicStroke(6.0f));
        drawCar1(g2, carX);
        drawCar2(g2, car2X);


    }

    //function to draw persons with switch case for every person number
    private void drawPerson(Graphics2D g2, int x, int y, int personNum) {
        GradientPaint gradient;
        switch(personNum) {
            case 1: gradient = new GradientPaint(x, y, new Color(255, 200, 150), x, y+110, new Color(255, 150, 100)); break;
            case 2: gradient = new GradientPaint(x, y, new Color(255, 182, 193), x, y+110, new Color(255, 105, 180)); break;
            case 3: gradient = new GradientPaint(x, y, new Color(150, 200, 255), x, y+110, new Color(100, 150, 255)); break;
            case 4: gradient = new GradientPaint(x, y, new Color(72, 61, 139),x, y + 110, new Color(123, 104, 238));break;
            default: gradient = new GradientPaint(x, y, Color.PINK, x, y+110, Color.PINK);
        }

        g2.setPaint(gradient);
        g2.drawLine(x, y, x, y + 70);
        g2.drawLine(x, y + 70, x - 30, y + 110);
        g2.drawLine(x, y + 70, x + 20, y + 110);
        g2.drawLine(x - 40, y + 35, x + 40, y + 35);
        g2.fillOval(x - 30, y - 40, 60, 60);
    }

    //function to draw first car
    private void drawCar1(Graphics2D g2, int x) {
        GradientPaint carBodyGradient = new GradientPaint(x, 580, new Color(255, 105, 180), x, 680, new Color(199, 21, 133));//car body color
        g2.setPaint(carBodyGradient);
        g2.fillRoundRect(x, 580, 300, 100, 20, 20);

        // Chrome trim
        GradientPaint trimGradient = new GradientPaint(x, 590, new Color(220, 220, 220), x, 670, new Color(180, 180, 180));
        g2.setPaint(trimGradient);
        g2.drawRoundRect(x+5, 585, 290, 90, 15, 15);


        GradientPaint roofGradient = new GradientPaint(x+10, 540, new Color(100, 149, 237, 150),x+10, 580, new Color(25, 25, 112, 200));//car roof color
        g2.setPaint(roofGradient);
        for(int i = 0; i < 4; i++) {
            int triangleX = x + 10 + i * 70;
            g2.fillPolygon(
                    new int[]{triangleX, triangleX+35, triangleX+70},
                    new int[]{580, 540, 580},
                    3);
        }//loop to put roof on the car

        // Wheels with detailed rims
        g2.setColor(new Color(30, 30, 30));
        g2.fillOval(x+15, 680, 70, 70);
        g2.fillOval(x+215, 680, 70, 70);

        GradientPaint rimGradient = new GradientPaint(x+15, 680, new Color(192, 192, 192), x+85, 750, new Color(105, 105, 105));
        g2.setPaint(rimGradient);
        g2.fillOval(x+25, 690, 50, 50);
        g2.fillOval(x+225, 690, 50, 50);

        g2.setColor(new Color(70, 70, 70));
        g2.fillOval(x+40, 705, 20, 20);
        g2.fillOval(x+240, 705, 20, 20);
    }

    //function to draw the second card
    private void drawCar2(Graphics2D g2, int x) {
        // Car body
        GradientPaint carBodyGradient = new GradientPaint(x, 580, new Color(50, 50, 50), x, 680, new Color(20, 20, 20));
        g2.setPaint(carBodyGradient);
        g2.fillRoundRect(x, 580, 300, 100, 20, 20);

        // Silver trim
        GradientPaint trimGradient = new GradientPaint(x, 590, new Color(200, 200, 200), x, 670, new Color(150, 150, 150));
        g2.setPaint(trimGradient);
        g2.setStroke(new BasicStroke(8f));
        g2.drawRoundRect(x+5, 585, 290, 90, 15, 15);

        //Dark roof
        GradientPaint roofGradient = new GradientPaint(x+10, 540, new Color(50, 50, 50, 180), x+10, 580, new Color(10, 10, 10, 200));
        g2.setPaint(roofGradient);
        //roof loop
        for(int i = 0; i < 4; i++) {
            int triangleX = x + 10 + i * 70;
            g2.fillPolygon(
                    new int[]{triangleX, triangleX+35, triangleX+70},
                    new int[]{580, 540, 580},
                    3);
        }

        // Wheels drawing
        g2.setColor(Color.BLACK);
        g2.fillOval(x+15, 670, 70, 70);
        g2.fillOval(x+215, 670, 70, 70);
        g2.setColor(new Color(180, 180, 180));
        g2.fillOval(x+25, 680, 50, 50);
        g2.fillOval(x+225, 680, 50, 50);
    }
    //function to draw trees
    private void drawTrees(Graphics2D g2) {
        int xs = 100;
        for(int i = 0; i < 13; i++) {
            if(i % 2 == 0) {

                g2.setColor(new Color(101, 67, 33)); // Wood brown
                g2.setStroke(new BasicStroke(5f));
                g2.drawLine(xs, 345, xs, 450);


                GradientPaint leavesGradient = new GradientPaint(xs-25, 380, new Color(50, 200, 50),xs+25, 430, new Color(0, 120, 0));//leavs color
                g2.setPaint(leavesGradient);

                g2.fillArc(xs, 380, 50, 50, 180, -180);
                g2.fillArc((xs-50), 380, 50, 50, 180, -180);
                g2.fillArc(xs, 350, 50, 50, 180, -140);
                g2.fillArc((xs-50), 350, 50, 50, 140, -150);

            } else {
                GradientPaint trunkGradient = new GradientPaint(xs, 345, new Color(101, 67, 33), xs, 450, new Color(60, 40, 20));
                g2.setPaint(trunkGradient);
                g2.setStroke(new BasicStroke(8f));
                g2.drawLine(xs, 345, xs, 450);


                g2.setColor(new Color(0, 100, 0)); // Dark green base
                g2.fillOval(xs-60, 330, 120, 100);

                g2.setColor(new Color(0, 150, 0)); // Medium green
                g2.fillOval(xs-50, 340, 100, 80);

                g2.setColor(new Color(50, 200, 50)); // Light green highlights
                g2.fillOval(xs-40, 350, 80, 60);
            }

            xs += 150;
        }
    }
    }
