package app.lastmineat.services;

import java.util.List;

import app.lastmineat.models.Vendor;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public interface VendorService {
    @GET("GetVendorListRequest")
    Call<List<Vendor>> getVendorList();

    @GET("GetVendorByIdRequest/{id}")
    Call<Vendor> getVendorById(@Path("id") int id);
}
