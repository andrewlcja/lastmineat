/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.CreateRedemptionRequest;
import model.GenericResponse;
import model.Redemption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.DealService;
import service.RedemptionService;
import org.springframework.stereotype.Controller;

/**
 *
 * @author andrew.lim.2013
 */
@Controller
public class RedemptionController {
    @Autowired
    RedemptionService redemptionService;    
    
    @RequestMapping(value = "/GetRedemptionListByUserAndVendorIdRequest/{userId}/{vendorId}", method = RequestMethod.GET)
    public @ResponseBody List<Redemption> getRedemptionListByUserAndVendorIdRequest(@PathVariable("userId") int userId, @PathVariable("vendorId") int vendorId) {
        List<Redemption> redemptionList = null;
        try {
            redemptionList = redemptionService.getRedemptionListByUserAndVendorIdRequest(userId, vendorId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return redemptionList;
    }
    
    @RequestMapping(value = "/GetRedemptionListByUserAndDealIdRequest/{userId}/{dealId}", method = RequestMethod.GET)
    public @ResponseBody List<Redemption> getRedemptionListByUserAndDealIdRequest(@PathVariable("userId") int userId, @PathVariable("dealId") int dealId) {
        List<Redemption> redemptionList = null;
        try {
            redemptionList = redemptionService.getRedemptionListByUserAndDealIdRequest(userId, dealId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return redemptionList;
    }
    
    @RequestMapping(value = "/GetRedemptionListByUserIdRequest/{userId}", method = RequestMethod.GET)
    public @ResponseBody List<Redemption> getRedemptionListByUserIdRequest(@PathVariable("userId") int userId) {
        List<Redemption> redemptionList = null;
        try {
            redemptionList = redemptionService.getRedemptionListByUserIdRequest(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return redemptionList;
    }
    
    @RequestMapping(value = "/CreateRedemptionRequest", method = RequestMethod.POST)
    public @ResponseBody GenericResponse createRedemptionRequest(@RequestBody CreateRedemptionRequest request) {
        GenericResponse response = null;
        try {
            response = redemptionService.createRedemptionRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
