// Main.java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    private Random ran; 
    private ArrayList<Cup> clist; 
    public Main(Random random, int numCups) {
        this.clist = new ArrayList<>();
        this.ran = random;

        if (numCups <= 0) {
            throw new IllegalArgumentException("Number of cups must be positive.");
        }

        // Create the specified number of cups with IDs
        for (int i = 1; i <= numCups; i++) {
            Cup cup = new Cup(i);
            clist.add(cup);
        }

        // Randomly place the ball in one cup initially
        if (!clist.isEmpty()) {
            int ballHolderIndex = ran.nextInt(clist.size());
            clist.get(ballHolderIndex).giveBall(); 
        }
    }

   //Retrieve Cup object by ID
    public Cup getCup(int id) {
        for (Cup cup : clist) {
            if (cup.getId() == id) {
                return cup;
            }
        }
        return null;
    }

    //Shuffles cups and places the ball in new random cup
    public void shuffle() {
        if (clist != null) {
            removeBallFromAll(); 

            // Place the ball in a new random cup 
            if (!clist.isEmpty()) {
                int newBallIndex = ran.nextInt(clist.size()); 
                clist.get(newBallIndex).giveBall(); 
            }
        }
    }

    public void swapCupsByIndex(int index1, int index2) {
        if (clist != null && index1 >= 0 && index1 < clist.size() && index2 >= 0 && index2 < clist.size()) {
            Collections.swap(clist, index1, index2); // Perform the swap in the ArrayList
        } else {
            System.err.println("Invalid indices for swapping cups: " + index1 + ", " + index2 + " (List size: " + (clist == null ? "null" : clist.size()) + ")");
        }
    }

    // Call the removeBall method on each Cup object
    private void removeBallFromAll() {
        if (clist != null) {
            for (Cup cup : clist) {
                cup.removeBall(); 
            }
        }
    }


    public ArrayList<Cup> getCupList() {
        return clist; 
    }

    public void saveScore(String username, int currentScore) {
        List<ScoreEntry> leaderboard = new ArrayList<>(); 
        File file = new File("leaderboard.txt"); 

        // Read existing scores from the file
        if (file.exists() && !file.isDirectory()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - "); 
                    if (parts.length == 2) {
                        String userFromFile = parts[0].trim(); 
                        try {
                            int userScoreFromFile = Integer.parseInt(parts[1].trim()); 
                            leaderboard.add(new ScoreEntry(userFromFile, userScoreFromFile)); // Add to leaderboard list
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed score line in leaderboard.txt: " + line);
                        }
                    } else {
                         System.err.println("Skipping improperly formatted line in leaderboard.txt: " + line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading leaderboard.txt: " + e.getMessage());
            }
        }

        // Add player's score to leaderboard 
        leaderboard.add(new ScoreEntry(username, currentScore));

        // Sorts leaderboard based on the score
        leaderboard.sort((s1, s2) -> Integer.compare(s2.score, s1.score)); 

        // Write the sorted leaderboard back to file
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (ScoreEntry entry : leaderboard) {
                out.println(entry); 
            }
        } catch (IOException e) {
            System.err.println("Error writing to leaderboard.txt: " + e.getMessage());
        }
    }

    public static class ScoreEntry { 
        String username; 
        int score;       

        public ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }

        @Override
        public String toString() {
            return username + " - " + score;
        }
    }
}