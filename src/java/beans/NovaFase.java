/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import pojo.Advogado;
import pojo.Fase;
import pojo.Processo;
import service.FaseService;
import service.ProcessoListViewHelper;
import util.LoginSession;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "novaFase")
@RequestScoped
public class NovaFase implements Serializable {

    private ProcessoSession processoSess = ProcessoSession.getInstance();
    private LoginSession sess = LoginSession.getInstance();

    private Processo processoSel;
    private ProcessoListViewHelper processo;
    private Advogado advogado;
    private String tipo;
    private String reclamacao;
    private String titulo;
    private Part file = null;

    public NovaFase() {
    }

    @PostConstruct
    public void init() {
        Processo processoTemp = processoSess.getProcessoSel();
        ProcessoListViewHelper processoHelperTemp = processoSess.getProcessoHelperSel();
        if (processoTemp != null) {
            this.processoSel = processoTemp;
        }
        if (processoHelperTemp != null) {
            this.processo = processoHelperTemp;
        }
        this.advogado = sess.getUsuarioLogado().getPessoa().getListOfAdvogado().get(0);
    }

    // Getters e Setters
    public Processo getProcessoSel() {
        return processoSel;
    }

    public void setProcessoSel(Processo processoSel) {
        this.processoSel = processoSel;
    }

    public ProcessoListViewHelper getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoListViewHelper processo) {
        this.processo = processo;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(String reclamacao) {
        this.reclamacao = reclamacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String salvarNovaFase() throws FileNotFoundException, IOException {
        Fase novaFase = new Fase();
        if (file != null) {
            String contentType = file.getContentType();
            String ext = file.getSubmittedFileName().substring(file.getSubmittedFileName().length() - 3).toLowerCase();
            if (!contentType.equals("application/pdf") && !ext.equals("pdf")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de arquivo inválido. Só é permitido pdf", "Erro nova fase"));
                return "";
            }
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            novaFase.setDocumento(bytes);
        } else {
            novaFase.setDocumento(null);
        }

        // Monta o objeto Fase
        Date data = new Date();
        novaFase.setTitulo(this.titulo);
        novaFase.setReclamacaoParte(this.reclamacao);
        novaFase.setJustificativaJuiz((this.tipo.equals("INFORMATIVA") ? "N/A" : null));
        novaFase.setStatus(this.tipo);
        novaFase.setDataAbertura(data);
        novaFase.setDataDecisao((this.tipo.equals("INFORMATIVA") ? data : null));
        novaFase.setParte(this.advogado.getListOfAdvogadoParte().get(0).getParte());
        novaFase.setProcesso(this.processoSel);

        // Salva a fase
        FaseService.save(novaFase);

        return "/advogado/processo.jsf?faces-redirect=true";
    }
    
    /**
     * Voltar para os detalhes do processo
     * @return URL para a página do processo
     */
    public String voltarParaProcesso() {
        this.processoSess.setProcessoHelperSel(this.processo);
        this.processoSess.setProcessoSel(this.processoSel);
        return "/advogado/processo.jsf?faces-redirect=true";
    }
}
