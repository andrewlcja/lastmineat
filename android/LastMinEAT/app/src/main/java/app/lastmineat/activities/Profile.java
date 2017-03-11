package app.lastmineat.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import app.lastmineat.R;
import app.lastmineat.models.Redemption;
import app.lastmineat.models.User;
import app.lastmineat.services.RedemptionService;
import app.lastmineat.util.CircleTransform;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    private User user;
    private TextView profileEmail, profileRedemptions;
    private ImageView profilePic;

    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private RedemptionService redemptionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        //get shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.getFirstName() + " " + user.getLastName());

        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileRedemptions = (TextView) findViewById(R.id.profile_redemptions);
        profilePic = (ImageView) findViewById(R.id.profile_pic);

        profileEmail.setText(user.getEmail());
        Picasso.with(this).load(user.getPhotoUrl()).transform(new CircleTransform()).into(profilePic);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //setup retrofit with api endpoint
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        redemptionService = retrofit.create(RedemptionService.class);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(2);
        profileRedemptions.startAnimation(anim);

        Call<List<Redemption>> call = redemptionService.getRedemptionListByUserId(user.getId());
        call.enqueue(new Callback<List<Redemption>>() {
            @Override
            public void onResponse(Call<List<Redemption>> call, Response<List<Redemption>> response) {
                List<Redemption> redemptionList = response.body();

                if (redemptionList.size() > 0) {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, redemptionList.size());
                    valueAnimator.setDuration(1500);

                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {

                            profileRedemptions.setText(valueAnimator.getAnimatedValue().toString());

                        }
                    });
                    valueAnimator.start();
                }
            }

            @Override
            public void onFailure(Call<List<Redemption>> call, Throwable t) {
                boolean isConnectionError = t instanceof IOException;

                //check if there is internet access
                if (isConnectionError) {
                    Toast.makeText(Profile.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.action_profile);
        menuItem.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("user");
                editor.commit();

                LoginManager.getInstance().logOut();

                Intent logoutIntent = new Intent(this, Login.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
