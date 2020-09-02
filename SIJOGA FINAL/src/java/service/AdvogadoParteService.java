/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.AdvogadoParte;
import pojo.Parte;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class AdvogadoParteService {

    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    /**
     * Salva ou atualiza um objeto do tipo AdvogadoParte
     *
     * @param advogadoParte Objeto AdvogadoParte que será salvo
     */
    public static void saveOrUpdate(AdvogadoParte advogadoParte) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(advogadoParte);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("AdvogadoParteService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Cria um objeto do tipo AdvogadoParte
     *
     * @param parte Objeto que será criado no banco de dados
     */
    public static void save(AdvogadoParte advogadoParte) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(advogadoParte);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("AdvogadoParteService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }
}
