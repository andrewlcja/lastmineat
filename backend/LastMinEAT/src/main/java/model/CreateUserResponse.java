/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author andrew.lim.2013
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
