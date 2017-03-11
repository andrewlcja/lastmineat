/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.DealDAO;
import dao.RedemptionDAO;
import dao.UserDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import model.CreateRedemptionRequest;
import model.Deal;
import model.GenericResponse;
import model.Redemption;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class RedemptionService {
    @Autowired
    RedemptionDAO redemptionDAO;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    DealDAO dealDAO;
    
    public List<Redemption> getRedemptionListByUserIdRequest(int userId) throws Exception {
        return redemptionDAO.getRedemptionListByUserId(userId);
    }
    
    public List<Redemption> getRedemptionListByUserAndVendorIdRequest(int userId, int vendorId) throws Exception {
        return redemptionDAO.getRedemptionListByUserAndVendorId(userId, vendorId);
    }
    
    public List<Redemption> getRedemptionListByUserAndDealIdRequest(int userId, int dealId) throws Exception {
        return redemptionDAO.getRedemptionListByUserAndDealId(userId, dealId);
    }
    
    public GenericResponse createRedemptionRequest(CreateRedemptionRequest request) throws Exception {
        User user = userDAO.getUserById(request.getUserId());
        Deal deal = dealDAO.getDealById(request.getDealId());
        
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String dateTime = format.format(now);
        
        redemptionDAO.createRedemption(new Redemption(deal, user, dateTime));
        
        int redemptionCount = deal.getRedemptionCount();
        redemptionCount++;
        deal.setRedemptionCount(redemptionCount);
        dealDAO.updateDeal(deal);
        return new GenericResponse(true, null);
    }
}
