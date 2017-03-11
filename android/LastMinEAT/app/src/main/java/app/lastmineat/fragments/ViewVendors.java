package app.lastmineat.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import app.lastmineat.R;
import app.lastmineat.activities.ViewVendor;
import app.lastmineat.adapters.ViewDealsAdapter;
import app.lastmineat.adapters.ViewVendorsAdapter;
import app.lastmineat.models.Deal;
import app.lastmineat.models.Vendor;
import app.lastmineat.services.DealService;
import app.lastmineat.services.VendorService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewVendors extends Fragment {
    private SwipeRefreshLayout viewVendorsSwipeRefreshLayout;
    private RecyclerView viewVendorsRecyclerView;
    private ViewVendorsAdapter viewVendorsAdapter;

    private Retrofit retrofit;
    private VendorService vendorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_vendors, container, false);

        viewVendorsSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.view_vendors_swipe_refresh_layout);
        viewVendorsRecyclerView = (RecyclerView) view.findViewById(R.id.view_vendors_recycler_view);

        //set refresh color
        viewVendorsSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //set on refresh listener
        viewVendorsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                retrieveVendorList();
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
                .baseUrl(getActivity().getString(R.string.api_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //create service
        vendorService = retrofit.create(VendorService.class);

        viewVendorsSwipeRefreshLayout.setRefreshing(true);
        retrieveVendorList();

        return view;
    }

    private void retrieveVendorList() {
        Call<List<Vendor>> call = vendorService.getVendorList();
        call.enqueue(new Callback<List<Vendor>>() {
            @Override
            public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
                viewVendorsSwipeRefreshLayout.setRefreshing(false);
                if (viewVendorsAdapter == null) {
                    //create activity adapter
                    viewVendorsAdapter = new ViewVendorsAdapter(getActivity(), response.body());

                    viewVendorsAdapter.setOnItemClickListener(new ViewVendorsAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent intent = new Intent(getActivity(), ViewVendor.class);
                            intent.putExtra("vendor", viewVendorsAdapter.getItem(position));
                            startActivity(intent);

                            //slide left animation
                            getActivity().overridePendingTransition(R.anim.slide_left, R.anim.stay_still);
                        }
                    });

                    viewVendorsRecyclerView.setAdapter(viewVendorsAdapter);
                    viewVendorsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
                    viewVendorsAdapter.clear();
                    viewVendorsAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Vendor>> call, Throwable t) {
                boolean isConnectionError = t instanceof IOException;

                //check if there is internet access
                if (isConnectionError) {
                    Toast.makeText(getActivity(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
