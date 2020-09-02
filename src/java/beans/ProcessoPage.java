/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pojo.Advogado;
import pojo.Fase;
import pojo.Intimacao;
import pojo.IntimacaoSosifod;
import pojo.OficialJustica;
import pojo.Parte;
import pojo.Pessoa;
import pojo.Processo;
import pojo.Usuario;
import service.AdvogadoParteService;
import service.FaseService;
import service.IntimacaoService;
import service.PessoaService;
import service.ProcessoListViewHelper;
import service.ProcessoService;
import util.LoginSession;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "processoPage")
@RequestScoped
public class ProcessoPage {

    private Processo processoRaw;
    private ProcessoListViewHelper processo;
    private List<Fase> fases = new ArrayList<>();
    private String oabNovoAdvStr;
    private int oabNovoAdv;
    private ProcessoSession processoSess = ProcessoSession.getInstance();
    private LoginSession sess = LoginSession.getInstance();
    private Pessoa pessoa = null;

    // Atributos referentes à deliberação de uma fase pelo juíz
    private String decisao;
    private String justificativa;
    private String vencedor;
    private int oficial;
    private int oficialId;
    private String intimado;
    
    private List<OficialJustica> listaOficial;
    private List<IntimacaoSosifod> listaIntimacao;
    
    private String classeBurra = "form-intimacao-hide";
    private String classeFilhaDaPuta = "form-encerramento-hide";

    // Construtor e PostConstruct
    public ProcessoPage() {
    }

    @PostConstruct
    public void init() {

        // Pega a pessoa da sessão
        if (this.pessoa == null) {
            Usuario usuarioSess = sess.getUsuarioLogado();
            if (usuarioSess != null) {
                this.pessoa = usuarioSess.getPessoa();
            }
        }
        
        this.processoRaw = processoSess.getProcessoSel();
        this.processo = processoSess.getProcessoHelperSel();
        
        
        // Busca Oficiais no WebService
        setListaOficial();
        
        // Atualiza intimacoes no WebService
        setListaIntimacoes();
        updateIntimacoes();

        
        
        this.fases = ProcessoService.fetchFasesDeProcesso(this.processo.getPkId());
                
    }

    // Getters e Setters
    public Processo getProcessoRaw() {
        return processoRaw;
    }

    public void setProcessoRaw(Processo processoRaw) {
        this.processoRaw = processoRaw;
    }

    public ProcessoListViewHelper getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoListViewHelper processo) {
        this.processo = processo;
    }

    public List<Fase> getFases() {
        return fases;
    }

    public void setFases(List<Fase> fases) {
        this.fases = fases;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDecisao() {
        return decisao;
    }

    public void setDecisao(String decisao) {
        this.decisao = decisao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getVencedor() {
        return vencedor;
    }

    public void setVencedor(String vencedor) {
        this.vencedor = vencedor;
    }

    public String getIntimado() {
        return intimado;
    }

    public void setIntimado(String intimado) {
        this.intimado = intimado;
    }

    public String getClasseBurra() {
        return classeBurra;
    }

    public void setClasseBurra(String classeBurra) {
        this.classeBurra = classeBurra;
    }

    public String getClasseFilhaDaPuta() {
        return classeFilhaDaPuta;
    }

    public void setClasseFilhaDaPuta(String classeFilhaDaPuta) {
        this.classeFilhaDaPuta = classeFilhaDaPuta;
    }
    
    

    // Métodos específicos
    /**
     * Método para retornar a classe do fundo do card de uma fase, caso ela
     * esteja em deliberação
     *
     * @param status
     * @return
     */
    public String getBGClass(String status) {
        if (status.equals("DELIBERACAO")) {
            return "fase-deliberacao";
        } else {
            return "fase-ok";
        }
    }

    /**
     * Verifica se o processo selecionado já está encerrado ou não
     *
     * @return
     */
    public boolean checkProcessoEncerrado() {
        return !this.processo.getStatus().equals("ENCERRADO");
    }

    /**
     * Criar uma nova fase para um processo
     *
     * @return URL da tela de criação de fase
     */
    public String criarNovaFase() {
        if (this.fases.size() > 0) {
            int indexUltimaFase = this.fases.size() - 1;
            if (this.fases.get(indexUltimaFase).getStatus().equals("DELIBERACAO")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível criar nova fase. Existe uma fase em deliberação", "Erro nova fase"));
                return "";
            }
            if (this.fases.get(indexUltimaFase).getStatus().equals("INTIMACAO")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível criar nova fase. Aguardando intimação ser efetivada", "Erro nova fase"));
                return "";
            }
        }
        this.processoSess.setProcessoHelperSel(this.processo);
        this.processoSess.setProcessoSel(this.processoRaw);
        return "/advogado/nova-fase.jsf";
    }

    /**
     * Verifica se o Status da fase possui alguma justificativa do juiz para ser
     * mostrada ou não.
     *
     * @param status
     * @return
     */
    public boolean converteDecisaoJuiz(String status) {
        return (status.equals("DELIBERACAO") || status.equals("INFORMATIVO"));
    }

    public String showIntimacao(Fase fase) {
        if (fase.getListOfIntimacao() != null) {
            return (fase.getListOfIntimacao().isEmpty() ? "intimacao-fase-hide" : "");
        }
        return "intimacao-fase-hide";
    }

    /**
     * Retorna uma string informando se o intimado é o promovente ou promovido
     *
     * @param intimado Objeto Parte que representa o intimado
     * @return String indicando se o intimado é o Promovido ou Promovente
     */
    public String getIntimado(Parte intimado) {
        return (intimado.getPkId() == this.processoRaw.getPromovido().getPkId() ? "Promovido" : "Promovente");
    }

    /**
     * Baixar um arquivo PDF anexado a uma fase
     *
     * @param fase Fase que será baixado o arquivo
     */
    public void baixarPDF(Fase fase) {
        byte[] file = fase.getDocumento();
        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream outputStream;
            outputStream = response.getOutputStream();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"anexo.pdf\"");

            // Exportar o relatório para PDF.
//            InputStream inputStream = IOUtils.toInputStream(file.toString());
            InputStream inputStream = new ByteArrayInputStream(file);

            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = inputStream.read(bytesBuffer)) > 0) {
                outputStream.write(bytesBuffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(NovaFase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Verifica se já tem advogado associado e se não tiver direcionar para tela
     * de associação
     *
     * @return URL da tela de associação de advogado
     */
    public String irParaAssociarAdvogado() {
        if (this.processoRaw.getPromovente().getPessoa().getPkCpf().equals(this.pessoa.getPkCpf())) {
            if (this.processoRaw.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Processo já possui advogado associado", "Erro Associar advogado"));
                return "";
            }
        }

        if (this.processoRaw.getPromovido().getPessoa().getPkCpf().equals(this.pessoa.getPkCpf())) {
            if (this.processoRaw.getPromovido().getListOfAdvogadoParte().get(0).getAdvogado() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Processo já possui advogado associado", "Erro Associar advogado"));
                return "";
            }
        }

        this.processoSess.setProcessoSel(this.processoRaw);
        this.processoSess.setProcessoHelperSel(processo);
        String perfil = sess.getPerfilLogado();
        switch (perfil) {
            case "juiz":
                return "/juiz/set-adv.jsf?faces-redirect=true";
            case "advogado":
                return "/advogado/set-adv.jsf?faces-redirect=true";
            default:
                return "/parte/set-adv.jsf?faces-redirect=true";
        }
    }

    public String associarAdvogado() {

        // Verificar se o advogado que foi digitado existe no sistema
        Advogado advogadoNovo = null;
        if (!this.oabNovoAdvStr.equals("")) {
            this.oabNovoAdv = Integer.parseInt(this.oabNovoAdvStr);
            advogadoNovo = PessoaService.getAdvogadoCadastrado(oabNovoAdv);

            // Caso não exista, informa o erro
            if (advogadoNovo == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Advogado não encontrado no sistema", "Erro Novo Processo"));
                return "";
            }
        }

        // Se existir, atribui o objeto advogado à parte promovente ou promovida
        if (this.processoRaw.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado() == null) {
            this.processoRaw.getPromovente().getListOfAdvogadoParte().get(0).setAdvogado(advogadoNovo);

            // Salvar a AdvogadoParte
            AdvogadoParteService.saveOrUpdate(this.processoRaw.getPromovente().getListOfAdvogadoParte().get(0));

        } else {
            this.processoRaw.getPromovido().getListOfAdvogadoParte().get(0).setAdvogado(advogadoNovo);

            // salvar a AdvogadoParte
            AdvogadoParteService.saveOrUpdate(this.processoRaw.getPromovido().getListOfAdvogadoParte().get(0));
        }

        // Salvar o processo
        ProcessoService.saveOrUpdate(this.processoRaw);

        // Fetch do processo
        processoRaw = ProcessoService.getProcessoById(this.processoRaw.getPkId());

        // colocar o novo processo no processoSess
        ProcessoListViewHelper processoView = new ProcessoListViewHelper(this.processoRaw);
        processoSess.setProcessoSel(this.processoRaw);
        processoSess.setProcessoHelperSel(processoView);

        String perfil = sess.getPerfilLogado();
        switch (perfil) {
            case "juiz":
                return "/juiz/processo.jsf?faces-redirect=true";
            case "advogado":
                return "/advogado/processo.jsf?faces-redirect=true";
            default:
                return "/parte/processo.jsf?faces-redirect=true";
        }
    }

    /**
     * Retornar ao processo selecionado
     *
     * @return URL dos detalhes do processo
     */
    public String voltarParaProcessoParte() {
        processoSess.setProcessoHelperSel(this.processo);
        processoSess.setProcessoSel(this.processoRaw);
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
     * Deliberação da fase pelo juiz
     *
     * @return URL para onde ir após a deliberação
     */
    public void deliberarFase() {
        // OBS: Status possíveis para uma fase:
        // PEDIDO ACEITO    -> Não altera status do processo
        // PEDIDO NEGADO    -> Não altera status do processo
        // PEDIDO INTIMACAO -> Não altera status do processo
        // INTIMACAO        -> Não altera status do processo
        // ENCERRADO        -> Processo como ENCERRADO

        int faseIndex = this.fases.size() - 1;

        // Atualizar a fase
        this.fases.get(faseIndex).setDataDecisao(new Date());
        this.fases.get(faseIndex).setJustificativaJuiz(this.justificativa);
        this.fases.get(faseIndex).setStatus(this.decisao);

        // Salvar a fase
        FaseService.saveOrUpdate(this.fases.get(faseIndex));

        // Verificar se for ENCERRADO e atualizar o processo
        if (this.decisao.equals("ENCERRADO")) {
            Processo processoTemp = processoSess.getProcessoSel();
            processoTemp.setStatus(this.decisao);
            if (this.vencedor.equals("PROMOVENTE")) {
                processoTemp.setVencedor(processoTemp.getPromovente());
            } else {
                processoTemp.setVencedor(processoTemp.getPromovido());
            }

            // Salvar o processo
            ProcessoService.saveOrUpdate(processoTemp);

            // Fetch do processo
            processoTemp = ProcessoService.getProcessoById(processoTemp.getPkId());

            this.processo = new ProcessoListViewHelper(processoTemp);
            this.processoRaw = processoTemp;
            this.processoSess.setProcessoHelperSel(processo);
            this.processoSess.setProcessoSel(processoTemp);
        }

        // ATENÇÃO SOSIFOD!!!!!!!! É AQUI QUE GERA A INTIMAÇÃO
        // Verificar se é intimação e gerar
        if (this.decisao.equals("PEDIDO INTIMACAO")) {
            // TODO BRUTAL AQUI: Fazer a parte que envia a intimação para o SOSIFOD

            // Definir texto de título da fase gerada automaticamente
            String titulo = "[SIJOGA] Intimação";

            // Pegar a parte que está sendo intimada
            Parte parteIntimada = null;
            parteIntimada = (this.intimado.equals("PROMOVENTE") ? this.processoRaw.getPromovente() : this.processoRaw.getPromovido());

            // Definir texto da reclamação da fase gerada automaticamente
            String reclamacao = "[Fase gerada automaticamente] Intimação para a parte "
                    + this.intimado + " - CPF: " + parteIntimada.getPessoa().getPkCpf();

            // Pegar a data de abertura
            Date data = new Date();

            // Montar o objeto fase gerado pela intimação
            Fase fase = new Fase();
            fase.setDataAbertura(data);
            fase.setDataDecisao(data);
            fase.setDocumento(null);
            fase.setJustificativaJuiz(this.justificativa);
            fase.setParte(parteIntimada);
            fase.setProcesso(this.processoRaw);
            fase.setReclamacaoParte(reclamacao);
            fase.setStatus("INTIMACAO");
            fase.setTitulo(titulo);

            // Salvar o objeto nova fase
            FaseService.save(fase);
            
            OficialJustica o = getById(Long.valueOf(oficialId));
                                
            // ATENÇÃO SOSIFODE!!! AQUI TÁ MONTANDO O OBJETO INTIMAÇÃO 
            // Montar o objeto intimacao
            Intimacao intimacao = new Intimacao();
            intimacao.setDataAbertura(data);
            intimacao.setFase(fase);
            intimacao.setIntimado(parteIntimada);
            intimacao.setOficialDeJustica(oficialId);
            
            // De alguma forma, enviar a intimação para o SOSIFOD
            // Salvar a intimacao
            IntimacaoService.save(intimacao);
            
            //Oficial Selecionado            
            String nomeOficial = o.getNome();
            String cpfOficial = o.getCPF();
            
            String nomeIntimado = parteIntimada.getPessoa().getNome();
            String cpfIntimado = parteIntimada.getPessoa().getPkCpf();
            
            
            // Criação do Objeto Intimação conforme SOSIFOD
            IntimacaoSosifod intimacaoSosifod = new IntimacaoSosifod();
            intimacaoSosifod.setDataIntimacao(data);
            intimacaoSosifod.setCpfIntimado(cpfIntimado);
            intimacaoSosifod.setCpfOficial(cpfOficial);
            intimacaoSosifod.setDataExecucaoIntimacao(null);
            intimacaoSosifod.setEndereçoIntimado(parteIntimada.getPessoa().getListOfEndereco().get(0).getComplemento());            
            intimacaoSosifod.setId(intimacao.getPkId().longValue());
            intimacaoSosifod.setIsEfetivada(false);
            intimacaoSosifod.setNomeIntimado(nomeIntimado);
            intimacaoSosifod.setNomeOficialAlocado(nomeOficial);
            intimacaoSosifod.setProcesso(String.valueOf(processoRaw.getPkId()));
            
            restUpdate(intimacaoSosifod);
                        
        }

        // Fetch nas fases
        this.fases = ProcessoService.fetchFasesDeProcesso(this.processo.getPkId());
    }

    /**
     * Pega a mudança do menu de tipos de deliberação e define uma classe para a div dos formulários
     * @param e Evento de mudança do menu de tipos de deliberação
     */
    public void alteraFilhaDaPuta(ValueChangeEvent e) {
        String caralho = (String)e.getNewValue();
        if (caralho.equals("PEDIDO INTIMACAO")) {
            this.classeBurra = "form-intimacao-show";
        } else {
            this.classeBurra = "form-intimacao-hide";
        }
        
        if (caralho.equals("ENCERRADO")) {
            this.classeFilhaDaPuta = "form-encerramento-show";
        } else {
            this.classeFilhaDaPuta = "form-encerramento-hide";
        }
        
    }

    public List<OficialJustica> getListaOficial() {
        return listaOficial;
    }

    public void setListaOficial() {
        
        Client client = ClientBuilder.newClient();
        
        try {
            Response r = client
                        .target("http://localhost:8080/webresources/oficial")
                        .request(MediaType.APPLICATION_JSON)
                        .get();

            this.listaOficial = r.readEntity(new GenericType<List<OficialJustica>>(){});          
        }
        catch (Exception e){
            
        }        
        
    }
    

    public List<IntimacaoSosifod> getListaIntimacao() {
        return listaIntimacao;
    }

    public void setListaIntimacao(List<IntimacaoSosifod> listaIntimacao) {
        this.listaIntimacao = listaIntimacao;
    }
    
    public void restUpdate(IntimacaoSosifod intimacaoSosi){
                        
        Client client = ClientBuilder.newClient();            

        try {
        IntimacaoSosifod r = client
                        .target("http://localhost:8080/webresources/intimacao/objeto")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(intimacaoSosi), IntimacaoSosifod.class);        
        }
        catch (Exception e){
            
        }
    }
    
    public OficialJustica getById(Long id){
        
        for (OficialJustica o : listaOficial){
            if(o.getId().equals(id)){
                return o;
            }
        }
        
        return null;
    }

    public int getOficialId() {
        return oficialId;
    }

    public void setOficialId(int oficialId) {
        this.oficialId = oficialId;
    }
    
    
    public void setListaIntimacoes() {

        Client client = ClientBuilder.newClient();

        try {
            Response r = client
                    .target("http://localhost:8080/webresources/intimacao")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            this.listaIntimacao = r.readEntity(new GenericType<List<IntimacaoSosifod>>() {
            });

            // Verifica se a lista é nula ou vazia. Se for só retorna e não faz mais coisa alguma
            if (this.listaIntimacao == null || this.listaIntimacao.size() == 0) {
                return;
            }

            // Se a lista tem elementos, vamos iterar por eles e atualizar as intimações que estão como true
            for (IntimacaoSosifod i : this.listaIntimacao) {
                if (i.isIsEfetivada()) {
                    // Busca a intimação normal do SIJOGA
                    Intimacao intimacao = IntimacaoService.getIntimacaoById(i.getId().intValue());

                    // Se veio uma intimação, vamos atualizar
                    if (intimacao != null) {
                        // Primeiro verifica se a intimação está como nao efetivada
                        if (!intimacao.isIsEfetivada()) {
                            intimacao.setIsEfetivada(true);
                            intimacao.setDataExecucaoIntimacao(i.getDataExecucaoIntimacao());

                            // Salvar a intimacao alterada no banco de dados
                            try {
                                IntimacaoService.saveOrUpdate(intimacao);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                            // Cria uma nova fase para o processo
                            FaseService.criarFaseIntimacaoExecutada(Integer.parseInt(i.getProcesso()));
                            
                            this.fases = ProcessoService.fetchFasesDeProcesso(this.processo.getPkId());

                            
                        }

                    }
                }
            }

        } catch (Exception e) {

        }

    }
    
    
    public void updateIntimacoes(){
        if (this.listaIntimacao == null){
            return;
        }
        for (IntimacaoSosifod i : listaIntimacao){
            
            Client client = ClientBuilder.newClient();
        
            try {
                IntimacaoSosifod r = client
                            .target("http://localhost:8080/webresources/intimacao" + "/" + i.getId())
                            .request(MediaType.APPLICATION_JSON)
                            .get(IntimacaoSosifod.class);
                
                i = r;
            }
            catch (Exception e){

            }
        }
       
    }
    
    
    

}
