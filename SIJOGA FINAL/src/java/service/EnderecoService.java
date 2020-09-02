package service;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import pojo.Endereco;
import util.HibernateUtil;

/**
 *
 * @author Erick
 */
public class EnderecoService {

    private static SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Endereco endereco) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(endereco);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("EndereçoService.saveOrUpdate():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Endereco endereco) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(endereco);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("EndereçoService.save():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static List<EnderecoService> findAll() {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Endereco.class);
            return c.list();
        } catch (HibernateException e) {
            System.out.println("EndereçoService.findAll():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static Endereco findByCPF(String cpf) {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Endereco.class);
            c.add(Restrictions.eq("pkCpf", cpf));
            return (Endereco) c.uniqueResult();
        } catch (HibernateException e) {
            System.out.println("EndereçoService.findByCPF():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void deleteByCPF(String cpf) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.delete(EnderecoService.findByCPF(cpf));
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("EndereçoService.deleteByCPF():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

}
