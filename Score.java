public class Score {
    String username;
    int score;

    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String toString() {
        return username + " - " + score;
    }
}