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
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author andrew.lim.2013
 */
@Entity
@Table(name = "vendor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vendor implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "hashedPassword")
    private String hashedPassword;
    
    @Column(name = "salt")
    private String salt;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "unitNo")
    private String unitNo;
    
    @Column(name = "postalCode")
    private String postalCode;
    
    @Column(name = "contactNo")
    private String contactNo;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "startTime")
    private String startTime;
    
    @Column(name = "endTime")
    private String endTime;
        
    @Column(name = "photoUrl")
    private String photoUrl;

    public Vendor() {
    }
    
    public Vendor(String username, String hashedPassword, String salt, String name, String address, String unitNo, String postalCode, String contactNo, String email, String description, String startTime, String endTime, String photoUrl) {
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
