/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import facades.PessoaFacade;
import facades.ProcessoFacade;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Advogado;
import pojo.Fase;
import pojo.Pessoa;
import pojo.Processo;
import service.PessoaService;
import service.ProcessoListViewHelper;
import service.ProcessoService;
import util.LoginSession;
import util.ProcessoSession;
import util.Reports;

/**
 *
 * @author cassiano
 */
@Named(value = "advogadoPage")
@RequestScoped
public class AdvogadoPage {

    private LoginSession sess = LoginSession.getInstance();
    private ProcessoSession processoSess = ProcessoSession.getInstance();
    private List<Processo> allProcessos = new ArrayList<>();
    private Processo processoSelecionado = null;
    private Advogado advogado = null;
    private Pessoa pessoa = null;
    private ProcessoListViewHelper processo;

    private String oabNovoAdvStr;
    private int oabNovoAdv;

    public AdvogadoPage() {
    }

    @PostConstruct
    public void init() {

        // Pega o advogado da sessão
        if (this.advogado == null) {
            Advogado advogadoSess = sess.getUsuarioLogado().getPessoa().getListOfAdvogado().get(0);
            if (advogadoSess != null) {
                this.advogado = advogadoSess;
                this.pessoa = advogado.getPessoa();
            }
        }

        //TODO: talvez tirar essa parte de pegar um processo da sessão.
        // Se tiver um processo no processSess pega ela
        Processo processoTemp = this.processoSess.getProcessoSel();
        if (processoTemp != null) {
            this.processoSelecionado = processoTemp;
        }

        // Se tiver um processoHelper no processSess pega ele
        ProcessoListViewHelper processoHelperTemp = this.processoSess.getProcessoHelperSel();
        if (processoHelperTemp != null) {
            this.processo = processoHelperTemp;
        }

        // Carregamento de processos 
        this.allProcessos = ProcessoService.fetchProcessosAdvogado(this.advogado.getPkOab());

    }

    // Getters e Setters
    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Processo> getAllProcessos() {
        return allProcessos;
    }

    public void setAllProcessos(List<Processo> allProcessos) {
        this.allProcessos = allProcessos;
    }

    public String getOabNovoAdvStr() {
        return oabNovoAdvStr;
    }

    public void setOabNovoAdvStr(String oabNovoAdvStr) {
        this.oabNovoAdvStr = oabNovoAdvStr;
    }

    public int getOabNovoAdv() {
        return oabNovoAdv;
    }

    public void setOabNovoAdv(int oabNovoAdv) {
        this.oabNovoAdv = oabNovoAdv;
    }

    public Processo getProcessoSelecionado() {
        return processoSelecionado;
    }

    public void setProcessoSelecionado(Processo processoSelecionado) {
        this.processoSelecionado = processoSelecionado;
    }

    public ProcessoListViewHelper getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoListViewHelper processo) {
        this.processo = processo;
    }

    // Métodos específicos     
    /**
     * Gerar relatório processos abertos do advogado
     */
    public void gerarRelatorioAdvogadoAberto() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInicio;
        try {
            dataInicio = sdf.parse("01/01/1900");
            Date dataFim = new Date();
            Reports.gerarAdvAbertosReport(advogado.getPkOab(), dataInicio, dataFim);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível gerar relatório", "Erro ao gerar relatório"));
        }
    }

    /**
     * Gerar relatório de processos encerrados do advogado
     */
    public void gerarRelatorioAdvogadoEncerrado() {
        try {
            Reports.gerarAdvEncerradosReport(advogado.getPkOab());
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível gerar relatório", "Erro ao gerar relatório"));
        }
    }

    /**
     * Método para verificar se precisa mostrar a notificação e que um processo
     * está em deliberação
     *
     * @param fases fases do processo
     * @return classe para mostrar ou ocultar o ícone
     */
    public String getClass(List<Fase> fases) {
        if (fases.size() == 0) {
            return "alerta-hidden";
        }
        Collections.sort(fases, (o1, o2) -> o1.getPkId().compareTo(o2.getPkId()));
        String status = fases.get(fases.size() - 1).getStatus();
        if (status.equals("DELIBERACAO")) {
            return "alerta-acao";
        } else {
            return "alerta-hidden";
        }
    }

    /**
     * Retorna uma String para informar se o vencedor de um proceso é a parte
     * promovida, promovente ou então se não há um vencedor ainda.
     *
     * @param p Processo
     * @return String informando vencedor: PROMOVENTE, PROMOVIDO ou N/A
     */
    public String getParteVencedora(Processo p) {
        if (p.getVencedor() == null) {
            return "N/A";
        }
        return (p.getVencedor().getPessoa().getPkCpf().equals(p.getPromovente().getPessoa().getPkCpf()) ? "PROMOVENTE" : "PROMOVIDO");
    }

    /**
     * Seleciona um processo da lista para exibir os detalhes
     *
     * @param processo Processo selecionado
     * @return URL para página do processo em detalhes
     */
    public String selecionarProcesso(Processo processoView) {
        this.processoSelecionado = processoView;
        this.processoSess.setProcessoSel(processoView);
        this.processo = new ProcessoListViewHelper(processoView);
        this.processoSess.setProcessoHelperSel(processo);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("processoSel", processoView);
        return "/advogado/processo.jsf?faces-redirect=false";
    }

    /**
     * Retorna para lista de todos os processos
     *
     * @return URL para página dos processos do advogado
     */
    public String voltarParaProcessos() {
        this.processoSelecionado = null;
        return "/advogado.jsf?faces-redirect=true";
    }

    /**
     * Ir para página de criação de novos processos
     * @return URL da página de criação de novos processos
     */
    public String criarNovoProcesso() {
        processoSess.setProcessoSel(null);
        return "/advogado/novo-processo.jsf?faces-redirect=true";
    }

//    public String selecionarProcessoReadonly(Processo processo) {
//        this.processoSelecionado = processo;
//        this.processoSess.setProcessoSel(processo);
//        return "/advogado/processo-readonly.jsf";
//    }
//
//    public String voltarParaProcessoReadonly() {
//        return "/advogado/processo-readonly.jsf";
//    }

    /**
     * Ir para processos como parte do advogado
     *
     * @return URL para processos como parte do advogado
     */
    public String irParaProcessosReadonly() {
        processoSess.setProcessoSel(null);
        return "/advogado/processos-readonly.jsf";
    }
}
