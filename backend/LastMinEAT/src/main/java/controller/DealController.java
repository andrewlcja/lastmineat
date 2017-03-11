/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.DealService;

/**
 *
 * @author andrew.lim.2013
 */
@Controller
public class DealController {
    @Autowired
    DealService dealService;
    
    @RequestMapping(value = "/GetDealListRequest", method = RequestMethod.GET)
    public @ResponseBody List<Deal> getDealListRequest() {
        List<Deal> dealList = null;
        try {
            dealList = dealService.getDealListRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dealList;
    }
    
    @RequestMapping(value = "/GetDealListByVendorIdRequest/{vendorId}", method = RequestMethod.GET)
    public @ResponseBody List<Deal> getDealListByVendorIdRequest(@PathVariable("vendorId") int vendorId) {
        List<Deal> dealList = null;
        try {
            dealList = dealService.getDealListByVendorIdRequest(vendorId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dealList;
    }
    
    @RequestMapping(value = "/GetDealByIdRequest/{id}", method = RequestMethod.GET)
    public @ResponseBody Deal getDealByIdRequest(@PathVariable("id") int id) {
        Deal deal = null;
        
        try {
            deal = dealService.getDealByIdRequest(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deal;
    }
}
