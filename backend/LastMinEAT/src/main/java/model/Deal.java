/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author andrew.lim.2013
 */
@Entity
@Table(name = "deal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Deal implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "vendorId")
    private Vendor vendor;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "initialPrice")
    private double initialPrice;
    
    @Column(name = "percentage")
    private int percentage;
    
    @Column(name = "requiredUsers")
    private int requiredUsers;
    
    @Column(name = "startDateTime")
    private String startDateTime;
    
    @Column(name = "endDateTime")
    private String endDateTime;
    
    @Column(name = "photoUrl")
    private String photoUrl;
    
    @Column(name = "redemptionLimit")
    private int redemptionLimit;
    
    @Column(name = "redemptionCount")
    private int redemptionCount;

    public Deal() {
    }

    public Deal(Vendor vendor, String description, double initialPrice, int percentage, int requiredUsers, String startDateTime, String endDateTime, String photoUrl, int redemptionLimit, int redemptionCount) {
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

    public void setRedemptionCount(int redemptionCount) {
        this.redemptionCount = redemptionCount;
    }
}
