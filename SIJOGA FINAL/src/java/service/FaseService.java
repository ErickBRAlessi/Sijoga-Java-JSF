/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import pojo.Fase;
import pojo.Intimacao;
import pojo.Parte;
import pojo.Processo;
import util.HibernateUtil;

/**
 *
 * @author cassiano
 */
public class FaseService {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();
    
    /**
     * Salva ou atualiza um objeto fase 
     * @param fase Objeto fase para ser salvo ou atualizado
     */
    public static void saveOrUpdate(Fase fase) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.saveOrUpdate(fase);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("FaseService.saveOrUpdate(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }

    /**
     * Cria um objeto fase no banco de dados
     * @param fase Objeto fase para ser criado
     */
    public static void save(Fase fase) {
        Session s = null;
        try {
            s = factory.openSession();
            s.beginTransaction();
            s.save(fase);
            s.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("FaseService.save(): " + e.getMessage());
            throw e;
        } finally {
            s.close();
        }
    }
    
    /**
     * Cria uma nova fase automática quando uma intimaçõ é executada no SOSIFOD
     * @param idProcesso 
     */
    public static void criarFaseIntimacaoExecutada(int idProcesso) {
        
        // Buscar  processo referente a fase
        Processo processo = ProcessoService.getProcessoById(idProcesso);
        
        // Buscar fases do processo (já ordenada por ID crescente) 
        List<Fase> fases = ProcessoService.fetchFasesDeProcesso(idProcesso);
        
        // Buscar a intimação para buscar quem é a parte intimada
        Fase fase = fases.get(fases.size() - 1);
        Parte intimado = null;
        if (fase.getListOfIntimacao() != null || fase.getListOfIntimacao().size() > 0) {
            Intimacao intimacao = fase.getListOfIntimacao().get(0);
            intimado = intimacao.getIntimado();
        }
        
        // Textos padrões
        String titulo = "[SIJOGA] Intimação executada";
        String reclamacao = "[Fase gerada automaticamente] Intimação executada para a parte";
                
        // Cria objeto nova fase
        Fase novaFase = new Fase();
        novaFase.setDataAbertura(new Date());
        novaFase.setDataDecisao(null);
        novaFase.setDocumento(null);
        novaFase.setJustificativaJuiz("N/A");
        novaFase.setParte(intimado);
        novaFase.setProcesso(processo);
        novaFase.setReclamacaoParte(reclamacao);
        novaFase.setStatus("INFORMATIVA");
        novaFase.setTitulo(titulo);
        
        // Salvar nova fase
        FaseService.save(novaFase);
    }
}
