/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Redemption;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author andrew.lim.2013
 */
public class RedemptionDAO {
    @Autowired
    SessionFactory sessionFactory;

    Session session = null;
    Transaction tx = null;
    
    public void createRedemption(Redemption redemption) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        session.save(redemption);
        tx.commit();
        session.close();
    }
    
    public List<Redemption> getRedemptionListByUserAndVendorId(int userId, int vendorId) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Redemption> redemptionList = session.createCriteria(Redemption.class)
                .createAlias("deal.vendor", "vendor")
                .add(Restrictions.eq("user.id", userId))
                .add(Restrictions.eq("vendor.id", vendorId)).list();
        tx.commit();
        session.close();
        return redemptionList;
    }
    
    public List<Redemption> getRedemptionListByUserAndDealId(int userId, int dealId) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Redemption> redemptionList = session.createCriteria(Redemption.class)
                .add(Restrictions.eq("user.id", userId))
                .add(Restrictions.eq("deal.id", dealId)).list();
        tx.commit();
        session.close();
        return redemptionList;
    }
    
    public List<Redemption> getRedemptionListByUserId(int userId) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<Redemption> redemptionList = session.createCriteria(Redemption.class)
                .add(Restrictions.eq("user.id", userId)).list();
        tx.commit();
        session.close();
        return redemptionList;
    }
}
