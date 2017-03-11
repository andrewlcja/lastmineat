package app.lastmineat.models;

import java.io.Serializable;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class Deal implements Serializable {
    private int id;
    private Vendor vendor;
    private String description;
    private double initialPrice;
    private int percentage;
    private int requiredUsers;
    private String startDateTime;
    private String endDateTime;
    private String photoUrl;
    private int redemptionLimit;
    private int redemptionCount;

    public Deal(int id, Vendor vendor, String description, double initialPrice, int percentage, int requiredUsers, String startDateTime, String endDateTime, String photoUrl, int redemptionLimit, int redemptionCount) {
        this.id = id;
        this.vendor = vendor;
        this.description = description;
        this.initialPrice = initialPrice;
        this.percentage = percentage;
        this.requiredUsers = requiredUsers;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.photoUrl = photoUrl;
        this.redemptionLimit = redemptionLimit;
        this.redemptionCount = redemptionCount;
    }

    public int getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getRequiredUsers() {
        return requiredUsers;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getRedemptionLimit() {
        return redemptionLimit;
    }

    public int getRedemptionCount() {
        return redemptionCount;
    }
}
