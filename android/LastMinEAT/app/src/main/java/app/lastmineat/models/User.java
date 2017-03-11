package app.lastmineat.models;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class User {
    private int id;
    private String username;
    private String hashedPassword;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;

    public User(int id, String username, String hashedPassword, String salt, String firstName, String lastName, String email, String role, String photoUrl) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}