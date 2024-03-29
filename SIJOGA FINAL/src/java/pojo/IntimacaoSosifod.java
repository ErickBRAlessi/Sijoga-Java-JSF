/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.Date;
/**
 *
 * @author Bruno Fernandes
 */
public class IntimacaoSosifod {

    private Date dataIntimacao;
    private String cpfIntimado;
    private String nomeIntimado;
    private String endereçoIntimado;
    private Date dataExecucaoIntimacao;
    private boolean isEfetivada;
    private String nomeOficialAlocado;
    private String processo;
    private String cpfOficial;
    private Long Id;

    public IntimacaoSosifod(Date dataIntimacao, String cpfIntimado, String nomeIntimado, String endereçoIntimado, Date dataExecucaoIntimacao, boolean isEfetivada, String nomeOficialAlocado, String processo, String cpfOficial, Long idIntimacao) {
        this.dataIntimacao = dataIntimacao;
        this.cpfIntimado = cpfIntimado;
        this.nomeIntimado = nomeIntimado;
        this.endereçoIntimado = endereçoIntimado;
        this.dataExecucaoIntimacao = dataExecucaoIntimacao;
        this.isEfetivada = isEfetivada;
        this.nomeOficialAlocado = nomeOficialAlocado;
        this.processo = processo;
        this.cpfOficial = cpfOficial;
        this.Id = idIntimacao;
    }

    public IntimacaoSosifod(Date dataIntimacao, String cpfIntimado, String nomeIntimado, String endereçoIntimado, Date dataExecucaoIntimacao, boolean isEfetivada, String nomeOficialAlocado, String processo, String cpfOficial) {
        this.dataIntimacao = dataIntimacao;
        this.cpfIntimado = cpfIntimado;
        this.nomeIntimado = nomeIntimado;
        this.endereçoIntimado = endereçoIntimado;
        this.dataExecucaoIntimacao = dataExecucaoIntimacao;
        this.isEfetivada = isEfetivada;
        this.nomeOficialAlocado = nomeOficialAlocado;
        this.processo = processo;
        this.cpfOficial = cpfOficial;
    }

    public IntimacaoSosifod() {
    }

    public Date getDataIntimacao() {
        return dataIntimacao;
    }

    public void setDataIntimacao(Date dataIntimacao) {
        this.dataIntimacao = dataIntimacao;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
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
