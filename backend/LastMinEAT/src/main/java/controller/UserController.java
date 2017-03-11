/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.CreateFBUserRequest;
import model.CreateUserRequest;
import model.CreateUserResponse;
import model.GenericResponse;
import model.LoginRequest;
import model.LoginResponse;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

/**
 *
 * @author andrew.lim.2013
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/GetUserListRequest", method = RequestMethod.GET)
    public @ResponseBody List<User> getUserListRequest() {
        List<User> userList = null;
        try {
            userList = userService.getUserListRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }
    
    @RequestMapping(value = "/CreateUserRequest", method = RequestMethod.POST)
    public @ResponseBody CreateUserResponse createUserRequest(@RequestBody CreateUserRequest request) {
        CreateUserResponse response = null;
        try {
            response = userService.createUserRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    
    @RequestMapping(value = "/CreateFBUserRequest", method = RequestMethod.POST)
    public @ResponseBody CreateUserResponse createUserRequest(@RequestBody CreateFBUserRequest request) {
        CreateUserResponse response = null;
        try {
            response = userService.createFBUserRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    
    @RequestMapping(value = "/LoginRequest", method = RequestMethod.POST)
    public @ResponseBody LoginResponse loginRequest(@RequestBody LoginRequest request) {
        LoginResponse response = null;
        try {
            response = userService.loginRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
