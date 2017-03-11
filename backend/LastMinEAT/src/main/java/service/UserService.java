/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.UserDAO;
import java.util.ArrayList;
import java.util.List;
import model.CreateFBUserRequest;
import model.CreateUserRequest;
import model.CreateUserResponse;
import model.GenericResponse;
import model.LoginRequest;
import model.LoginResponse;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import utility.HashUtility;

/**
 *
 * @author andrew.lim.2013
 */
public class UserService {

    @Autowired
    UserDAO userDAO;

    public List<User> getUserListRequest() throws Exception {
        return userDAO.retrieveAll();
    }

    public CreateUserResponse createUserRequest(CreateUserRequest request) throws Exception {
        String username = request.getUsername().trim();
        String password = request.getPassword().trim();
        String confirmPassword = request.getConfirmPassword().trim();
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();
        String email = request.getEmail().trim();
        String photoUrl = request.getPhotoUrl().trim();

        ArrayList<String> errorList = new ArrayList<String>();

        //validations
        if (username.isEmpty()) {
            errorList.add("Username cannot be blank");
        }
        
        if (password.isEmpty()) {
            errorList.add("Password cannot be blank");
        }
        
        if (confirmPassword.isEmpty()) {
            errorList.add("Confirmation Password cannot be blank");
        }
        
        if (firstName.isEmpty()) {
            errorList.add("First Name cannot be blank");
        }
        
        if (lastName.isEmpty()) {
            errorList.add("Last Name cannot be blank");
        }        
        
        if (email.isEmpty()) {
            errorList.add("Email cannot be blank");
        }
        
        if (photoUrl.isEmpty()) {
            photoUrl = "NA";
        }

        if (!errorList.isEmpty()) {
            return new CreateUserResponse(false, null, errorList);
        }

        if (userDAO.getUserByUsername(username) != null) {
            errorList.add("Username already exists");
        }

        if (!email.contains("@") || email.length() == 1) {
            errorList.add("Invalid Email");
        } else if (userDAO.getUserByEmail(email) != null) {
            errorList.add("Email already exists");
        }

        if (!password.equals(confirmPassword)) {
            errorList.add("Passwords do not match");
        }

        if (!errorList.isEmpty()) {
            return new CreateUserResponse(false, null, errorList);
        }

        //generate hash password and salt
        String[] credentials = HashUtility.getHashAndSalt(password);
        String hashedPassword = credentials[0];
        String salt = credentials[1];

        userDAO.createUser(new User(username, hashedPassword, salt, firstName, lastName, email, photoUrl));
        User user = userDAO.getUserByEmail(email);
        return new CreateUserResponse(true, user, errorList);
    }

    public CreateUserResponse createFBUserRequest(CreateFBUserRequest request) throws Exception {
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();
        String email = request.getEmail().trim();
        String photoUrl = request.getPhotoUrl().trim();

        ArrayList<String> errorList = new ArrayList<String>();

        //validations
        if (firstName.isEmpty()) {
            errorList.add("First Name cannot be blank");
        }
        
        if (lastName.isEmpty()) {
            errorList.add("Last Name cannot be blank");
        }
        
        if (email.isEmpty()) {
            errorList.add("Email cannot be blank");
        }
        
        if (photoUrl.isEmpty()) {
            photoUrl = "NA";
        }

        if (!errorList.isEmpty()) {
            return new CreateUserResponse(false, null, errorList);
        }
        
        if (!email.contains("@") || email.length() == 1) {
            errorList.add("Invalid Email");
        }
        
        if (!errorList.isEmpty()) {
            return new CreateUserResponse(false, null, errorList);
        }
        
        User user = userDAO.getUserByEmail(email);
        
        if (user != null) {
            return new CreateUserResponse(true, user, errorList);
        } else {
            userDAO.createUser(new User("NA", "NA", "NA", firstName, lastName, email, photoUrl));
            user = userDAO.getUserByEmail(email);
            return new CreateUserResponse(true, user, errorList);
        }
    }

    public LoginResponse loginRequest(LoginRequest request) throws Exception {
        String username = request.getUsername().trim();
        String password = request.getPassword().trim();

        ArrayList<String> errorList = new ArrayList<String>();

        //validations
        if (username.isEmpty()) {
            errorList.add("Username cannot be blank");
        }
        if (password.isEmpty()) {
            errorList.add("Password cannot be blank");
        }

        User user = userDAO.getUserByUsername(request.getUsername());

        if (user == null) {
            errorList.add("Invalid username/password");
            return new LoginResponse(false, null, errorList);
        }

        if (!HashUtility.verify(password, user.getHashedPassword(), user.getSalt())) {
            errorList.add("Invalid username/password");
            return new LoginResponse(false, null, errorList);
        }

        return new LoginResponse(true, user, errorList);
    }
}
