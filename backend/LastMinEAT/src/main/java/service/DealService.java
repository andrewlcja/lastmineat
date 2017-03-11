/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.DealDAO;
import java.util.List;
import model.Deal;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class DealService {
    @Autowired
    DealDAO dealDAO;
    
    public List<Deal> getDealListRequest() throws Exception {
        return dealDAO.retrieveAll();
    }
    
    public List<Deal> getDealListByVendorIdRequest(int vendorId) throws Exception {
        return dealDAO.getDealListByVendorId(vendorId);
    }
    
    public Deal getDealByIdRequest(int id) throws Exception {
        return dealDAO.getDealById(id);
    }
}
