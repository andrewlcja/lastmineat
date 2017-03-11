/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author andrew.lim.2013
 */
@Entity
@Table(name = "redemption")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Redemption {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "dealId")
    private Deal deal;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;    
    
    @Column(name = "dateTime")
    private String dateTime;

    public Redemption() {
    }

    public Redemption(Deal deal, User user, String dateTime) {
        this.deal = deal;
        this.user = user;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public Deal getDeal() {
        return deal;
    }

    public User getUser() {
        return user;
    }

    public String getDateTime() {
        return dateTime;
    }
}
