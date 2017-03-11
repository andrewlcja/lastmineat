package app.lastmineat.models;

import java.io.Serializable;

/**
 * Created by andrew.lim.2013 on 10/3/2017.
 */

public class Vendor implements Serializable {
    private int id;
    private String username;
    private String hashedPassword;
    private String salt;
    private String name;
    private String address;
    private String unitNo;
    private String postalCode;
    private String contactNo;
    private String email;
    private String description;
    private String startTime;
    private String endTime;
    private String photoUrl;

    public Vendor(int id, String username, String hashedPassword, String salt, String name, String address, String unitNo, String postalCode, String contactNo, String email, String description, String startTime, String endTime, String photoUrl) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.name = name;
        this.address = address;
        this.unitNo = unitNo;
        this.postalCode = postalCode;
        this.contactNo = contactNo;
        this.email = email;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
