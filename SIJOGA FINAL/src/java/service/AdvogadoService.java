/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Advogado;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class AdvogadoService {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Advogado advogado) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(advogado);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("AdvogadoService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Advogado advogado) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(advogado);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

}
