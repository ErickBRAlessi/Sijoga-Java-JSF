package service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import pojo.Advogado;
import pojo.Juiz;
import pojo.Pessoa;
import pojo.Usuario;
import util.HibernateUtil;

/**
 *
 * @author Erick
 */
public class UsuarioService {

    private static SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Usuario usuario) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(usuario);
            s.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Usuario usuario) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(usuario);
            s.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            s.close();
        }
    }

    public static List<Usuario> findAll() {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Usuario.class);
            return (List<Usuario>) c.list();
        } catch (Exception e) {
            throw e;
        } finally {
            s.close();
        }
    }

    public static Usuario findByCPF(String cpf) {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Usuario.class);
            c.add(Restrictions.eq("pkCpf", cpf));
            return (Usuario) c.uniqueResult();
        } catch (Exception e) {
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
            s.delete(UsuarioService.findByCPF(cpf));
            s.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            s.close();
        }
    }

    //autentica baseado no email e senha
    public static Usuario canLogin(String email, String senha) {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = null;
        usuarios = findAll();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                usuario = u;
                break;
            }
        }
        return usuario;
    }

    //Busca se usuário está na tabela de juizes
    public static Juiz buscaJuizPorEmail(String emailBuscado) {
        Session s = null;
        Juiz j = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            String email = emailBuscado;
            Query query = s.createQuery("select distinct j from Juiz j, Usuario u "
                    + "where u.email = :email");
            query.setString("email", email);
            j = (Juiz) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return j;
    }

    //Busca se usuario está na tabela de advogados
    public static Advogado buscaAdvogadoPorEmail(String emailBuscado) {
        Session s = null;
        Advogado a = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            String email = emailBuscado;
            Query query = s.createQuery("select distinct a from Advogado a, Usuario u "
                    + "where u.email = :email");
            query.setString("email", email);
            a = (Advogado) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return a;
    }

    //Busca se usuário setá na tabela de pessoas
    public static Pessoa buscaPessoaPorEmail(String emailBuscado) {
        Session s = null;
        Pessoa p = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            String email = emailBuscado;
            Query query = s.createQuery("select distinct p from Pessoa p, Usuario u "
                    + "where u.email = :email");
            query.setString("email", email);
            p = (Pessoa) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return p;
    }

    //Busca usuario global por email
    public static Usuario buscaUsuarioPorEmail(String emailBuscado) {
        Session s = factory.openSession();
        Usuario u = null;
        try {
            s.beginTransaction();
            String email = emailBuscado;
            Query query = s.createQuery("select distinct u from Usuario u "
                    + "where u.email = :email");
            query.setString("email", email);
            u = (Usuario) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return u;
    }

}
