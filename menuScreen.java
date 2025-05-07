import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class menuScreen extends JFrame {
    private JComboBox<String> difficultySelector;
    private JTextArea leaderboardArea;
    private JTextField usernameField;
    private Image backgroundImage;

    public menuScreen() {
        super("Ball and Cup Game Menu");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setResizable(false);
        backgroundImage = new ImageIcon("Menu1.jpg").getImage();
        

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); 
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
  
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false); 

        // Adding title label
        JLabel titleLabel = new JLabel("Ball and Cup Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Original font size
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        titleLabel.setForeground(Color.WHITE); // Original color
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Vertical space

        // Adding leaderboard label
        JLabel leaderboardLabel = new JLabel("Leaderboard:");
        leaderboardLabel.setForeground(Color.WHITE); // Original color
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        panel.add(leaderboardLabel);

        // Adding leaderboard text area inside a scroll pane
        leaderboardArea = new JTextArea(6, 25); 
        leaderboardArea.setFont(new Font("Arial", Font.PLAIN, 20));
        leaderboardArea.setEditable(false); 
        JScrollPane scrollPane = new JScrollPane(leaderboardArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(scrollPane);

        loadLeaderboard(); // Load and display scores when menu opens
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding difficulty selection panel
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        difficultyPanel.setOpaque(false); 
        JLabel difficultyLabel = new JLabel("Select Difficulty: ");
        difficultyLabel.setForeground(Color.WHITE); 
        difficultyPanel.add(difficultyLabel);
        difficultySelector = new JComboBox<>(new String[] { "Normal", "Hard" });
        difficultyPanel.add(difficultySelector);
        difficultyPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(difficultyPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding username input panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        userPanel.setOpaque(false); 
        JLabel usernameLabel = new JLabel("Enter Username: ");
        usernameLabel.setForeground(Color.WHITE);
        userPanel.add(usernameLabel);
        usernameField = new JTextField(15); 
        userPanel.add(usernameField);
        userPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(userPanel);
         panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding start button
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); 

        startButton.addActionListener(e -> {
            String username = usernameField.getText().trim(); 

            // Validate username
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username");
                return;
            }

            // Get difficulty index (0 for Normal, 1 for Hard)
            int difficulty = difficultySelector.getSelectedIndex();

            dispose();

            SwingUtilities.invokeLater(() -> {
                 App gameFrame = new App(username, difficulty);
                 gameFrame.setVisible(true); 
                 gameFrame.startGame();
            });
        });

        panel.add(startButton);
        add(panel); 
    }

    private void loadLeaderboard() {
        File file = new File("leaderboard.txt"); 
        // Check if file exists and is not a directory
        if (!file.exists() || file.isDirectory()) {
            leaderboardArea.setText("No leaderboard data"); 
            return;
        }

        // Use Main.ScoreEntry list to hold scores
        List<Main.ScoreEntry> leaderboard = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - "); 
                if (parts.length == 2) {
                    String user = parts[0].trim(); 
                    try {
                        int score = Integer.parseInt(parts[1].trim()); 
                        leaderboard.add(new Main.ScoreEntry(user, score)); 
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed score line: " + line);
                    }
                } else {
                    System.err.println("Skipping improperly formatted line: " + line);
                }
            }
        } catch (IOException e) {
            leaderboardArea.setText("Error loading leaderboard."); 
            System.err.println("Error reading leaderboard file: " + e.getMessage());
        }

        // Sort scores in descending order
        leaderboard.sort((s1, s2) -> Integer.compare(s2.score, s1.score));

        StringBuilder sb = new StringBuilder();
        int place = 1;

        for (Main.ScoreEntry entry : leaderboard) {
            sb.append("\t").append("\t").append(place).append(". ").append(entry.username).append(" - ")
                    .append(entry.score).append("\n");
            place++; 
        }

        // Set the text area content
        if (leaderboard.isEmpty()) {
            leaderboardArea.setText("No scores recorded yet.");
        } else {
            leaderboardArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            new menuScreen().setVisible(true); 
        });
    }
}