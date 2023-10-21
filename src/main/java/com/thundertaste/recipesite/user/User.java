package com.thundertaste.recipesite.user;




import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String profileImage;

    @Temporal(TemporalType.DATE)
    private Date joinedDate;

    private String bio;

    // Omitted favorites for now, as it relates to a many-to-many relationship with Recipe

    public User() {
        // Default constructor
    }

    public User(String username, String email, String passwordHash, String profileImage, Date joinedDate, String bio) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profileImage = profileImage;
        this.joinedDate = joinedDate;
        this.bio = bio;
    }

    // Getters and setters
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", joinedDate=" + joinedDate +
                ", bio='" + bio + '\'' +
                '}';
    }
}
