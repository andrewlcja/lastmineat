/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author andrew.lim.2013
 */
public class CreateRedemptionRequest {
    private int dealId;
    private int userId;

    public CreateRedemptionRequest() {
    }

    public CreateRedemptionRequest(int dealId, int userId) {
        this.dealId = dealId;
        this.userId = userId;
    }

    public int getDealId() {
        return dealId;
    }

    public int getUserId() {
        return userId;
    }
}
