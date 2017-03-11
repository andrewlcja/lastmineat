package app.lastmineat.services;

import java.util.List;

import app.lastmineat.models.Deal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public interface DealService {
    @GET("GetDealListRequest")
    Call<List<Deal>> getDealList();

    @GET("GetDealListByVendorIdRequest/{vendorId}")
    Call<List<Deal>> getDealListByVendorId(@Path("vendorId") int vendorId);

    @GET("GetDealByIdRequest/{id}")
    Call<Deal> getDealById(@Path("id") int id);
}
