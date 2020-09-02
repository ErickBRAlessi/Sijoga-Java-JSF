package beans;

import facades.PessoaFacade;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import pojo.Advogado;
import pojo.Usuario;
import service.UsuarioService;
import util.Ferramentas;
import util.LoginSession;
import util.MD5;
import util.ProcessoSession;
import util.SessionContext;

/**
 *
 * @author cassiano
 */
@Named(value = "loginMB")
@RequestScoped
public class LoginMB implements Serializable {

    private LoginSession sess = LoginSession.getInstance();
    private ProcessoSession processoSess = ProcessoSession.getInstance();
    private String email;
    private String senha;

    public LoginMB() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String doLogin() throws NoSuchAlgorithmException {
        this.senha = MD5.toMD5(this.senha);
        String perfil = "parte";
        Usuario usuario = UsuarioService.canLogin(this.email, this.senha);
        if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha ou email incorretos", "Erro login"));
            return "";
        }
        usuario = UsuarioService.buscaUsuarioPorEmail(email);

        if (usuario.getPessoa().getListOfAdvogado().size() != 0) {
            perfil = "advogado";
        }
        if (usuario.getPessoa().getListOfJuiz().size() != 0) {
            perfil = "juiz";
        }

        sess.iniciarSessao(usuario, perfil);
        String urlDestino = "";
        switch (perfil) {
            case "juiz":
                urlDestino = "/juiz.jsf?faces-redirect=true";
                break;
            case "advogado":
                urlDestino = "/advogado.jsf?faces-redirect=true";
                break;
            case "parte":
                urlDestino = "/parte.jsf?faces-redirect=true";
                break;
            default:
                urlDestino = "/index.jsf?faces-redirect=true";
        }
        return urlDestino;
    }

    public String doLogout() {
        sess.encerrarSessao();
        processoSess.encerrarSessao();
        return "/login.jsf?faces-redirect=true";
    }

    public String novoRegistro() {
        return "/register.jsf";
    }
}
