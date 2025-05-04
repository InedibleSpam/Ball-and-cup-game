import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class App extends JFrame {
    static String user;
    static int difficulty;
    int score;
    Main cups;
    private Image cupIcon;
    private Image background;

    public App(String username, int difficulty) {
        score = 0;
        App.user = username;
        int cupCount = (difficulty==0) ? 3 : 5;
        cups = new Main(new Random(), cupCount);
        cups.shuffle();

        // Initialize frame
        background = new ImageIcon("Background.jpg").getImage();

        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize Jpanel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setOpaque(false);

        panel.setSize(700, 600);
        cupIcon = new ImageIcon("Cup.jpg").getImage();

        // Initalize score field
        JLabel scoreLabel = new JLabel();
        scoreLabel.setText(username + ": " + score);
        Font font = new Font("Arial", Font.BOLD, 30);
        scoreLabel.setFont(font);
        scoreLabel.setForeground(Color.WHITE);

        // Add buttons
        JButton button1 = new JButton() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
            }

        };
        JButton button2 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
            }
        };
        JButton button3 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
            }
        };
        JButton button4 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
            }
        };
        JButton button5 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(cupIcon, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null);
        // Conditionals for difficulty settings
        if (difficulty == 0) {
            button1.setBounds(100, 200, 150, 150);
            button2.setBounds(300, 200, 150, 150);
            button3.setBounds(500, 200, 150, 150);
        } else {
            button1.setBounds(75, 250, 100, 100);
            button2.setBounds(195, 250, 100, 100);
            button3.setBounds(315, 250, 100, 100);
            button4.setBounds(435, 250, 100, 100);
            button5.setBounds(555, 250, 100, 100);
        }

        scoreLabel.setBounds(310, 100, 500, 50);
        panel.add(scoreLabel);

        if (difficulty == 0) {
            panel.add(button3);
            panel.add(button1);
            panel.add(button2);
        } else {
            panel.add(button3);
            panel.add(button1);
            panel.add(button2);
            panel.add(button4);
            panel.add(button5);
        }

        add(panel);

        

        // Button listeners
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cups.getCup(1).checkForBall() == true) {
                    score += 1;
                    cups.shuffle();
                    scoreLabel.setText(username + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "YOU FAILED YOU LOSER");
                    cups.saveScore(user, score);
                    dispose();
                    System.exit(0);
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cups.getCup(2).checkForBall() == true) {
                    score += 1;
                    cups.shuffle();
                    scoreLabel.setText(username + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "YOU FAILED YOU LOSER");
                    cups.saveScore(username, score);
                    dispose();
                    System.exit(0);
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cups.getCup(3).checkForBall() == true) {
                    score += 1;
                    cups.shuffle();
                    scoreLabel.setText(username + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "YOU FAILED YOU LOSER");
                    cups.saveScore(username, score);
                    dispose();
                    System.exit(0);
                }
            }
        });
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cups.getCup(4).checkForBall() == true) {
                    score += 1;
                    cups.shuffle();
                    scoreLabel.setText(username + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "YOU FAILED YOU LOSER");
                    cups.saveScore(username, score);
                    dispose();
                    System.exit(0);
                }
            }
        });
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cups.getCup(5).checkForBall() == true) {
                    score += 1;
                    cups.shuffle();
                    scoreLabel.setText(username + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "YOU FAILED YOU LOSER");
                    cups.saveScore(username, score);
                    dispose();
                    System.exit(0);
                }
            }
        });

    }

    // Main class for exceptions and running gui
    public static void main(String Username, int difficulty) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BasicGameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BasicGameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BasicGameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BasicGameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        new App(user, difficulty).setVisible(true);
    }
}
