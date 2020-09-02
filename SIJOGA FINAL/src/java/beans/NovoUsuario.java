/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import facades.PessoaFacade;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import pojo.Advogado;
import pojo.Endereco;
import pojo.Juiz;
import pojo.Pessoa;
import pojo.Usuario;
import service.AdvogadoService;
import service.EnderecoService;
import service.JuizService;
import service.PessoaService;
import service.UsuarioService;
import util.Ferramentas;
import util.LoginSession;
import util.MD5;
import util.ProcessoSession;

/**
 *
 * @author cassiano
 */
@Named(value = "novoUsuario")
@RequestScoped
public class NovoUsuario {

    private String nome;
    private String sobrenome;
    private String cpf;
    private Date nasc;
    private String complemento;
    private String cepStr;
    private int cep;
    private String senha;
    private String email;
    private String tipoUsuario;
    private String oabStr;
    private int oab;

    private boolean showCampos;
    private boolean isEdit;
    private boolean mudarPerfil;

    private String classeForm = "form-novo-usuario-hide";
    private String classeOab = "form-oab-hide";

    private final Ferramentas ferramentas = Ferramentas.getInstance();
    private LoginSession sess = LoginSession.getInstance();

    public NovoUsuario() {
    }

    @PostConstruct
    public void init() {
        Usuario usuario = sess.getUsuarioLogado();

        if (usuario != null) {
            // Usuário está editando o perfil, então carrega as infos
            this.nome = usuario.getPessoa().getNome();
            this.sobrenome = usuario.getPessoa().getSobrenome();
            this.nasc = usuario.getPessoa().getNascimento();
            this.cpf = usuario.getPessoa().getPkCpf();
            this.email = usuario.getEmail();
            this.senha = usuario.getSenha();
            this.cep = usuario.getPessoa().getListOfEndereco().get(0).getCep();
            this.cepStr = String.valueOf(this.cep);
            this.complemento = usuario.getPessoa().getListOfEndereco().get(0).getComplemento();
            this.tipoUsuario = sess.getPerfilLogado();
            if (tipoUsuario.equals("advogado")) {
                this.oab = usuario.getPessoa().getListOfAdvogado().get(0).getPkOab();
                this.oabStr = String.valueOf(this.oab);
            }
            if (tipoUsuario.equals("juiz")) {
                this.oab = usuario.getPessoa().getListOfJuiz().get(0).getPkOab();
                this.oabStr = String.valueOf(this.oab);
            }
            this.isEdit = true;
        }
    }

    // Getters e Setters
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

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCepStr() {
        return cepStr;
    }

    public void setCepStr(String cepStr) {
        this.cepStr = cepStr;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getOabStr() {
        return oabStr;
    }

    public void setOabStr(String oabStr) {
        this.oabStr = oabStr;
    }

    public int getOab() {
        return oab;
    }

    public void setOab(int oab) {
        this.oab = oab;
    }

    public boolean isShowCampos() {
        return showCampos;
    }

    public void setShowCampos(boolean showCampos) {
        this.showCampos = showCampos;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean isMudarPerfil() {
        return mudarPerfil;
    }

    public void setMudarPerfil(boolean mudarPerfil) {
        this.mudarPerfil = mudarPerfil;
    }

    public String getClasseForm() {
        return classeForm;
    }

    public void setClasseForm(String classeForm) {
        this.classeForm = classeForm;
    }

    public String getClasseOab() {
        return classeOab;
    }

    public void setClasseOab(String classeOab) {
        this.classeOab = classeOab;
    }

    // Métodos específicos
    /**
     * Salvar usuário
     *
     * @return URL da mesma página, porém informando se pessoa foi encontrada
     */
    public String salvarAlteracoes() throws NoSuchAlgorithmException {
        // Limpar o cpf e cep
        if (this.cpf != null && !this.cpf.equals("")) {
            this.cpf = ferramentas.removerCaracteresCpf(this.cpf);
        }
        this.cepStr = ferramentas.removerCaracteresCep(this.cepStr);

        // Hash da senha
        this.senha = MD5.toMD5(this.senha);

        // Converte o cep para int
        this.cep = Integer.parseInt(this.cepStr);

        // Checar se tem usuário na sessao para marcar isEdit como true se for o cas
        Usuario usuarioSess = sess.getUsuarioLogado();

        if (usuarioSess != null) {
            this.isEdit = true;
        }

        // Verifica se é só edição do usuário que já está conectado
        if (this.isEdit) {
            // Buscar Pessoa no banco de dados
            Pessoa pessoaTemp = PessoaService.getPessoaCadastrada(usuarioSess.getPkCpf());

            // Se existir, altera tudo, menos CPF, e OAB (se existir)
            if (pessoaTemp != null) {
                pessoaTemp.setNome(this.nome);
                pessoaTemp.setSobrenome(this.sobrenome);
                pessoaTemp.setNascimento(this.nasc);
                pessoaTemp.getListOfEndereco().get(0).setCep(this.cep);
                pessoaTemp.getListOfEndereco().get(0).setComplemento(this.complemento);

                // Verificar se a pessoa tem perfil
                if (pessoaTemp.getListOfUsuario() != null) {
                    pessoaTemp.getListOfUsuario().get(0).setEmail(this.email);
                    pessoaTemp.getListOfUsuario().get(0).setSenha(this.senha);

                }

                // Salvar pessoa
                try {
                    PessoaService.saveOrUpdate(pessoaTemp);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar pessoa no banco de dados", "Erro Novo Processo"));
                    return "";
                }

                // Salvar usuário
                try {
                    UsuarioService.saveOrUpdate(pessoaTemp.getListOfUsuario().get(0));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar usuário no banco de dados", "Erro Novo Processo"));
                    return "";
                }

                // Salvar endereço
                try {
                    EnderecoService.saveOrUpdate(pessoaTemp.getListOfEndereco().get(0));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar endereço no banco de dados", "Erro Novo Processo"));
                    return "";
                }

            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com sucesso", "Erro Novo Processo"));
            // Retornar para a tela do perfil, caso esteja logado
            if (usuarioSess != null) {
                sess.setUsuarioLogado(usuarioSess);
                String perfilLogado = sess.getPerfilLogado();
                switch (perfilLogado) {
                    case "juiz":
                        return "/juiz.jsf?faces-redirect=true";
                    case "advogado":
                        return "/advogado.jsf?faces-redirect=true";
                    case "parte":
                        return "/parte.jsf?faces-redirect=true";
                }
            }

            // Retornar para a tela de login, caso não esteja logado
            return "/login.jsf?faces-redirect=true";
        }

        // Checar se tipo de perfil é advogado ou juiz e se foi digitado OAB
        if (this.tipoUsuario.equals("advogado") || this.tipoUsuario.equals("juiz")) {
            if (this.oabStr == null || this.oabStr.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Para Advogado ou Juiz o OAB é obrigatório", "Erro Novo Processo"));
                return "";
            }
        }

        // Buscar Pessoa no banco de dados
        Pessoa pessoaTemp = PessoaService.getPessoaCadastrada(this.cpf);

        // Verificar se Advogado ou Juiz já existe para uma pesosa que não existe
        if (pessoaTemp == null && (this.oabStr != null && !this.oabStr.equals(""))) {

            Juiz juizCheck = PessoaService.getJuizCadastrado(Integer.parseInt(this.oabStr));
            if (juizCheck != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OAB Já cadastrado no sistema. Tente outro", "Erro Novo Processo"));
                return "";
            }
            Advogado advogadoCheck = PessoaService.getAdvogadoCadastrado(Integer.parseInt(this.oabStr));
            if (advogadoCheck != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OAB Já cadastrado no sistema. Tente outro", "Erro Novo Processo"));
                return "";
            }
        }

        // Se existir, altera tudo, menos CPF, e OAB (se existir)
        if (pessoaTemp != null) {
            pessoaTemp.setNome(this.nome);
            pessoaTemp.setSobrenome(this.sobrenome);
            pessoaTemp.setNascimento(this.nasc);
            pessoaTemp.getListOfEndereco().get(0).setCep(this.cep);
            pessoaTemp.getListOfEndereco().get(0).setComplemento(this.complemento);

            // Verificar se a pessoa tem perfil
            if (pessoaTemp.getListOfUsuario() != null) {
                pessoaTemp.getListOfUsuario().get(0).setEmail(this.email);
                pessoaTemp.getListOfUsuario().get(0).setSenha(this.senha);

            }

            // Salvar pessoa
            try {
                PessoaService.saveOrUpdate(pessoaTemp);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar pessoa no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Salvar usuário
            try {
                UsuarioService.saveOrUpdate(pessoaTemp.getListOfUsuario().get(0));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar usuário no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Salvar endereço
            try {
                EnderecoService.saveOrUpdate(pessoaTemp.getListOfEndereco().get(0));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar endereço no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Se não tem usuário criar
            if (pessoaTemp.getListOfUsuario() == null) {
                Usuario usuarioNovo = new Usuario();
                usuarioNovo.setEmail(this.email);
                usuarioNovo.setSenha(senha);
                usuarioNovo.setPkCpf(this.cpf);
                usuarioNovo.setPessoa(pessoaTemp);

                // Salvar usuário
                try {
                    UsuarioService.save(usuarioNovo);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao atualizar usuário no banco de dados", "Erro Novo Processo"));
                    return "";
                }
            }

        } else {
            // Se não existir a pessoa, cria do zero
            pessoaTemp = new Pessoa();
            pessoaTemp.setNome(this.nome);
            pessoaTemp.setSobrenome(this.sobrenome);
            pessoaTemp.setNascimento(this.nasc);
            pessoaTemp.setPkCpf(this.cpf);

            // Salvar pessoa
            try {
                PessoaService.save(pessoaTemp);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar pessoa no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Cria objeto usuario
            Usuario usuarioNovo = new Usuario();
            usuarioNovo.setEmail(this.email);
            usuarioNovo.setSenha(this.senha);
            usuarioNovo.setPkCpf(this.cpf);
            usuarioNovo.setPessoa(pessoaTemp);

            // Salvar usuário
            try {
                UsuarioService.save(usuarioNovo);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar usuário no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Cria objeto endereço
            Endereco enderecoNovo = new Endereco();
            enderecoNovo.setCep(this.cep);
            enderecoNovo.setComplemento(this.complemento);
            enderecoNovo.setPkCpf(this.cpf);

            // Salvar endereço
            try {
                EnderecoService.save(enderecoNovo);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar endereço no banco de dados", "Erro Novo Processo"));
                return "";
            }

            // Verifica se é advogado ou juiz e cria objeto de acordo
            if (this.tipoUsuario.equals("advogado")) {
                Advogado advogadoNovo = new Advogado();
                advogadoNovo.setPessoa(pessoaTemp);
                advogadoNovo.setPkOab(Integer.parseInt(this.oabStr));

                // Salva advogado
                try {
                    AdvogadoService.save(advogadoNovo);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar advogado no banco de dados", "Erro Novo Processo"));
                    return "";
                }
            }
            if (this.tipoUsuario.equals("juiz")) {
                Juiz juizNovo = new Juiz();
                juizNovo.setPessoa(pessoaTemp);
                juizNovo.setPkOab(Integer.parseInt(this.oabStr));

                // Salva juiz
                try {
                    JuizService.save(juizNovo);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao salvar advogado no banco de dados", "Erro Novo Processo"));
                    return "";
                }
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com sucesso", "Erro Novo Processo"));
        // Retornar para a tela do perfil, caso esteja logado
        Usuario usuarioTemp = sess.getUsuarioLogado();
        if (usuarioTemp != null) {
            String perfilLogado = sess.getPerfilLogado();
            switch (perfilLogado) {
                case "juiz":
                    return "/juiz.jsf?faces-redirect=true";
                case "advogado":
                    return "/advogado.jsf?faces-redirect=true";
                case "parte":
                    return "/parte.jsf?faces-redirect=true";
            }
        }

        // Retornar para a tela de login, caso não esteja logado
        return "/login.jsf?faces-redirect=true";
    }

    /**
     * Retornar para tela inicial verificando se o usuário está logado ou se tá
     * criando um novo cadastro
     *
     * @return URL para retorno
     */
    public String voltarParaTelaInicial() {
        Usuario usuarioTemp = sess.getUsuarioLogado();
        if (usuarioTemp != null) {
            String perfilLogado = sess.getPerfilLogado();
            switch (perfilLogado) {
                case "juiz":
                    return "/juiz.jsf?faces-redirect=true";
                case "advogado":
                    return "/advogado.jsf?faces-redirect=true";
                case "parte":
                    return "/parte.jsf?faces-redirect=true";
            }
        }

        // Retornar para a tela de login, caso não esteja logado
        return "/login.jsf?faces-redirect=true";
    }

}
