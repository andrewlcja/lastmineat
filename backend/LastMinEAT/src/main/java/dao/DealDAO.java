/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Deal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class DealDAO {
    @Autowired
    SessionFactory sessionFactory;

    Session session = null;
    Transaction tx = null;
    
    public List<Deal> retrieveAll() throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Deal> vendorList = session.createCriteria(Deal.class).list();
        tx.commit();
        session.close();
        return vendorList;
    }
    
    public List<Deal> getDealListByVendorId(int vendorId) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Deal> dealList = session.createCriteria(Deal.class)
                .add(Restrictions.eq("vendor.id", vendorId)).list();
        tx.commit();
        session.close();
        return dealList;
    }
    
    public Deal getDealById(int id) {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Deal deal = (Deal) session.createCriteria(Deal.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        tx.commit();
        session.close();
        return deal;
    }
    
    public void updateDeal(Deal deal) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        session.update(deal);
        tx.commit();
        session.close();
    }
}
