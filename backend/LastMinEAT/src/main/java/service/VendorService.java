/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.VendorDAO;
import java.util.List;
import model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class VendorService {
    @Autowired
    VendorDAO vendorDAO;
    
    public List<Vendor> getVendorListRequest() throws Exception {
        return vendorDAO.retrieveAll();
    }
    
    public Vendor getVendorByIdRequest(int id) throws Exception {
        return vendorDAO.getVendorById(id);
    }
}
