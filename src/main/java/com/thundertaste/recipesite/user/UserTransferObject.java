package com.thundertaste.recipesite.user;

public class UserTransferObject {

    private Long userID;
    private String username;
    private String email;


    public UserTransferObject(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.email = user.getEmail();

    }

    public static UserTransferObject fromUser(User user) {
        return new UserTransferObject(user);
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
