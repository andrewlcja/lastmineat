package app.lastmineat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import app.lastmineat.R;
import app.lastmineat.models.CreateFBUserRequest;
import app.lastmineat.models.CreateUserResponse;
import app.lastmineat.models.GenericResponse;
import app.lastmineat.models.LoginRequest;
import app.lastmineat.models.LoginResponse;
import app.lastmineat.services.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private EditText inputUsername, inputPassword;
    private LoginButton fbLoginBtn;
    private CallbackManager callbackManager;
    private MaterialDialog waitDialog;

    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);

        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_btn);
        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    final String email = object.getString("email");

                                    final Profile profile = Profile.getCurrentProfile();

                                    //show please wait dialog
                                    waitDialog = new MaterialDialog.Builder(Login.this)
                                            .content(getString(R.string.default_dialog_wait_text))
                                            .cancelable(false)
                                            .progress(true, 0)
                                            .show();

                                    Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Call<CreateUserResponse> call = userService.createFBUser(new CreateFBUserRequest(profile.getFirstName(), profile.getLastName(), email, profile.getProfilePictureUri(100, 100).toString()));
                                                    call.enqueue(new Callback<CreateUserResponse>() {
                                                        @Override
                                                        public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                                                            //dismiss please wait dialog
                                                            waitDialog.dismiss();
                                                            CreateUserResponse createUserResponse = response.body();

                                                            if (createUserResponse.isCreated()) {
                                                                //store user profile in device
                                                                Gson gson = new Gson();
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putString("user", gson.toJson(response.body().getUser()));
                                                                editor.commit();

                                                                Intent intent = new Intent(Login.this, Home.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                                                            //dismiss please wait dialog
                                                            waitDialog.dismiss();
                                                            boolean isConnectionError = t instanceof IOException;

                                                            //check if there is internet access
                                                            if (isConnectionError) {
                                                                Toast.makeText(Login.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    };
                                    thread.start();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //setup retrofit with api endpoint
        retrofit = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getString(R.string.api_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //create user service
        userService = retrofit.create(UserService.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login(View view) {
        //show please wait dialog
        waitDialog = new MaterialDialog.Builder(Login.this)
                .content(getString(R.string.default_dialog_wait_text))
                .cancelable(false)
                .progress(true, 0)
                .show();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Call<LoginResponse> call = userService.login(new LoginRequest(inputUsername.getText().toString().trim(), inputPassword.getText().toString().trim()));
                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                //dismiss please wait dialog
                                waitDialog.dismiss();
                                LoginResponse loginResponse = response.body();

                                if (loginResponse.isAuthenticated()) {
                                    //store user profile in device
                                    Gson gson = new Gson();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user", gson.toJson(response.body().getUser()));
                                    editor.commit();

                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, loginResponse.getErrorList().get(0), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                //dismiss please wait dialog
                                waitDialog.dismiss();
                                boolean isConnectionError = t instanceof IOException;

                                //check if there is internet access
                                if (isConnectionError) {
                                    Toast.makeText(Login.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }
}
