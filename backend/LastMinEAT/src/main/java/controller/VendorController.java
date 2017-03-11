/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;
import service.VendorService;

/**
 *
 * @author andrew.lim.2013
 */
@Controller
public class VendorController {
    @Autowired
    VendorService vendorService;
    
    @RequestMapping(value = "/GetVendorListRequest", method = RequestMethod.GET)
    public @ResponseBody List<Vendor> getVendorListRequest() {
        List<Vendor> vendorList = null;
        try {
            vendorList = vendorService.getVendorListRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vendorList;
    }
    
    @RequestMapping(value = "/GetVendorByIdRequest/{id}", method = RequestMethod.GET)
    public @ResponseBody Vendor getVendorByIdRequest(@PathVariable("id") int id) {
        Vendor vendor = null;
        
        try {
            vendor = vendorService.getVendorByIdRequest(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vendor;
    }
}
