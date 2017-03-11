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
