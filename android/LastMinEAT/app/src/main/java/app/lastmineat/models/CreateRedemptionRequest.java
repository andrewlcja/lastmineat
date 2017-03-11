package app.lastmineat.models;

/**
 * Created by andrew.lim.2013 on 11/3/2017.
 */

public class CreateRedemptionRequest {
    private int dealId;
    private int userId;

    public CreateRedemptionRequest(int dealId, int userId) {
        this.dealId = dealId;
        this.userId = userId;
    }
}
