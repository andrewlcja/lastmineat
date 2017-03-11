package app.lastmineat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;

import java.io.IOException;

import app.lastmineat.R;
import app.lastmineat.models.CreateRedemptionRequest;
import app.lastmineat.models.Deal;
import app.lastmineat.models.GenericResponse;
import app.lastmineat.models.User;
import app.lastmineat.services.RedemptionService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Decoder extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView mydecoderview;
    private boolean read;
    private Deal deal;
    private User user;
    private MaterialDialog waitDialog;

    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private RedemptionService redemptionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        read = false;

        //get shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);


        //get deal
        Intent intent = getIntent();
        deal = (Deal) intent.getSerializableExtra("deal");

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        mydecoderview.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        mydecoderview.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        mydecoderview.setTorchEnabled(true);

        // Use this function to set front camera preview
        mydecoderview.setFrontCamera();

        // Use this function to set back camera preview
        mydecoderview.setBackCamera();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //setup retrofit with api endpoint
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //create service
        redemptionService = retrofit.create(RedemptionService.class);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (!read) {
            read = true;
            final String finalText = text;

            //show please wait dialog
            waitDialog = new MaterialDialog.Builder(Decoder.this)
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
                            if (finalText.equals(deal.getVendor().getId() + deal.getVendor().getName())) {
                                Call<GenericResponse> call = redemptionService.createRedemption(new CreateRedemptionRequest(deal.getId(), user.getId()));
                                call.enqueue(new Callback<GenericResponse>() {
                                    @Override
                                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                                        //dismiss please wait dialog
                                        waitDialog.dismiss();

                                        if (response.body().isSuccess()) {
                                            new MaterialDialog.Builder(Decoder.this)
                                                    .title("Successfully Redeemed")
                                                    .content("Claim your " + deal.getDescription() + "! Thank you!")
                                                    .positiveText(getString(R.string.default_dialog_neutral_text))
                                                    .cancelable(false)
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            Intent intent = new Intent(Decoder.this, Home.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                                        //dismiss please wait dialog
                                        waitDialog.dismiss();
                                        boolean isConnectionError = t instanceof IOException;

                                        //check if there is internet access
                                        if (isConnectionError) {
                                            Toast.makeText(Decoder.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                //dismiss please wait dialog
                                waitDialog.dismiss();

                                new MaterialDialog.Builder(Decoder.this)
                                        .title("Incorrect QR Code")
                                        .content("Sorry! Please try again!")
                                        .positiveText(getString(R.string.default_dialog_neutral_text))
                                        .cancelable(false)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Intent intent = new Intent(Decoder.this, Home.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }
            };
            thread.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.stopCamera();
    }
}
