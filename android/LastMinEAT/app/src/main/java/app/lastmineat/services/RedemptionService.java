package app.lastmineat.services;

import java.util.List;

import app.lastmineat.models.CreateRedemptionRequest;
import app.lastmineat.models.GenericResponse;
import app.lastmineat.models.Redemption;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by andrew.lim.2013 on 11/3/2017.
 */

public interface RedemptionService {
    @GET("GetRedemptionListByUserAndVendorIdRequest/{userId}/{vendorId}")
    Call<List<Redemption>> getRedemptionListByUserAndVendorId(@Path("userId") int userId, @Path("vendorId") int vendorId);

    @GET("GetRedemptionListByUserAndDealIdRequest/{userId}/{dealId}")
    Call<List<Redemption>> getRedemptionListByUserAndDealId(@Path("userId") int userId, @Path("dealId") int dealId);

    @GET("GetRedemptionListByUserIdRequest/{userId}")
    Call<List<Redemption>> getRedemptionListByUserId(@Path("userId") int userId);

    @POST("CreateRedemptionRequest")
    Call<GenericResponse> createRedemption(@Body CreateRedemptionRequest body);
}
