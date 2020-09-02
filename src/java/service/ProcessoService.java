/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Fase;
import pojo.Processo;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class ProcessoService {

    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    public static void saveOrUpdate(Processo processo) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(processo);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    public static void save(Processo processo) {
        Session s = factory.openSession();
        try {
            s.beginTransaction();
            s.save(processo);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("PessoaService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Busca pelos processos de um juiz
     *
     * @param oab oab do juiz
     * @return Lista de processos
     */
    public static List<Processo> getProcessosJuiz(int oab) {
        Session s = factory.openSession();
        List<Processo> processos = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("select distinct p from Processo p, Juiz j where j.pkOab = :oab");
            query.setInteger("oab", oab);
            processos = (List<Processo>) query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    public static List<Processo> getProcessosAdvogado(int oab) {
        Session s = factory.openSession();
        List<Processo> processos = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("select distinct p from Processo p, Advogado a where a.pkOab = :oab");
            query.setInteger("oab", oab);
            processos = (List<Processo>) query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Busca os processos associados a um juiz pelo seu OAB
     *
     * @param oab OAB do juiz
     * @return Lista de processos do juiz (retorna lista vazia se não forem
     * encontrados processos)
     */
    public static List<Processo> fetchProcessosJuiz(int oab) {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT pr FROM Processo pr " +
                    "JOIN pr.juiz j WHERE j.pkOab = :oabBuscado ORDER BY pr.pkId asc");
            query.setInteger("oabBuscado", oab);
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Método bolado para buscar os processos de um advogado pelo seu OAB
     *
     * @param oab oab do advogado
     * @return Lista de processos (retorna lista vazia se não encontrar coisa
     * alguma)
     */
    public static List<Processo> fetchProcessosAdvogado(int oab) {
        List<Processo> processos = new ArrayList<>();
        List<Processo> processosPromovidos;
        processosPromovidos = fetchProcessosPromovidoAdvogado(oab);
        List<Processo> processosPromoventes;
        processosPromoventes = fetchProcessosPromoventeAdvogado(oab);
        processos.addAll(processosPromovidos);
        processos.addAll(processosPromoventes);

        return processos;
    }

    /**
     * Busca os processos em que um advogado está como parte promovida
     *
     * @param oab OAB do advogado
     * @return Lista de processos (retorna lista vazia se não for encontrado
     * alguma coisa)
     */
    public static List<Processo> fetchProcessosPromovidoAdvogado(int oab) {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT pr FROM Processo pr "
                    + "JOIN pr.promovido pe JOIN pe.listOfAdvogadoParte ap "
                    + "JOIN ap.advogado a WHERE a.pkOab = :oabBuscado ORDER BY pr.pkId asc");
            query.setInteger("oabBuscado", oab);
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Busca os processos em que um advogado está como parte promovente
     *
     * @param oab OAB do advogado
     * @return Lista de processos (retorna lista vazia se não for encotrada
     * alguma coisa)
     */
    public static List<Processo> fetchProcessosPromoventeAdvogado(int oab) {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT pr FROM Processo pr "
                    + "JOIN pr.promovente pe JOIN pe.listOfAdvogadoParte ap "
                    + "JOIN ap.advogado a WHERE a.pkOab = :oabBuscado ORDER BY pr.pkId asc" );
            query.setInteger("oabBuscado", oab);
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Método bolado para buscar os processos de uma parte pelo seu CPF
     *
     * @param cpf CPF da parte
     * @return Lista de processos (retorna lista vazia se não encontrar coisa
     * alguma)
     */
    public static List<Processo> fetchProcessosParte(String cpf) {
        List<Processo> processos = new ArrayList<>();
        List<Processo> processosPromovidos;
        processosPromovidos = fetchProcessosPromovidoParte(cpf);
        List<Processo> processosPromoventes;
        processosPromoventes = fetchProcessosPromoventeParte(cpf);
        processos.addAll(processosPromovidos);
        processos.addAll(processosPromoventes);

        return processos;
    }

    /**
     * Busca lista de processos de uma parte, pelo CPF, associada como
     * promovente
     *
     * @param cpf CPF da parte
     * @return Lista de processos (retorna lista vazia se não forem encontrados
     * os processos
     */
    public static List<Processo> fetchProcessosPromoventeParte(String cpf) {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT pr FROM Processo pr " +
                    "JOIN pr.promovente pe JOIN pe.pessoa ps " +
                    "WHERE ps.pkCpf = :cpfBuscado ORDER BY pr.pkId asc");
            query.setString("cpfBuscado", cpf);
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Busca lista de processos de uma parte, pelo CPF, associada como promovido
     *
     * @param cpf CPF da parte
     * @return Lista de processos (retorna lista vazia se não forem encontrados
     * os processos
     */
    public static List<Processo> fetchProcessosPromovidoParte(String cpf) {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT pr FROM Processo pr " +
                    "JOIN pr.promovido pe JOIN pe.pessoa ps " + 
                    "WHERE ps.pkCpf = :cpfBuscado ORDER BY pr.pkId asc");
            query.setString("cpfBuscado", cpf);
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }

    /**
     * Busca pelas fases de um processo com base em seu ID
     *
     * @param processoId ID do processo
     * @return Lista de fases (retorna lista vazia se não for encontrado coisa
     * alguma)
     */
    public static List<Fase> fetchFasesDeProcesso(int processoId) {
        Session s = factory.openSession();
        List<Fase> fases = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("SELECT DISTINCT f FROM Fase f "
                    + "JOIN f.processo pr WHERE pr.pkId = :processoBuscado "
                    + "ORDER BY f.pkId asc");
            query.setInteger("processoBuscado", processoId);
            fases = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return fases;
    }
    
    /**
     * Retorna todos os processos existentes no sistema
     * @return 
     */
    public static List<Processo> fetchProcessos() {
        Session s = factory.openSession();
        List<Processo> processos = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Processo p");
            processos = query.list();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processos;
    }
    
    /**
     * Buscar um processo pelo seu ID
     * @param id ID do processo a ser buscado
     * @return Objeto Processo
     */
    public static Processo getProcessoById(int id) {
         Session s = factory.openSession();
        Processo processo = null;
        try {
            s.beginTransaction();
            Query query = s.createQuery("FROM Processo p WHERE p.pkId = :idBuscado");
            query.setInteger("idBuscado", id);
            processo = (Processo) query.uniqueResult();
        } catch (HibernateException e) {
            throw e;
        } finally {
            s.close();
        }
        return processo;
    }            
}
