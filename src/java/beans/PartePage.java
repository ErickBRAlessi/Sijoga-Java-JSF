/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import facades.PessoaFacade;
import facades.ProcessoFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Advogado;
import pojo.Pessoa;
import pojo.Processo;
import pojo.Usuario;
import service.AdvogadoParteService;
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
@Named(value = "partePage")
@RequestScoped
public class PartePage {

    private LoginSession sess = LoginSession.getInstance();
    private ProcessoSession processoSess = ProcessoSession.getInstance();
    private List<Processo> allProcessos = new ArrayList<>();
    private Processo processoSelecionado = null;
    private Usuario usuario = null;
    private Pessoa pessoa = null;
    private ProcessoListViewHelper processo;

    public PartePage() {
    }

    @PostConstruct
    public void init() {

        // Pega o usuário da sessão
        if (this.usuario == null) {
            Usuario usuarioSess = sess.getUsuarioLogado();
            if (usuarioSess != null) {
                this.usuario = usuarioSess;
                this.pessoa = usuario.getPessoa();
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
        this.allProcessos = ProcessoService.fetchProcessosParte(this.usuario.getPkCpf());
    }

    // Getters e Setters
    public List<Processo> getAllProcessos() {
        return allProcessos;
    }

    public void setAllProcessos(List<Processo> allProcessos) {
        this.allProcessos = allProcessos;
    }

    public Processo getProcessoSelecionado() {
        return processoSelecionado;
    }

    public void setProcessoSelecionado(Processo processoSelecionado) {
        this.processoSelecionado = processoSelecionado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ProcessoListViewHelper getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoListViewHelper processo) {
        this.processo = processo;
    }

    // Métodos específicos
    /**
     * Gerar relatório de processos da parte
     */
    public void gerarRelatorioParte() {
        try {
            //ARRUMAR PRA PEGAR O CPF DA PESSOA 
            Reports.gerarParteRelatorio(this.usuario.getPkCpf());
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível gerar relatório", "Erro ao gerar relatório"));
        }
    }

    /**
     * Verifica se o processo tem um advogado associado para a parte logada e
     * retorna uma classe para mostrar o íconde, caso não tenha
     *
     * @param p Processo que será verificado
     * @return Classe para mostrar ou esconder o ícone
     */
    public String getClass(Processo p) {
        String cpfPromovente = p.getPromovente().getPessoa().getPkCpf();
        String cpfPromovido = p.getPromovido().getPessoa().getPkCpf();
        String cpfUsuario = this.usuario.getPkCpf();
        
        if (cpfPromovente.equals(cpfUsuario) && p.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado() == null) {
            return "advogado-missing";
        }
        
        if (cpfPromovido.equals(cpfUsuario) && p.getPromovido().getListOfAdvogadoParte().get(0).getAdvogado() == null) {
            return "advogado-missing";
        }
        return "advogado-hide";

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
        String perfil = sess.getPerfilLogado();
        switch (perfil) {
            case "juiz":
                return "/juiz/processo-readonly.jsf?faces-redirect=true";
            case "advogado":
                return "/advogado/processo-readonly.jsf?faces-redirect=true";
            default:
                return "/parte/processo.jsf?faces-redirect=true";
        }
    }

    /**
     * Retorna para lista de todos os processos
     *
     * @return URL para página dos processos do advogado
     */
    public String voltarParaProcessos() {
        this.processoSelecionado = null;
        String perfil = sess.getPerfilLogado();
        switch (perfil) {
            case "juiz":
                return "/juiz/processos-readonly.jsf?faces-redirect=true";
            case "advogado":
                return "/advogado/processos-readonly.jsf?faces-redirect=true";
            default:
                return "/parte.jsf?faces-redirect=true";
        }

    }
}
