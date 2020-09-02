/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Intimacao;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class IntimacaoService {
        private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    
    /**
     * Salva ou atualiza um objeto fase 
     * @param fase Objeto fase para ser salvo ou atualizado
     */
    public static void saveOrUpdate(Intimacao intimacao) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(intimacao);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("IntimacaoService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Cria um objeto fase no banco de dados
     * @param fase Objeto fase para ser criado
     */
    public static void save(Intimacao intimacao) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(intimacao);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("IntimacaoService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }
    
    /**
     * Buscar uma intimação pelo ID
     * @param id ID da intimação buscada
     * @return Objeto da intimação
     */
    public static Intimacao getIntimacaoById(int id) {
        Session s = factory.openSession();
        Intimacao intimacao;
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Intimacao i WHERE i.pkId = :idBuscado");
            query.setInteger("idBuscado", id);
            intimacao = (Intimacao)query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return intimacao;
    }
}
