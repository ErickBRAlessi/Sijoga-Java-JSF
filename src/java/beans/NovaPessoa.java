/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Endereco;
import pojo.Pessoa;
import service.EnderecoService;
import service.PessoaService;
import util.Ferramentas;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "novaPessoa")
@RequestScoped
public class NovaPessoa {
    
    private String nome;
    private String sobrenome;
    private String cpf;
    private Date nasc;
    private Endereco endereco = new Endereco();
    private String cepStr;
    
    private final Ferramentas ferramentas = Ferramentas.getInstance();
    private final ProcessoSession processoSess = ProcessoSession.getInstance();

    // Getters e Setters
    public NovaPessoa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getNasc() {
        return nasc;
    }

    public void setNasc(Date nasc) {
        this.nasc = nasc;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCepStr() {
        return cepStr;
    }

    public void setCepStr(String cepStr) {
        this.cepStr = cepStr;
    }
    
    
    // Métodos específicos

    /**
     * Salva uma nova pessoa no banco de dados e retorna para a página de criação de processo
     * @return URL da página de criação de processo
     */
    public String salvarNovaPessoa() {
        
        Pessoa novaPessoa = null;
        
        // Limpar caracteres especiais do CPF
        this.cpf = ferramentas.removerCaracteresCpf(this.cpf);
        
        // Verificar se o CPF já não está cadastrado no sistema
        novaPessoa = PessoaService.getPessoaCadastrada(cpf);
        if (novaPessoa != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF já cadastrado no sistema", "Erro Nova pessoa"));
            return "";
        }

        // Monta o objeto nova pessoa
        novaPessoa = new Pessoa();
        novaPessoa.setNome(this.nome);
        novaPessoa.setSobrenome(this.sobrenome);
        novaPessoa.setPkCpf(this.cpf);
        novaPessoa.setNascimento(this.nasc);
        
        // Salva o objeto pessoa
        PessoaService.save(novaPessoa);
        
        // Monta o objeto endereco
        // Remove caracteres especiais do CEP e atribui à endereco
        this.cepStr = ferramentas.removerCaracteresCep(this.cepStr);
        this.endereco.setCep(Integer.parseInt(this.cepStr));
        this.endereco.setPkCpf(novaPessoa.getPkCpf());
        this.endereco.setPessoa(novaPessoa);
        
        // Salva endereco no banco de dados
        EnderecoService.save(this.endereco);
        
        return "/advogado/novo-processo.jsf";
    }
    
}
