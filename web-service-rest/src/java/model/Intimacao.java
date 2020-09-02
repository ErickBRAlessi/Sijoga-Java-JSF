package model;

import java.util.Date;

public class Intimacao {
    private Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }
    private Date dataIntimacao;
    private String cpfIntimado;
    private String nomeIntimado;
    private String endereçoIntimado;
    private Date dataExecucaoIntimacao;
    private boolean isEfetivada;
    private String nomeOficialAlocado;
    private String processo;
    private String cpfOficial;    
    
    public Date getDataIntimacao() {
        return dataIntimacao;
    }

    public Intimacao() {
    }

    public String getCpfOficial() {
        return cpfOficial;
    }

    public void setCpfOficial(String cpfOficial) {
        this.cpfOficial = cpfOficial;
    }

    public String getNomeOficialAlocado() {
        return nomeOficialAlocado;
    }

    public void setNomeOficialAlocado(String nomeOficialAlocado) {
        this.nomeOficialAlocado = nomeOficialAlocado;
    }
    
    public void setDataIntimacao(Date dataIntimacao) {
        this.dataIntimacao = dataIntimacao;
    }

    public String getCpfIntimado() {
        return cpfIntimado;
    }

    public void setCpfIntimado(String cpfIntimado) {
        this.cpfIntimado = cpfIntimado;
    }

    public String getNomeIntimado() {
        return nomeIntimado;
    }

    public void setNomeIntimado(String nomeIntimado) {
        this.nomeIntimado = nomeIntimado;
    }

    public String getEndereçoIntimado() {
        return endereçoIntimado;
    }

    public void setEndereçoIntimado(String endereçoIntimado) {
        this.endereçoIntimado = endereçoIntimado;
    }

    public Date getDataExecucaoIntimacao() {
        return dataExecucaoIntimacao;
    }

    public void setDataExecucaoIntimacao(Date dataExecucaoIntimacao) {
        this.dataExecucaoIntimacao = dataExecucaoIntimacao;
    }

    public boolean isIsEfetivada() {
        return isEfetivada;
    }

    public void setIsEfetivada(boolean isEfetivada) {
        this.isEfetivada = isEfetivada;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

}
