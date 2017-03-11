/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.User;
import model.Vendor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class VendorDAO {
    @Autowired
    SessionFactory sessionFactory;

    Session session = null;
    Transaction tx = null;
    
    public List<Vendor> retrieveAll() throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Vendor> vendorList = session.createCriteria(Vendor.class).list();
        tx.commit();
        session.close();
        return vendorList;
    }
    
    public Vendor getVendorById(int id) {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Vendor vendor = (Vendor) session.createCriteria(Vendor.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        tx.commit();
        session.close();
        return vendor;
    }
}
