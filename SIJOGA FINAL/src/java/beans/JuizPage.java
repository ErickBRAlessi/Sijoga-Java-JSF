/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import facades.PessoaFacade;
import facades.ProcessoFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Fase;
import pojo.Juiz;
import pojo.Pessoa;
import pojo.Processo;
import service.ProcessoListViewHelper;
import service.ProcessoService;
import util.LoginSession;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "juizPage")
@RequestScoped
public class JuizPage {
//

    LoginSession sess = LoginSession.getInstance();
    ProcessoSession processoSess = ProcessoSession.getInstance();
    List<Processo> allProcessos = new ArrayList<>();
    private ProcessoListViewHelper processo;
    private Processo processoSelecionado;
    Juiz juiz = null;
    private Pessoa pessoa = null;

    private String tipo;

//    private String oabNovoAdvStr;
//    private int oabNovoAdv;

    public JuizPage() {
    }

    @PostConstruct
    public void init() {
        // Pega o advogado da sessão
        if (this.juiz == null) {
            Juiz juizSess = sess.getUsuarioLogado().getPessoa().getListOfJuiz().get(0);
            if (juizSess != null) {
                this.juiz = juizSess;
                this.pessoa = juiz.getPessoa();
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
        this.allProcessos = ProcessoService.fetchProcessosJuiz(this.juiz.getPkOab());
    }

    // Getters e Setters
    public List<Processo> getAllProcessos() {
        return allProcessos;
    }

    public void setAllProcessos(List<Processo> allProcessos) {
        this.allProcessos = allProcessos;
    }

    public ProcessoListViewHelper getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoListViewHelper processo) {
        this.processo = processo;
    }

    public Processo getProcessoSelecionado() {
        return processoSelecionado;
    }

    public void setProcessoSelecionado(Processo processoSelecionado) {
        this.processoSelecionado = processoSelecionado;
    }

    public Juiz getJuiz() {
        return juiz;
    }

    public void setJuiz(Juiz juiz) {
        this.juiz = juiz;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

//    public String getOabNovoAdvStr() {
//        return oabNovoAdvStr;
//    }
//
//    public void setOabNovoAdvStr(String oabNovoAdvStr) {
//        this.oabNovoAdvStr = oabNovoAdvStr;
//    }
//
//    public int getOabNovoAdv() {
//        return oabNovoAdv;
//    }
//
//    public void setOabNovoAdv(int oabNovoAdv) {
//        this.oabNovoAdv = oabNovoAdv;
//    }

    // Métodos específicos
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
        return "/juiz/processo.jsf?faces-redirect=false";
    }

    /**
     * Retorna para lista de todos os processos
     *
     * @return URL para página dos processos do advogado
     */
    public String voltarParaProcessos() {
        this.processoSelecionado = null;
        return "/juiz.jsf?faces-redirect=true";
    }

    /**
     * Voltar para os detalhes do processo
     *
     * @return URL para a página do processo
     */
    public String voltarParaProcesso() {
        this.processoSess.setProcessoHelperSel(this.processo);
        this.processoSess.setProcessoSel(this.processoSelecionado);
        return "/juiz/processo.jsf?faces-redirect=true";
    }

    /**
     * Ir para processos como parte do advogado
     *
     * @return URL para processos como parte do advogado
     */
    public String irParaProcessosReadonly() {
        processoSess.setProcessoSel(null);
        return "/juiz/processos-readonly.jsf";
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
     * Verifica se a fase está em DELIBERACAO para renderizar ou nao os componentes de deliberacao
     * @param fase Fase para verificar se está em deliberaçao
     * @return True ou False dependendo do status da fase
     */
    public boolean checkRenderDeliberacao(Fase fase) {
        return (fase.getStatus().equals("DELIBERACAO"));
    }

//    public String gerarIntimacao() {
//        // Verificar se tem fase aguardando deliberação ou se o processo tá encerrado
//        int index = this.processo.getFases().size() - 1;
//        if (this.processo.getFases().get(index).getStatus().equals("Em deliberação")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível gerar intimação enquanto existe fase aguardando deliberação", "Erro intimação"));
//            return "";
//        }
//
//        // Caso não tenha fase aguardando liberação, criar fase automática de intimação
//        Fase faseIntimacao = new Fase(
//                index + 1,
//                null,
//                "[Fase gerada automaticamente] Criada intimação no sistema SOSIFOD",
//                "Solicitação de intimação",
//                "Processado",
//                null,
//                new Date(),
//                new Date(),
//                "informativa"
//        );
//
//        // Adicionar fase de intimação no processo selecionado
//        //this.processo.getFases().add(faseIntimacao);
//
//        // Adicionar fase de intimação no processo no banco de dados
//        this.processoFacade.adicionarFaseEmProcesso(faseIntimacao, this.processo.getId());
//
//        // Colocar processo no processoSession
//        this.processoSess.setProcessoSel(null);
//
//        // Fazer requisição para o webservice da intimação
//        
//        return "/juiz.jsf?faces-redirect=true";
//    }
//
//    public String encerramentoDeProcesso() {
//        // Verificar se tem fase aguardando deliberação
//        int index = this.processo.getFases().size() - 1;
//        if (this.processo.getFases().get(index).getStatus().equals("Em deliberação")) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível encerrar o processo enquanto houver fase em deliberação", "Erro intimação"));
//            return "";
//        }
//
//        // Caso não tenha fase aguardando liberação, realizar navegação
//        return "/juiz/encerrar.jsf";
//    }
//
//    public String encerrarProcesso() {
//        // Criar nova fase automática informativa de encerramento
//        int index = this.processo.getFases().size();
//        Fase faseEncerramento = new Fase(
//                index,
//                null,
//                "[Fase gerada automaticamente] Encerramento do processo: " + this.processo.getId(),
//                this.justificativa,
//                "Processado",
//                null,
//                new Date(),
//                new Date(),
//                "informativa"
//        );
//        
//        // Atualiza o processo selecionado
//        this.processo.setStatus("Encerrado");
//        this.processo.setVencedor(vencedor);
//        
//        // Atualiza o processo no banco de dados
//        this.processoFacade.atualizarProcessoEncerramento(this.processo);
//        
//        // Adicionar fase ao processo selecionado
//        //this.processo.getFases().add(faseEncerramento);
//
//        // Adicionar fase ao processo no banco de dados
//        this.processoFacade.adicionarFaseEmProcesso(faseEncerramento, this.processo.getId());
//
//        // Colocar processo no processoSession
//        this.processoSess.setProcessoSel(null);
//
//        // Retornar à página do processo
//        return "/juiz.jsf?faces-redirect=true";
//    }
//
//
//    public void realizarDeliberacao() {
//        // Selecionar a última fase do processo
//        int index = this.processo.getFases().size() - 1;
//
//        // Alterar a fase
//        this.processo.getFases().get(index).setJustificativaJuiz(justificativa);
//        this.processo.getFases().get(index).setStatus(decisao);
//
//        // Enviar alteração de fase para o banco de dados
//        this.processoFacade.atualizarFaseProcessoDeliberacao(this.processo.getFases().get(index), this.processo.getId(), index);
//
//        // Atualizar o processoSession
//        this.processoSess.setProcessoSel(this.processo);
//    }
//
//    public String selecionarProcessoReadonly(Processo processoSelecionado) {
//        this.processo = processoSelecionado;
//        this.processoSess.setProcessoSel(processoSelecionado);
//        return "/juiz/processo-readonly.jsf";
//    }
//
//    public String associarAdvogadoProcesso() {
//        if (this.processo.getPartePromovente().getCpf() == this.juiz.getCpf()) {
//            if (this.processo.getAdvogadoPromovente() != null) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Processo já possui advogado associado", "Erro Associar advogado"));
//                return "";
//            }
//        }
//
//        if (this.processo.getPartePromovida().getCpf() == this.juiz.getCpf()) {
//            if (this.processo.getAdvogadoPromovido() != null) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Processo já possui advogado associado", "Erro Associar advogado"));
//                return "";
//            }
//        }
//
//        this.processoSess.setProcessoSel(this.processo);
//        return "/juiz/set-adv.jsf";
//    }
//
//    public String associarAdvogado() {
//        // Verificar se foi digitado um advogado parte promovida e tentar localiza-lo
//        Advogado advogado = null;
//        String parte = "";
//        if (!this.oabNovoAdvStr.equals("")) {
//            this.oabNovoAdv = Integer.parseInt(this.oabNovoAdvStr);
//            advogado = pessoaFacade.getAdvogadoByOab(this.oabNovoAdv);
//            if (advogado == null) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Advogado não encontrado no sistema", "Erro Associar advogado"));
//                return "";
//            }
//        }
//        if (this.processo.getAdvogadoPromovente() == null) {
//            this.processo.setAdvogadoPromovente(advogado);
//            parte = "promovente";
//        } else {
//            this.processo.setAdvogadoPromovido(advogado);
//            parte = "promovido";
//        }
//        this.processoSess.setProcessoSel(processo);
//        this.processoFacade.atualizarProcesso(processo, parte);
//
//        return "/juiz/processo-readonly.jsf";
//    }
//
//    public String irParaProcessosParte() {
//        processos = processoFacade.getProcessosPorCpf(juiz.getCpf());
//        this.processoSess.setProcessosSel(processos);
//        processoSess.setProcessoSel(null);
//        return "/juiz/processos-readonly.jsf";
//    }
//
//    public String getClassAdv(Processo p) {
//        if ((p.getPartePromovente().getCpf().equals(this.juiz.getCpf()) && p.getAdvogadoPromovente() == null)
//                || (p.getPartePromovida().getCpf().equals(this.juiz.getCpf()) && p.getAdvogadoPromovido() == null)) {
//            return "advogado-missing";
//        } else {
//            return "advogado-hide";
//        }
//    }
//
//    public String getBGClass(String status) {
//        if (status == "Em deliberação") {
//            return "fase-deliberacao";
//        } else {
//            return "fase-ok";
//        }
//    }
//    
//    public boolean checkProcessoEncerrado() {
//        return !this.processo.getStatus().equals("Encerrado");
//    }
}
