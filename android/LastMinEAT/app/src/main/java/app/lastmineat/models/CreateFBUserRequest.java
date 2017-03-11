package app.lastmineat.models;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class CreateFBUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;

    public CreateFBUserRequest(String firstName, String lastName, String email, String photoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
