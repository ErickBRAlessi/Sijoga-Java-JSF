package service;

import java.util.Date;
import java.util.List;
import pojo.Fase;
import pojo.Processo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Erick
 */
public class ProcessoListViewHelper {

    public ProcessoListViewHelper(Processo processo) {
        this.pkId = processo.getPkId();
        this.dataAbertura = processo.getDataAbertura();
        this.listOfFase = processo.getListOfFase();
        this.juiz = processo.getJuiz().getPessoa().getNome() + " " + processo.getJuiz().getPessoa().getSobrenome();
        this.promovente = (processo.getPromovente().getPessoa().getNome() + " " + processo.getPromovente().getPessoa().getSobrenome());
        this.promovido = (processo.getPromovido().getPessoa().getNome() + " " + processo.getPromovido().getPessoa().getSobrenome());
        if (processo.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado() != null) {
            this.advPromovente = (processo.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado().getPessoa().getNome() + " " + processo.getPromovente().getListOfAdvogadoParte().get(0).getAdvogado().getPessoa().getSobrenome());
        } else {
            this.advPromovente = "NÃO INFORMADO";
        }
        if (processo.getPromovido().getListOfAdvogadoParte().get(0).getAdvogado() != null) {
            this.advPromovido = (processo.getPromovido().getListOfAdvogadoParte().get(0).getAdvogado().getPessoa().getNome() + " " + processo.getPromovido().getListOfAdvogadoParte().get(0).getAdvogado().getPessoa().getSobrenome());
        } else {
            this.advPromovido = "NÃO INFORMADO";
        }
        
        this.vencedor = (processo.getVencedor() == null ? "N/A" : (processo.getVencedor().getPkId() == processo.getPromovido().getPkId() ? "PROMOVIDO" : "PROMOVENTE"));
        this.status = processo.getStatus();
    }

    private Integer pkId;

    private Date dataAbertura;

    private String status;

    private String promovente;
    
    private String advPromovente;

    private String promovido;
    
    private String advPromovido;

    private String vencedor;

    private String juiz;

    private List<Fase> listOfFase;

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPromovente() {
        return promovente;
    }

    public void setPromovente(String promovente) {
        this.promovente = promovente;
    }

    public String getAdvPromovente() {
        return advPromovente;
    }

    public void setAdvPromovente(String advPromovente) {
        this.advPromovente = advPromovente;
    }

    public String getPromovido() {
        return promovido;
    }

    public void setPromovido(String promovido) {
        this.promovido = promovido;
    }

    public String getAdvPromovido() {
        return advPromovido;
    }

    public void setAdvPromovido(String advPromovido) {
        this.advPromovido = advPromovido;
    }

    public String getVencedor() {
        return vencedor;
    }

    public void setVencedor(String vencedor) {
        this.vencedor = vencedor;
    }

    public String getJuiz() {
        return juiz;
    }

    public void setJuiz(String juiz) {
        this.juiz = juiz;
    }
    

    public List<Fase> getListOfFase() {
        return listOfFase;
    }

    public void setListOfFase(List<Fase> listOfFase) {
        this.listOfFase = listOfFase;
    }

    @Override
    public String toString() {
        return "DummieProcesso{" + "pkId=" + pkId + ", dataAbertura=" + dataAbertura + ", status=" + status + ", promovente=" + promovente + ", advPromovente=" + advPromovente + ", promovido=" + promovido + ", advPromovido=" + advPromovido + ", vencedor=" + vencedor + ", juiz=" + juiz + ", listOfFase=" + listOfFase + '}';
    }

    
    
    
}
