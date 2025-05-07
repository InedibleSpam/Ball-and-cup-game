
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

    private static final int MIN_ANIM_DURATION = 100;
    private static final int MIN_PAUSE = 10;
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

    private int shuffleStepsCompleted;
    private int totalShuffleSteps;

    // App constructor initializes the game with a username and difficulty level.
    public App(String username, int difficulty) {
        score = 0;
        App.user = username;
        App.difficulty = difficulty;

        // Determine the number of cups based on the selected difficulty
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

        // Create a JPanel to hold game components and draw the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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

        // Create and configure the label to display the player's name and score
        scoreLabel = new JLabel(username + " - " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(310, 100, 500, 50);
        panel.add(scoreLabel);

        buttons = new JButton[cupCount];

        int y = 250;
        int spacing;
        int startX;

        // Adjust spacing based on difficulty (0 = Normal, 1 = Hard)
        if (difficulty == 0) {
            spacing = 150;
        } else {
            spacing = 110;
        }
        // Calculate the starting X coordinate
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

            // Add an ActionListener to handle clicks on this specific cup button
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

    public void startGame() {
        startRound();
    }

    private void startRound() {
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
                startShufflingAnimation();
            });
            showBallTimer.setRepeats(false);
            showBallTimer.start();
        } else {
            System.err
                    .println("Error: Could not find the initial visual index of the cup with the ball after shuffle.");
            startShufflingAnimation();
        }
    }

    private void handleGuess(JButton clickedButton) {
        if (!canGuess) 
        return; // Ensure guessing is currently allowed
   
        // Find the current index of the clicked button within the 'buttons' array.
        // This array's order is synchronized with the logical cup list (Main.getCupList())
        // after the shuffling animation is complete.
        int guessedIndex = -1; // Variable to store the index of the clicked button
   
        for (int i = 0; i < buttons.length; i++) {
            // Find the index where the clickedButton is currently located in the buttons array
            if (buttons[i] == clickedButton) {
                guessedIndex = i; // Found the index of the clicked button
                break; // Exit the loop
            }
        }
   
        // If the clicked button was successfully found in the array
        if (guessedIndex != -1) {
            // Get the current logical list of cups from the Main object.
            ArrayList<Cup> currentCupList = cups.getCupList();
   
            // Check if the Cup object at the guessed index in the logical list currently has the ball.
            // The index in the 'buttons' array (guessedIndex) corresponds directly
            // to the index in the logical list (currentCupList) because they are kept in sync.
            if (currentCupList.get(guessedIndex).hasBall()) {
                // Player guessed correctly!
                score++; // Increment the score
                scoreLabel.setText(user + " - " + score); // Update the score display label
   
                // Show the ball under the guessed cup to visually confirm the correct guess
                showBallUnderCup(guessedIndex);
                ballLabel.setVisible(true); // Make sure the ball is visible
   
                // --- Speed Up Animation ---
                // Decrease duration and pause slightly, ensuring they don't go below minimums
                currentAnimationDuration = Math.max(MIN_ANIM_DURATION, currentAnimationDuration - 25); // Decrease duration by 25ms (adjust '25' to change speed increment)
                currentPauseAfterSwap = Math.max(MIN_PAUSE, currentPauseAfterSwap - 5);         // Decrease pause by 5ms (adjust '5' to change speed increment)
                System.out.println("Correct! Speed increased. Next shuffle duration: " + currentAnimationDuration + "ms, pause: " + currentPauseAfterSwap + "ms");
                // --- End Speed Up ---
   
   
                // Start a timer to begin the next round after a short delay
                Timer newRoundTimer = new Timer(2000, e -> startRound()); // 2000 ms = 2 seconds delay
                newRoundTimer.setRepeats(false); // Ensure the timer only fires once
                newRoundTimer.start(); // Start the timer
   
            } else {
                // Player guessed incorrectly - Game over
                // First, show the ball under the cup where it actually was located
                // Check if the ball's actual visual index is valid before displaying
                if (ballCupIndexVisual >= 0 && ballCupIndexVisual < buttons.length) {
                   showBallUnderCup(ballCupIndexVisual); // Use the last known visual index of the ball
                   ballLabel.setVisible(true); // Make sure the ball is visible
                } else {
                    System.err.println("Error: Ball's final visual index (" + ballCupIndexVisual + ") is invalid on game over.");
                    // Ball might not be shown correctly, but proceed to game over
                }
   
   
                // Display a "Game Over" message dialog showing the final score
                JOptionPane.showMessageDialog(this, "Game over. Your final score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
   
                // Save the player's final score to the leaderboard file
                // Check if cups object is valid before saving score
                if (cups != null) {
                   cups.saveScore(user, score); // Call the saveScore method in the Main class
                } else {
                    System.err.println("Error: Main logic object is null. Cannot save score.");
                }
   
   
                // Terminate the application after the player acknowledges the game over message.
                // Use SwingUtilities.invokeLater to ensure System.exit runs safely on the EDT.
                SwingUtilities.invokeLater(() -> {
                    System.exit(0);
                });
            }
        } else {
            // This case indicates an internal error where the clicked button wasn't found in the array.
            System.err.println("Error: Clicked button not found in buttons array. This should not happen with proper event handling.");
        }
    }

    private void showBallUnderCup(int cupIndex) {
        // Ensure the provided index is valid within the buttons array bounds
        if (cupIndex >= 0 && cupIndex < buttons.length) {
            JButton cupButton = buttons[cupIndex]; // Get the button at the specified index
            Point cupLocation = cupButton.getLocation(); // Get the current top-left location of the button

            // Calculate the horizontal position for the ball to be centered under the cup
            int ballX = cupLocation.x + (cupButton.getWidth() - ballLabel.getWidth()) / 2;
            // Calculate the vertical position for the ball to be just below the cup's
            // bottom edge
            int ballY = cupLocation.y + cupButton.getHeight() - (ballLabel.getHeight() / 2); // Adjust Y as needed

            ballLabel.setLocation(ballX, ballY); // Set the ball label's visual position
            // Visibility (true/false) needs to be set separately where showBallUnderCup is
            // called.
        } else {
            System.err.println("Error: Attempted to position ball under invalid cup index: " + cupIndex);
        }
    }

    /**
     * Initializes the counters for the shuffle animation and starts the first step.
     * Called after the initial ball display period ends.
     */
    private void startShufflingAnimation() {
        shuffleStepsCompleted = 0; // Reset the counter for completed shuffle steps
        
        if(difficulty == 0){
            totalShuffleSteps =10;
        }
        else {
            totalShuffleSteps = 20;
        }
        // Begin the first step in the sequence of shuffle animations.
        performNextShuffleStep();
    }

    /**
     * Performs a single logical swap in the Main class and initiates the
     * corresponding
     * visual swap animation for the cups in the App class. This method is called
     * repeatedly (sequentially) until all animation steps are completed.
     */
    private void performNextShuffleStep() {
        // Check if the current step is less than the total steps required for the shuffle
        if (shuffleStepsCompleted < totalShuffleSteps) {
            // --- Select Indices for this Swap ---
            Random random = new Random(); // Use a local Random instance for choosing swap indices
            int i = random.nextInt(cupCount); // Get a random index for the first cup
            int j = random.nextInt(cupCount); // Get a random index for the second cup
            // Ensure the two indices selected are different
            while (j == i) {
                j = random.nextInt(cupCount);
            }
   
            // --- Perform Logical Swap ---
            // Store the ball's visual index *BEFORE* this specific swap step occurs.
            // This is needed by the Swap method to animate the ball correctly with the cup it's currently under.
            int prevBallIndex = ballCupIndexVisual;
   
            cups.swapCupsByIndex(i, j); // This updates the underlying game state immediately
   
            // --- Update Visual Ball Index ---
            // Update the visual ball index (index in the 'buttons' array) based on the logical swap that just happened.
            // If the ball was visually at index 'i' before the swap (meaning logically the ball was in the cup currently at index i),
            // it's now logically at index 'j' (the cup originally at j is now at i, and the cup originally at i is now at j),
            // so its new visual index is 'j'.
            if (ballCupIndexVisual == i) {
                ballCupIndexVisual = j;
            }
            // If the ball was visually at index 'j' before the swap, it's now logically at index 'i',
            // so its new visual index is 'i'.
            else if (ballCupIndexVisual == j) {
                ballCupIndexVisual = i;
            }
            // If the ball's original visual index (prevBallIndex) was neither 'i' nor 'j',
            // it means the ball was not involved in this particular swap of indices,
            // and ballCupIndexVisual remains unchanged.
   
            // --- Initiate Visual Swap Animation ---
            // Use the current speed variables for the animation duration and pause
            int animationDuration = currentAnimationDuration; // Use the current speed variable
            int pauseAfterSwap = currentPauseAfterSwap;   // Use the current speed variable
   
   
            // Start the visual swap animation for the buttons currently located at indices i and j in the 'buttons' array.
            // Pass a lambda function as the 'onComplete' callback that will run AFTER this Swap animation finishes.
            // Pass 'prevBallIndex' so the Swap method knows which cup to move the ball with.
            Swap(i, j, animationDuration, () -> {
                 // This code block executes AFTER the Swap animation for the current step is fully complete,
                 // including updating the 'buttons' array.
   
                 // Add a small pause before initiating the next shuffle step.
                 Timer pauseTimer = new Timer(pauseAfterSwap, e -> {
                     shuffleStepsCompleted++; // Increment the counter for completed steps
                     performNextShuffleStep(); // Recursively call performNextShuffleStep to start the next step
                 });
                 pauseTimer.setRepeats(false); // The pause timer should only fire once
                 pauseTimer.start(); // Start the pause timer
            }, prevBallIndex); // Pass the ball's visual index *before* this swap occurred
   
   
            // The ball's visual movement is handled *inside* the Swap method as the cups animate.
            // No separate call to moveBallWithCups is needed here.
   
        } else {
            // All sequential shuffle steps (totalShuffleSteps) have been completed for this round's animation.
            canGuess = true; // Enable the player to click on cups to make a guess.
            System.out.println("Shuffling complete. Make your guess!");
        }
    }

    /**
     * Animates the visual swap of two JButton components representing cups.
     * The buttons are located at the given indices (i and j) in the 'buttons' array
     * *at the start* of this animation.
     * The animation moves btn1 towards the starting location of btn2, and btn2
     * towards the starting location of btn1.
     * The ball label is moved along with the cup it was visually under (based on
     * prevBallIndex).
     * After the animation finishes, the references in the 'buttons' array at
     * indices i and j are swapped
     * to reflect their new visual positions, and the onComplete callback is
     * executed.
     *
     * @param i             The index in the 'buttons' array of the first button
     *                      involved in this swap.
     * @param j             The index in the 'buttons' array of the second button
     *                      involved in this swap.
     * @param duration      The total duration in milliseconds for the animation of
     *                      this single swap.
     * @param onComplete    A Runnable to execute after the animation and the update
     *                      to the 'buttons' array are complete.
     * @param prevBallIndex The visual index of the ball *before* this specific swap
     *                      step began.
     */
    private void Swap(int i, int j, int duration, Runnable onComplete, int prevBallIndex) {
        // Get the JButton references currently stored at indices i and j in the buttons
        // array.
        JButton btn1 = buttons[i]; // The button that is visually at index i currently
        JButton btn2 = buttons[j]; // The button that is visually at index j currently

        // Store the starting pixel locations of these two buttons before animation
        // begins.
        Point p1_start = btn1.getLocation(); // Starting top-left corner for btn1
        Point p2_start = btn2.getLocation(); // Starting top-left corner for btn2

        // Calculate the pixel offsets needed to position the ball label centered
        // horizontally
        // and correctly vertically relative to the top-left corner of a cup button.
        // These offsets are constant for the ball relative to its cup.
        int ballOffsetX = (btn1.getWidth() - ballLabel.getWidth()) / 2; // Center horizontally
        // Vertical offset: cup height - half ball height (adjust as needed for visual
        // appearance)
        int ballOffsetY = btn1.getHeight() - (ballLabel.getHeight() / 2);

        // Create a javax.swing.Timer to drive the animation frame by frame.
        Timer timer = new Timer(10, null); // Fires an action event every 10 milliseconds
        // Calculate the total number of animation steps based on the total duration and
        // the timer delay.
        // Math.max(1, ...) ensures at least one step even for durations less than the
        // timer delay.
        final int steps = Math.max(1, duration / 10);
        // Use a one-element array to allow the step counter to be modified from within
        // the lambda expression.
        final int[] step = {0};

        // Calculate the horizontal and vertical pixel displacement that should occur
        // for
        // each button during a single animation step. Use double for floating-point
        // precision.
        double dx1 = (double) (p2_start.x - p1_start.x) / steps; // Horizontal displacement per step for btn1 (moves
                                                                 // towards p2)
        double dy1 = (double) (p2_start.y - p1_start.y) / steps; // Vertical displacement per step for btn1

        double dx2 = (double) (p1_start.x - p2_start.x) / steps; // Horizontal displacement per step for btn2 (moves
                                                                 // towards p1)
        double dy2 = (double) (p1_start.y - p2_start.y) / steps; // Vertical displacement per step for btn2

        // Add the action listener that will be executed every time the timer fires.
        timer.addActionListener(e -> {
            // Check if there are more animation steps remaining
            if (step[0] < steps) {
                // Calculate the current visual location for btn1 at the current step.
                // This is the starting location plus the displacement per step multiplied by
                // the number of steps completed so far (+1 for the next step).
                int currentX1 = (int) (p1_start.x + dx1 * (step[0] + 1));
                int currentY1 = (int) (p1_start.y + dy1 * (step[0] + 1));

                // Calculate the current visual location for btn2 at the current step.
                int currentX2 = (int) (p2_start.x + dx2 * (step[0] + 1));
                int currentY2 = (int) (p2_start.y + dy2 * (step[0] + 1));

                // Update the actual visual location of the buttons on the panel.
                btn1.setLocation(currentX1, currentY1);
                btn2.setLocation(currentX2, currentY2);

                // Move the ball label along with the cup button it was visually under before
                // this swap started.
                // Use the 'prevBallIndex' passed into this method to determine which cup the
                // ball follows.
                if (prevBallIndex == i) { // If the ball started under the button that was at index 'i'
                    // Move the ball label to stay with btn1, applying the calculated offset.
                    ballLabel.setLocation(currentX1 + ballOffsetX, currentY1 + ballOffsetY);
                }
                // If the ball started under the button that was at index 'j'
                else if (prevBallIndex == j) {
                    // Move the ball label to stay with btn2, applying the calculated offset.
                    ballLabel.setLocation(currentX2 + ballOffsetX, currentY2 + ballOffsetY);
                }
                // If prevBallIndex is neither i nor j, the ball is not involved in this
                // specific visual swap animation
                // and its location is not updated by this timer (it stays with its own cup
                // which isn't moving in this swap).

                step[0]++; // Increment the step counter for the next timer fire

            } else {
                // The animation is complete (all steps have been performed)
                timer.stop(); // Stop the animation timer

                // Ensure both buttons end up in their precise final target locations to avoid
                // any minor visual
                // discrepancies caused by integer casting during the step-by-step movement.
                // Button 1 should end up exactly where button 2 started.
                Point p1_end = new Point(p2_start.x, p2_start.y);
                // Button 2 should end up exactly where button 1 started.
                Point p2_end = new Point(p1_start.x, p1_start.y);

                btn1.setLocation(p1_end);
                btn2.setLocation(p2_end);

                // Ensure the ball label is also precisely positioned under the correct cup's
                // final location
                // after the animation finishes.
                if (prevBallIndex == i) { // Ball started under button at 'i', moved with it to 'p1_end'
                    ballLabel.setLocation(p1_end.x + ballOffsetX, p1_end.y + ballOffsetY);
                } else if (prevBallIndex == j) { // Ball started under button at 'j', moved with it to 'p2_end'
                    ballLabel.setLocation(p2_end.x + ballOffsetX, p2_end.y + ballOffsetY);
                }
                // If the ball wasn't involved, its final location is not set by this swap.

                // *** CRITICAL SYNCHRONIZATION STEP ***
                // Swap the references to the JButton objects within the 'buttons' array *AFTER*
                // the visual animation
                // is fully complete. This updates the 'buttons' array to reflect the new visual
                // positions:
                // the button that finished at index 'i' is now stored at buttons[i],
                // and the button that finished at index 'j' is now stored at buttons[j].
                // This is essential to keep the 'buttons' array's order synchronized with the
                // logical order
                // of the cups in Main.cups.getCupList().
                JButton temp = buttons[i];
                buttons[i] = buttons[j];
                buttons[j] = temp;

                // Execute the completion callback (the Runnable that was passed when Swap was
                // called).
                // This callback is responsible for starting the next shuffle step or signaling
                // that
                // the entire shuffle animation sequence is finished and guessing can begin.
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        timer.start(); // Start the animation timer for this single swap
    }
}