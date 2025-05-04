
import java.io.*;
import java.util.*;

public class Main {
    Random ran;
    ArrayList<Cup> clist;
    static String user;
    int score;

    public Main(Random random, int num) {
        clist = new ArrayList<>();
        this.ran = random;
        for (int i = 1; i <= num; i++) {
            Cup cup = new Cup(i);
            clist.add(cup);
        }
    }

    public Cup getCup(int id) {
        for (Cup cup : clist) {
            if (cup.getId() == id) {
                return cup;
            }

        }
        return null;
    }

    public void shuffle() {
        int x = ran.nextInt(1, clist.size() + 1);
        for (Cup cup : clist) {
            if (cup.getId() == x) {
                cup.giveBall();
                System.out.println(cup.getId());
            } else {
                cup.removeBall();
            }
        }

    }

    // public void savescore(String username, int score) {
    // try (PrintWriter out = new PrintWriter(new FileWriter("leaderboard.txt",
    // true))) {
    // out.println(username + " : " + score);

    // } catch (Exception e) {
    // System.out.println("Oh no! We encountered an exception in the code!");
    // System.out.println("X X");
    // System.out.println(" O ");
    // }
    // }

    public void saveScore(String username, int Score) {
        List<Score> leaderboard = new ArrayList<>();

        File file = new File("leaderboard.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" : ");
                    if (parts.length == 2) {
                        String user = parts[0];
                        int userScore = Integer.parseInt(parts[1]);
                        leaderboard.add(new Score(user, userScore));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        leaderboard.add(new Score(username, Score));

        leaderboard.sort((s1, s2) -> Integer.compare(s2.score, s1.score));

        try (PrintWriter out = new PrintWriter(new FileWriter("leaderboard.txt"))) {
            for (Score score : leaderboard) {
                out.println(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}