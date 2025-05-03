import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    public static void main(String username, int difficulty) {
        JFrame frame = new JFrame("Ball and Cup");
        BallAndCupGame game = new BallAndCupGame(username, difficulty);
        frame.setSize(700, 600);
        frame.setResizable(false);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center the window
        frame.setVisible(true);
    }
}

