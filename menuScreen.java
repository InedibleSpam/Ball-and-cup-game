import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;;

public class menuScreen extends JFrame {
    private JComboBox<String> difficultySelector;
    private JTextArea leaderboardArea;
    private JTextField usernameField;
    private Image backgroundImage;

    // Making start menu
    public menuScreen() {
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

        // Adding title
        JLabel titleLabel = new JLabel("Ball and Cup Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding leaderboard
        JLabel leaderboardLabel = new JLabel("Leaderboard:");
        leaderboardLabel.setForeground(Color.WHITE);
        leaderboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(leaderboardLabel);
        leaderboardArea = new JTextArea(6, 25);
        leaderboardArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(leaderboardArea);
        panel.add(scrollPane);
        loadLeaderboard();
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding difficulty selector
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        difficultyPanel.setOpaque(false);
        JLabel difficultyLabel = new JLabel("Select Difficulty: ");
        difficultyLabel.setForeground(Color.WHITE);
        difficultyPanel.add(difficultyLabel);
        difficultySelector = new JComboBox<>(new String[] { "Normal", "Hard" });
        difficultyPanel.add(difficultySelector);
        panel.add(difficultyPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Adding username setter
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userPanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Enter Username: ");
        usernameLabel.setForeground(Color.WHITE);
        userPanel.add(usernameLabel);
        usernameField = new JTextField(15);
        userPanel.add(usernameField);
        panel.add(userPanel);

        // Adding start button
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            String username = usernameField.getText();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username");
                return;
            }
            //Stores difficulty as 0 or 1
            int difficulty = difficultySelector.getSelectedIndex(); 

            // Start game
            dispose();

            App.main(username, difficulty);
        });

        panel.add(startButton);
        add(panel);

    }

    // Handles leaderboard
    private void loadLeaderboard() {
        File file = new File("leaderboard.txt");
        if (!file.exists()) {
            leaderboardArea.setText("No leaderboard data");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            leaderboardArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboardArea.append(line + "\n");
            }
        } catch (IOException e) {
            leaderboardArea.setText("Error loading leaderboard.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new menuScreen().setVisible(true);
        });
    }
}