/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import facades.PessoaFacade;
import facades.ProcessoFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Advogado;
import pojo.AdvogadoParte;
import pojo.Juiz;
import pojo.Parte;
import pojo.Pessoa;
import pojo.Processo;
import service.AdvogadoParteService;
import service.ParteService;
import service.PessoaService;
import service.ProcessoListViewHelper;
import service.ProcessoService;
import util.Ferramentas;
import util.LoginSession;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "novoProcesso")
@RequestScoped
public class NovoProcesso {

    LoginSession sess = LoginSession.getInstance();
    ProcessoSession processoSess = ProcessoSession.getInstance();

    private Advogado advogado;
    private Pessoa pessoa;
    private String cpfPromovente = "";
    private String cpfPromovido = "";
    private int oabPromovido;
    private String oabPromovidoStr = "";

    Ferramentas ferramentas = Ferramentas.getInstance();

    public NovoProcesso() {
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

    public String getCpfPromovente() {
        return cpfPromovente;
    }

    public void setCpfPromovente(String cpfPromovente) {
        this.cpfPromovente = cpfPromovente;
    }

    public String getCpfPromovido() {
        return cpfPromovido;
    }

    public void setCpfPromovido(String cpfPromovido) {
        this.cpfPromovido = cpfPromovido;
    }

    public int getOabPromovido() {
        return oabPromovido;
    }

    public void setOabPromovido(int oabPromovido) {
        this.oabPromovido = oabPromovido;
    }

    public String getOabPromovidoStr() {
        return oabPromovidoStr;
    }

    public void setOabPromovidoStr(String oabPromovidoStr) {
        this.oabPromovidoStr = oabPromovidoStr;
    }

    // Métodos específicos
    public String salvarNovoProcesso() {
        // Limpar os cpfs (kkkk)
        this.cpfPromovente = ferramentas.removerCaracteresCpf(this.cpfPromovente);
        this.cpfPromovido = ferramentas.removerCaracteresCpf(cpfPromovido);

        // Verificar se CPFs informados são iguais
        if (this.cpfPromovente.equals(this.cpfPromovido)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF parte promovida e promovente não podem ser iguais", "Erro Novo Processo"));
            return "";
        }

        // Verificar se o processo nao está sendo aberto contra o advogado que está abrindo o processo
        if (cpfPromovido.equals(this.advogado.getPessoa().getPkCpf())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O CPF do promovido não pode ser o mesmo do advogado que está abrindo o processo", "Erro Novo Processo"));
            return "";
        }

        // Buscar Pessoas relacionadas aos CPFs
        Pessoa pessoaPromovente = PessoaService.getPessoaCadastrada(cpfPromovente);
        if (pessoaPromovente == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parte promovente não cadastrada. Cadastre-a no sistema", "Erro Novo Processo"));
            return "";
        }

        Pessoa pessoaPromovida = PessoaService.getPessoaCadastrada(cpfPromovido);
        if (pessoaPromovida == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Parte promovida não cadastrada. Cadastre-a no sistema", "Erro Novo Processo"));
            return "";
        }

        // Verificar se foi digitado um advogado parte promovida e tentar localiza-lo
        Advogado advogadoPromovido = null;
        if (!this.oabPromovidoStr.equals("")) {
            this.oabPromovido = Integer.parseInt(this.oabPromovidoStr);
            advogadoPromovido = PessoaService.getAdvogadoCadastrado(oabPromovido);
            if (advogadoPromovido == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Advogado da parte promovida não encontrado no sistema", "Erro Novo Processo"));
                return "";
            }
        }

        // Buscar o juíz com menos processos
        Juiz juizAssociado = PessoaService.getJuizMaisSusse(cpfPromovente, cpfPromovido);
        if (juizAssociado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foram encontrados juizes dispníveis para o processo. Tente mais tarde", "Erro Novo Processo"));
            return "";
        }

        // Criar as partes
        Parte promovido = new Parte();
        promovido.setPessoa(pessoaPromovida);
        promovido.setTipo("PROMOVIDO");

        Parte promovente = new Parte();
        promovente.setPessoa(pessoaPromovente);
        promovente.setTipo("PROMOVENTE");

        // Salvar as partes
        ParteService.save(promovente);
        ParteService.save(promovido);

        // Montar objeto AdvogadoParte;
        AdvogadoParte advogadoPartePromovente = new AdvogadoParte();
        advogadoPartePromovente.setAdvogado(this.advogado);
        advogadoPartePromovente.setParte(promovente);

        AdvogadoParte advogadoPartePromovido = new AdvogadoParte();
        advogadoPartePromovido.setAdvogado(advogadoPromovido);
        advogadoPartePromovido.setParte(promovido);

        // Salvar objetos AdvogadoParte
        AdvogadoParteService.save(advogadoPartePromovente);
        AdvogadoParteService.save(advogadoPartePromovido);

        // Montar o objeto processo
        Processo novoProcesso = new Processo();
        novoProcesso.setJuiz(juizAssociado);
        novoProcesso.setPromovente(promovente);
        novoProcesso.setPromovido(promovido);
        novoProcesso.setDataAbertura(new Date());
        novoProcesso.setStatus("ABERTO");
        
        // Salvar o objeto processo
        ProcessoService.save(novoProcesso);
        
        // Fetch do processo
        novoProcesso = ProcessoService.getProcessoById(novoProcesso.getPkId());

        // colocar o novo processo no processoSess
        ProcessoListViewHelper processoView = new ProcessoListViewHelper(novoProcesso);
        processoSess.setProcessoSel(novoProcesso);
        processoSess.setProcessoHelperSel(processoView);

        // redirecionar para Criação de fase.
        return "/advogado/nova-fase.jsf?faces-redirect=true";
    }

    /**
     * Cadastrar uma nova pessoa na etapa de novo processo
     *
     * @return URL para cdastrar uma nova pessoa
     */
    public String cadastrarNovaPessoa() {
        return "/advogado/nova-pessoa.jsf?faces-redirect=true";
    }
}
