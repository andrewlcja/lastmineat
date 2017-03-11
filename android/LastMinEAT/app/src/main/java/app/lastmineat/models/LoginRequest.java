package app.lastmineat.models;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
