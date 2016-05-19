package com.alex.bagofwords;

/* *** This class handles setting and getting user details of all users who are registered
   *** into the application. Used for leader board activity.
*/

public class LeaderBoardUser {

    private String number;
    private String username;
    private String score;

    public LeaderBoardUser(String number, String username, String score) {
        this.setNumber(number);
        this.setUsername(username);
        this.setScore(score);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
