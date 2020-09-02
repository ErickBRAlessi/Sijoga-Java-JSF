package service;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.criterion.Restrictions;
import pojo.Advogado;
import pojo.Juiz;
import pojo.Pessoa;
import pojo.Processo;
import pojo.Usuario;
import util.HibernateUtil;

/**
 *
 * @author Erick & Cassiano
 */
public class PessoaService {

    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Pessoa pessoa) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(pessoa);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Pessoa pessoa) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(pessoa);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static List<Pessoa> findAll() {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Pessoa.class);
            return c.list();
        } catch (HibernateException e) {
            System.out.println("PessoaService.findAll(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static Pessoa findByCPF(String cpf) {
        Session s = null;
        try {
            s = factory.openSession();
            Criteria c = s.createCriteria(Pessoa.class);
            c.add(Restrictions.eq("pkCpf", cpf));
            return (Pessoa) c.uniqueResult();
        } catch (HibernateException e) {
            System.out.println("PessoaService.findByCPF():" + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static Pessoa findByEmail(String email) {
        Session s = null;
        try {
            s = factory.openSession();
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("select distinct p from Pessoa p, Usuario u "
                    + "join fetch p.listOfUsuario "
                    + "where u.pkCpf = p.pkCpf "
                    + "and u.email = :email");
            query.setString("email", email);
            session.getTransaction().commit();
            Pessoa _p = (Pessoa) query.uniqueResult();

            session.beginTransaction();
            session.createQuery("select distinct p from Pessoa p "
                    + "join fetch p.listOfEndereco e "
                    + "where  p in :pessoa").setParameter("pessoa", _p);
            session.getTransaction().commit();

            _p = (Pessoa) query.uniqueResult();

            return _p;
        } catch (HibernateException e) {
            System.out.println("Erro PessoaService.findByEmail(): " + e.getMessage());
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
            s.delete(PessoaService.findByCPF(cpf));
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.deleteByCpf(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Busca uma lista de pessoas cadastradas
     *
     * @return lista de pessoas
     */
    public static List<Pessoa> fetchPessoas() {
        Session s = factory.openSession();
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Pessoa p");
            pessoas = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return pessoas;
    }

    /**
     * Busca uma lista de juizes cadastrados
     *
     * @return lista de juizes
     */
    public static List<Juiz> fetchJuizes() {
        Session s = factory.openSession();
        List<Juiz> juizes = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Juiz j");
            juizes = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return juizes;
    }

    /**
     * Buscar uma pessoa pelo CPF
     *
     * @param cpf CPF da pessoa que se deseja buscar
     * @return Objeto pessoa
     */
    public static Pessoa getPessoaCadastrada(String cpf) {
        Session s = factory.openSession();
        Pessoa pessoa = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Pessoa p WHERE p.pkCpf = :cpfBuscado");
            query.setString("cpfBuscado", cpf);
            pessoa = (Pessoa) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return pessoa;
    }

    /**
     * Buscar um advogado pelo OAB
     *
     * @param oab OAB do advogado que se deseja buscar
     * @return objeto Advogado
     */
    public static Advogado getAdvogadoCadastrado(int oab) {
        Session s = factory.openSession();
        Advogado advogado = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Advogado a WHERE a.pkOab = :oabBuscado");
            query.setInteger("oabBuscado", oab);
            advogado = (Advogado) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return advogado;
    }

    // Buscar juiz com menos processos
    public static Juiz getJuizMaisSusse(String promovente, String promovido) {
        Juiz juiz = null;
        List<Processo> processos = ProcessoService.fetchProcessos();
        List<Juiz> juizes = fetchJuizes();
        
        // Verifica se os juizes estão vazio. Se tiver retorna um objeto nulo
        if (juizes == null || juizes.isEmpty()) {
            return null;
        }

        // Verifica se os processos estão vazio... Se estiverem, retorna o primeiro juíz da lista.
        if (processos.isEmpty()) {
            if (!juizes.isEmpty()) {
                return juizes.get(0);
            }
        }
        
        // Iterar nos processos
        int index = -1;
        int menorQuantidade = -1;
        int quantidadeAtual = 0;

        for (int i = 0; i < juizes.size(); i++) {
            quantidadeAtual = 0;
            if (!juizes.get(i).getPessoa().getPkCpf().equals(promovente) && !juizes.get(i).getPessoa().getPkCpf().equals(promovido)) {
                for (Processo p : processos) {
                    if (p.getJuiz().getPkOab() == juizes.get(i).getPkOab()) {
                        quantidadeAtual++;
                    }
                }
                if (menorQuantidade == -1) {
                    index = i;
                    menorQuantidade = quantidadeAtual;
                } else {
                    if (quantidadeAtual < menorQuantidade) {
                        index = i;
                        menorQuantidade = quantidadeAtual;
                    }
                }
            }
        }
        if (index > -1) {
            juiz = juizes.get(index);
        }
        return juiz;
    }
    
        /**
     * Buscar um juiz pelo OAB
     * @param oab OAB para busca do juiz
     * @return Objeto Juiz
     */
    public static Juiz getJuizCadastrado(int oab) {
        Session s = factory.openSession();
        Juiz juiz = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Juiz j WHERE j.pkOab = :oabBuscado");
            query.setInteger("oabBuscado", oab);
            juiz = (Juiz) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return juiz;
    }

}
