package app.lastmineat.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.lastmineat.R;
import app.lastmineat.adapters.ViewDealsAdapter;
import app.lastmineat.adapters.ViewVendorAdapter;
import app.lastmineat.fragments.ViewDeals;
import app.lastmineat.models.Deal;
import app.lastmineat.models.Redemption;
import app.lastmineat.models.User;
import app.lastmineat.models.Vendor;
import app.lastmineat.services.DealService;
import app.lastmineat.services.RedemptionService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewVendor extends AppCompatActivity {
    private TextView vendorHours, vendorAddress, vendorPostalCode, vendorContact, vendorEmail, vendorDescription;
    private ImageView vendorCallIcon;
    private Vendor vendor;
    private SwipeRefreshLayout viewVendorSwipeRefreshLayout;
    private RecyclerView viewVendorRecyclerView;
    private ViewVendorAdapter viewVendorAdapter;
    private Deal currentDeal;
    private User user;
    private MaterialDialog waitDialog;

    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private DealService dealService;
    private RedemptionService redemptionService;

    private final int REQUEST_CAMERA = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendor);

        //get vendor
        Intent intent = getIntent();
        vendor = (Vendor) intent.getSerializableExtra("vendor");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        //get shared preferences
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        user = gson.fromJson(sharedPreferences.getString("user", null), User.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(vendor.getName());

        vendorHours = (TextView) findViewById(R.id.vendor_hours);
        vendorAddress = (TextView) findViewById(R.id.vendor_address);
        vendorPostalCode = (TextView) findViewById(R.id.vendor_postal_code);
        vendorContact = (TextView) findViewById(R.id.vendor_contact);
        vendorEmail = (TextView) findViewById(R.id.vendor_email);
        vendorDescription = (TextView) findViewById(R.id.vendor_description);
        vendorCallIcon = (ImageView) findViewById(R.id.vendor_call_icon);
        viewVendorSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view_vendor_swipe_refresh_layout);
        viewVendorRecyclerView = (RecyclerView) findViewById(R.id.view_vendor_recycler_view);

        vendorHours.setText(vendor.getStartTime() + " - " + vendor.getEndTime());
        vendorAddress.setText(vendor.getAddress() + ", " + vendor.getUnitNo());
        vendorPostalCode.setText(vendor.getPostalCode());
        vendorContact.setText(vendor.getContactNo());
        vendorEmail.setText(vendor.getEmail());
        vendorDescription.setText(vendor.getDescription());
        vendorCallIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        vendorCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(ViewVendor.this)
                        .title("Are your sure you want to call " + vendor.getName() + "?")
                        .content(vendor.getContactNo())
                        .positiveText(getString(R.string.default_dialog_positive_text))
                        .negativeText(getString(R.string.default_dialog_negative_text))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //open dialer with phone number
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + vendor.getContactNo()));
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        //set refresh color
        viewVendorSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //set on refresh listener
        viewVendorSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                                retrieveDealList();
                            }
                        });
                    }
                };
                thread.start();
            }
        });

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
        dealService = retrofit.create(DealService.class);
        redemptionService = retrofit.create(RedemptionService.class);

        viewVendorSwipeRefreshLayout.setRefreshing(true);
        retrieveDealList();
    }

    private void retrieveDealList() {
        Call<List<Deal>> call = dealService.getDealListByVendorId(vendor.getId());
        call.enqueue(new Callback<List<Deal>>() {
            @Override
            public void onResponse(Call<List<Deal>> call, Response<List<Deal>> response) {
                viewVendorSwipeRefreshLayout.setRefreshing(false);
                List<Deal> dealList = response.body();
                Collections.sort(dealList, new Comparator<Deal>() {
                    @Override
                    public int compare(Deal d1, Deal d2) {
                        return d1.getEndDateTime().compareTo(d2.getEndDateTime());
                    }
                });
                if (viewVendorAdapter == null) {
                    //create activity adapter
                    viewVendorAdapter = new ViewVendorAdapter(ViewVendor.this, dealList);

                    viewVendorAdapter.setOnItemClickListener(new ViewVendorAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            currentDeal = viewVendorAdapter.getItem(position);

                            new MaterialDialog.Builder(ViewVendor.this)
                                    .title("Are your sure you want to redeem this deal?")
                                    .content(currentDeal.getDescription())
                                    .positiveText(getString(R.string.default_dialog_positive_text))
                                    .negativeText(getString(R.string.default_dialog_negative_text))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            if (ContextCompat.checkSelfPermission(ViewVendor.this,
                                                    Manifest.permission.CAMERA)
                                                    != PackageManager.PERMISSION_GRANTED) {

                                                ActivityCompat.requestPermissions(ViewVendor.this,
                                                        new String[]{Manifest.permission.CAMERA},
                                                        REQUEST_CAMERA);
                                            } else {
                                                goToScanner();
                                            }
                                        }
                                    })
                                    .show();
                        }
                    });

                    viewVendorRecyclerView.setAdapter(viewVendorAdapter);
                    viewVendorRecyclerView.setLayoutManager(new LinearLayoutManager(ViewVendor.this));
                } else {
                    viewVendorAdapter.clear();
                    viewVendorAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Deal>> call, Throwable t) {
                boolean isConnectionError = t instanceof IOException;

                //check if there is internet access
                if (isConnectionError) {
                    Toast.makeText(ViewVendor.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToScanner() {
        //show please wait dialog
        waitDialog = new MaterialDialog.Builder(ViewVendor.this)
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
                        Call<List<Redemption>> call = redemptionService.getRedemptionListByUserAndDealId(user.getId(), currentDeal.getId());
                        call.enqueue(new Callback<List<Redemption>>() {
                            @Override
                            public void onResponse(Call<List<Redemption>> call, Response<List<Redemption>> response) {
                                //dismiss please wait dialog
                                waitDialog.dismiss();

                                List<Redemption> redemptionList = response.body();

                                if (redemptionList.isEmpty()) {
                                    Intent intent = new Intent(ViewVendor.this, Decoder.class);
                                    intent.putExtra("deal", currentDeal);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ViewVendor.this, "Sorry, you have already redeemed this deal!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Redemption>> call, Throwable t) {
                                //dismiss please wait dialog
                                waitDialog.dismiss();

                                boolean isConnectionError = t instanceof IOException;

                                //check if there is internet access
                                if (isConnectionError) {
                                    Toast.makeText(ViewVendor.this, getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToScanner();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //slide right animation
        overridePendingTransition(R.anim.stay_still, R.anim.slide_right);
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
