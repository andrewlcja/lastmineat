package app.lastmineat.models;

import java.util.ArrayList;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class CreateUserResponse {
    private boolean created;
    private User user;
    private ArrayList<String> errorList;

    public CreateUserResponse(boolean created, User user, ArrayList<String> errorList) {
        this.created = created;
        this.user = user;
        this.errorList = errorList;
    }

    public boolean isCreated() {
        return created;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }
}
