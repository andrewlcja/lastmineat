package app.lastmineat.models;

import java.util.ArrayList;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class LoginResponse {
    private boolean authenticated;
    private User user;
    private ArrayList<String> errorList;

    public LoginResponse(boolean authenticated, User user, ArrayList<String> errorList) {
        this.authenticated = authenticated;
        this.user = user;
        this.errorList = errorList;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }
}
