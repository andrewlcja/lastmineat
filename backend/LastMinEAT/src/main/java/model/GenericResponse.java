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
public class GenericResponse {
    private boolean success;
    private ArrayList<String> errorList;

    public GenericResponse(boolean success, ArrayList<String> errorList) {
        this.success = success;
        this.errorList = errorList;
    }

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }
}
