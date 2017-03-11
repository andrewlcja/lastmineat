package app.lastmineat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.lastmineat.R;
import app.lastmineat.activities.ViewVendor;
import app.lastmineat.adapters.ViewDealsAdapter;
import app.lastmineat.models.Deal;
import app.lastmineat.services.DealService;
import app.lastmineat.services.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewDeals extends Fragment {
    private SwipeRefreshLayout viewDealsSwipeRefreshLayout;
    private RecyclerView viewDealsRecyclerView;
    private ViewDealsAdapter viewDealsAdapter;

    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private DealService dealService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_deals, container, false);

        viewDealsSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.view_deals_swipe_refresh_layout);
        viewDealsRecyclerView = (RecyclerView) view.findViewById(R.id.view_deals_recycler_view);

        //set refresh color
        viewDealsSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //set on refresh listener
        viewDealsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                .baseUrl(getActivity().getString(R.string.api_endpoint))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //create service
        dealService = retrofit.create(DealService.class);

        viewDealsSwipeRefreshLayout.setRefreshing(true);
        retrieveDealList();
        return view;
    }

    private void retrieveDealList() {
        Call<List<Deal>> call = dealService.getDealList();
        call.enqueue(new Callback<List<Deal>>() {
            @Override
            public void onResponse(Call<List<Deal>> call, Response<List<Deal>> response) {
                viewDealsSwipeRefreshLayout.setRefreshing(false);
                List<Deal> dealList = response.body();
                Collections.sort(dealList, new Comparator<Deal>() {
                    @Override
                    public int compare(Deal d1, Deal d2) {
                        return d1.getEndDateTime().compareTo(d2.getEndDateTime());
                    }
                });
                if (viewDealsAdapter == null) {
                    //create activity adapter
                    viewDealsAdapter = new ViewDealsAdapter(getActivity(), dealList);

                    viewDealsAdapter.setOnItemClickListener(new ViewDealsAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent intent = new Intent(getActivity(), ViewVendor.class);
                            intent.putExtra("vendor", viewDealsAdapter.getItem(position).getVendor());
                            startActivity(intent);

                            //slide left animation
                            getActivity().overridePendingTransition(R.anim.slide_left, R.anim.stay_still);
                        }
                    });

                    viewDealsRecyclerView.setAdapter(viewDealsAdapter);
                    viewDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
                    viewDealsAdapter.clear();
                    viewDealsAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Deal>> call, Throwable t) {
                boolean isConnectionError = t instanceof IOException;

                //check if there is internet access
                if (isConnectionError) {
                    Toast.makeText(getActivity(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
