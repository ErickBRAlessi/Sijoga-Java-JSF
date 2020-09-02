/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Juiz;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class JuizService {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Juiz juiz) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(juiz);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("JuizService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Juiz juiz) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(juiz);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("JuizService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

}
