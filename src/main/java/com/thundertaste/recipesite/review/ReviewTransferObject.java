package com.thundertaste.recipesite.review;

import java.util.Date;

public class ReviewTransferObject {
    private String text;
    private Date datePosted;
    private String username; // username of the author



    private int score; // Score of the review
    // ... other properties like rating

    public ReviewTransferObject(String text, Date datePosted, String username,int score) {
        this.text = text;
        this.datePosted = datePosted;
        this.username = username;
        this.score=score;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
