package app.lastmineat.services;

import app.lastmineat.models.CreateFBUserRequest;
import app.lastmineat.models.CreateUserResponse;
import app.lastmineat.models.GenericResponse;
import app.lastmineat.models.LoginRequest;
import app.lastmineat.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public interface UserService {
    @POST("LoginRequest")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("CreateFBUserRequest")
    Call<CreateUserResponse> createFBUser(@Body CreateFBUserRequest body);
}
