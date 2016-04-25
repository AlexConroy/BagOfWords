package com.alex.bagofwords;

/**
 * Created by Alex on 25/04/2016.
 */
public class Contacts {


    public Contacts(String username, String score) {
        this.setUsername(username);
        this.setScore(score);
    }


    private String username, score;

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
