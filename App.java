
// App.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    static String user;
    static int difficulty;
    private int currentAnimationDuration;
    private int currentPauseAfterSwap;

    private static final int minimumAnimationTime = 100;
    private static final int minimumPauseTime = 10;
    int score;
    Main cups;
    private Image cupIcon;
    private Image background;
    private Image background2;
    private Image ballIcon;
    JLabel scoreLabel;

    int cupCount;
    JButton[] buttons;
    boolean canGuess = false;
    JLabel ballLabel;
    int ballCupIndexVisual;

    private int stepsCompleted;
    private int totalShuffleSteps;

    // App constructor initializes game
    public App(String username, int difficulty) {
        score = 0;
        App.user = username;
        App.difficulty = difficulty;

        // Determine number of cups based on difficulty
        if(difficulty == 0){
            cupCount = 3;
        } else {
            cupCount = 5;
        }

        cups = new Main(new Random(), cupCount);
        currentAnimationDuration = 400;
        currentPauseAfterSwap = 50;
        background = new ImageIcon("Background.jpg").getImage();
        background2 = new ImageIcon("Background2.jpg").getImage();
        cupIcon = new ImageIcon("Cup.jpg").getImage();
        ballIcon = new ImageIcon("Ball.jpg").getImage();
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Draw background images based on difficulty
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                //I don't see a point in this existing. Mess with it a bit to see if it's needed.
                super.paintComponent(g);
                if(difficulty == 0){
                    g.drawImage(new ImageIcon("Background.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
                }
                else {
                    g.drawImage(new ImageIcon("Background2.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
                }
                
            }
        };

        panel.setLayout(null);
        panel.setOpaque(false);

        // Create and configure label to display player's name and score
        scoreLabel = new JLabel(username + " - " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(310, 100, 500, 50);
        panel.add(scoreLabel);

        buttons = new JButton[cupCount];

        int y = 250;
        int spacing;
        int startX;

        // Adjust spacing based on difficulty
        if (difficulty == 0) {
            spacing = 150;
        } else {
            spacing = 110;
        }
        // Calculate the starting position
        int totalWidthOfCups = cupCount * 100;
        int totalSpacingWidth = (cupCount - 1) * spacing;
        if (cupCount == 1)
            totalSpacingWidth = 0;
        startX = (getWidth() - (totalWidthOfCups + (cupCount - 1) * (spacing - 100))) / 2;

        // Create and configure a JButton for each cup
        for (int i = 0; i < cupCount; i++) {
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
                }
            };
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);

            // Add an ActionListener to handle clicks 
            btn.addActionListener(e -> {
                if (!canGuess)
                    return;

                JButton clickedButton = (JButton) e.getSource();
                handleGuess(clickedButton);
            });

            int x = startX + i * spacing;
            btn.setBounds(x, y, 100, 100);
            buttons[i] = btn;
            panel.add(btn);
        }

        ImageIcon ballImageIcon = null;
        ballImageIcon = new ImageIcon(ballIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ballLabel = new JLabel(ballImageIcon);
        ballLabel.setBounds(0, 0, 30, 30);
        ballLabel.setVisible(false);
        panel.add(ballLabel);

        add(panel);

    }

    // Starts the game and shuffles the cups
    public void startGame() {
        canGuess = false;
        ballLabel.setVisible(false);

        cups.shuffle();

        ballCupIndexVisual = -1;
        ArrayList<Cup> currentCupList = cups.getCupList();
        for (int i = 0; i < buttons.length; i++) {
            if (currentCupList != null && i < currentCupList.size() && currentCupList.get(i).hasBall()) {
                ballCupIndexVisual = i;
                break;
            }
        }

        if (ballCupIndexVisual != -1) {
            showBallUnderCup(ballCupIndexVisual);
            ballLabel.setVisible(true);

            int showBallDuration = 1500;
            Timer showBallTimer = new Timer(showBallDuration, e -> {
                ballLabel.setVisible(false);
                shuffleSteps();
            });
            showBallTimer.setRepeats(false);
            showBallTimer.start();
        } else {
            System.err
                    .println("Error: Could not find the initial visual index of the cup with the ball after shuffle.");
            shuffleSteps();
        }
    }

    // Handles guessing
    private void handleGuess(JButton clickedButton) {
        if (!canGuess) 
        return; 
        int guessedIndex = -1;
   
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clickedButton) {
                guessedIndex = i;
                break; 
            }
        }
   
        if (guessedIndex != -1) {
            ArrayList<Cup> currentCupList = cups.getCupList();
            
            if (currentCupList.get(guessedIndex).hasBall()) {
                score++; 
                scoreLabel.setText(user + " - " + score); 
                showBallUnderCup(guessedIndex);
                ballLabel.setVisible(true); 

                // Handles when cups speed up
                currentAnimationDuration = Math.max(minimumAnimationTime, currentAnimationDuration - 25);
                currentPauseAfterSwap = Math.max(minimumPauseTime, currentPauseAfterSwap - 5);         
                System.out.println("Correct! Speed increased. Next shuffle duration: " + currentAnimationDuration + "ms, pause: " + currentPauseAfterSwap + "ms");
   
                Timer newRoundTimer = new Timer(2000, e -> startGame());
                newRoundTimer.setRepeats(false); 
                newRoundTimer.start();
   
            } else {
                if (ballCupIndexVisual >= 0 && ballCupIndexVisual < buttons.length) {
                   showBallUnderCup(ballCupIndexVisual); 
                   ballLabel.setVisible(true); 
                } else {
                    System.err.println("Error: Ball's final visual index (" + ballCupIndexVisual + ") is invalid on game over.");
                }
   
                JOptionPane.showMessageDialog(this, "Game over. Your final score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
   
                if (cups != null) {
                   cups.saveScore(user, score); 
                } else {
                    System.err.println("Error: Main logic object is null. Cannot save score.");
                }
   
                SwingUtilities.invokeLater(() -> {
                    System.exit(0);
                });
            }
        } else {
            System.err.println("Error: Clicked button not found in buttons array. This should not happen with proper event handling.");
        }
    }

    // Handling for showing the ball under the cup at beginning of round and after
    private void showBallUnderCup(int cupIndex) {
        if (cupIndex >= 0 && cupIndex < buttons.length) {
            JButton cupButton = buttons[cupIndex]; 
            Point cupLocation = cupButton.getLocation(); 

            int ballX = cupLocation.x + (cupButton.getWidth() - ballLabel.getWidth()) / 2;
            int ballY = cupLocation.y + cupButton.getHeight() - (ballLabel.getHeight() / 2); 

            ballLabel.setLocation(ballX, ballY);
            
        } else {

            System.err.println("Error: Attempted to position ball under invalid cup index: " + cupIndex);
        }
    }

    // Handles how many shuffles are done based on difficulty
    private void shuffleSteps() {
        stepsCompleted = 0; 
        
        if(difficulty == 0){
            totalShuffleSteps =10;
        }
        else {
            totalShuffleSteps = 20;
        }
        nextSwap();
    }

    // Handles the next step of the shuffling animation
    private void nextSwap() {
        if (stepsCompleted < totalShuffleSteps) {
            Random random = new Random();
            int i = random.nextInt(cupCount);
            int j = random.nextInt(cupCount);

            while (j == i) {
                j = random.nextInt(cupCount);
            }

            int prevBallIndex = ballCupIndexVisual;
   
            cups.swapCupsByIndex(i, j); 
   
            if (ballCupIndexVisual == i) {
                ballCupIndexVisual = j;
            }
 
            else if (ballCupIndexVisual == j) {
                ballCupIndexVisual = i;
            }

            int animationDuration = currentAnimationDuration; 
            int pauseAfterSwap = currentPauseAfterSwap;   

            // Swaps cups
            Swap(i, j, animationDuration, () -> {
   
                 Timer pauseTimer = new Timer(pauseAfterSwap, e -> {
                     stepsCompleted++; 
                     nextSwap(); 
                 });
                 pauseTimer.setRepeats(false); 
                 pauseTimer.start(); 
            }, prevBallIndex); 
   
        } else {
            canGuess = true; 
            System.out.println("Shuffling complete. Make your guess!");
        }
    }

    // Handles swapping the buttons and ball
    private void Swap(int i, int j, int duration, Runnable onComplete, int prevBallIndex) {
        JButton btn1 = buttons[i];
        JButton btn2 = buttons[j]; 

        // Starting location of buttons
        Point p1_start = btn1.getLocation(); 
        Point p2_start = btn2.getLocation(); 

        int ballOffsetX = (btn1.getWidth() - ballLabel.getWidth()) / 2; 
        int ballOffsetY = btn1.getHeight() - (ballLabel.getHeight() / 2);
        Timer timer = new Timer(10, null); 
        final int steps = duration / 10;
        final int[] step = {0};

        // Handles where the buttons should be
        double dx1 = (double) (p2_start.x - p1_start.x) / steps; 
        double dy1 = (double) (p2_start.y - p1_start.y) / steps; 
        double dx2 = (double) (p1_start.x - p2_start.x) / steps; 
        double dy2 = (double) (p1_start.y - p2_start.y) / steps;

        timer.addActionListener(e -> {
            if (step[0] < steps) {
                int currentX1 = (int) (p1_start.x + dx1 * (step[0] + 1));
                int currentY1 = (int) (p1_start.y + dy1 * (step[0] + 1));

                int currentX2 = (int) (p2_start.x + dx2 * (step[0] + 1));
                int currentY2 = (int) (p2_start.y + dy2 * (step[0] + 1));

                btn1.setLocation(currentX1, currentY1);
                btn2.setLocation(currentX2, currentY2);

                if (prevBallIndex == i) { 
                    ballLabel.setLocation(currentX1 + ballOffsetX, currentY1 + ballOffsetY);
                }
                else if (prevBallIndex == j) {
                    ballLabel.setLocation(currentX2 + ballOffsetX, currentY2 + ballOffsetY);
                }

                step[0]++; 

            } else {
                timer.stop(); 
                Point p1_end = new Point(p2_start.x, p2_start.y);
                Point p2_end = new Point(p1_start.x, p1_start.y);

                btn1.setLocation(p1_end);
                btn2.setLocation(p2_end);

                if (prevBallIndex == i) { 
                    ballLabel.setLocation(p1_end.x + ballOffsetX, p1_end.y + ballOffsetY);
                } else if (prevBallIndex == j) {
                    ballLabel.setLocation(p2_end.x + ballOffsetX, p2_end.y + ballOffsetY);
                }
               
                JButton temp = buttons[i];
                buttons[i] = buttons[j];
                buttons[j] = temp;


                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        timer.start(); 
    }
}
