    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Parte;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class ParteService {
        private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    
    /**
     * Salva ou atualiza um objeto do tipo Parte
     * @param parte Objeto parte que será salvo
     */
    public static void saveOrUpdate(Parte parte) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(parte);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("ParteService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Cria um objeto do tipo Parte
     * @param parte Objeto que será criado no banco de dados
     */
    public static void save(Parte parte) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(parte);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("ParteService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }
}
