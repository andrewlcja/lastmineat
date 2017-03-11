package app.lastmineat.models;

import java.util.ArrayList;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
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
