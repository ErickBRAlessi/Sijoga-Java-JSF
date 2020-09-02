/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import pojo.Usuario;

/**
 *
 * @author cassiano
 */
@Named(value = "loginSession")
@SessionScoped
public class LoginSession implements Serializable {

    private static LoginSession instance;
    private Usuario usuarioLogado = null;
    private String perfilLogado = null;

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }

        return instance;
    }

    public LoginSession() {
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public String getPerfilLogado() {
        return perfilLogado;
    }

    public void setPerfilLogado(String perfilLogado) {
        this.perfilLogado = perfilLogado;
    }
    
    public void iniciarSessao(Usuario usuario, String perfil) {
        this.setUsuarioLogado(usuario);
        this.setPerfilLogado(perfil);
    }
    
    public void encerrarSessao() {
        this.setUsuarioLogado(null);
        this.setPerfilLogado(null);
    }
}
