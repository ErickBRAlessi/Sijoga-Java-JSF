package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pojo.Pessoa;
import pojo.Usuario;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }
//

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Pega a URL que está tentando ser acessada
        String url = req.getRequestURI();

        // Verifica se é conteúdo estático pois pode ser o bootstrap
        boolean isStaticResource = req.getRequestURI().contains("/javax.faces.resource/");

        String urlDestino = "";

        LoginSession sess = LoginSession.getInstance();

        Usuario usuario = null;
        String perfil = null;

        if (sess != null) {
            usuario = sess.getUsuarioLogado();
            perfil = sess.getPerfilLogado();
        }

        if (usuario == null) {

            // Se usuário não está logado, confere se está tentando acessar alguma rota fora o login ou register
            if (url.indexOf("login.jsf") >= 0 || isStaticResource || url.indexOf("register.jsf") >= 0) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getServletContext().getContextPath() + "/login.jsf");
            }

            // Se tem uma pessoa logada
        } else {
            if (url.indexOf("login.jsf") >= 0 || url.indexOf("register.jsf") >= 0) {
                urlDestino = checkPerfil(perfil);
                res.sendRedirect(req.getServletContext().getContextPath() + urlDestino);
            } else if (url.indexOf("juiz") >= 0) {
                if (!perfil.equals("juiz")) {
                    urlDestino = checkPerfil(perfil);
                    res.sendRedirect(req.getServletContext().getContextPath() + urlDestino);
                } else {
                    chain.doFilter(request, response);
                }
            } else if (url.indexOf("advogado") >= 0) {
                if (!perfil.equals("advogado")) {
                    urlDestino = checkPerfil(perfil);
                    res.sendRedirect(req.getServletContext().getContextPath() + urlDestino);
                } else {
                    chain.doFilter(request, response);
                }
            } else if (url.indexOf("parte") >= 0) {
                if (!perfil.equals("parte")) {
                    urlDestino = checkPerfil(perfil);
                    res.sendRedirect(req.getServletContext().getContextPath() + urlDestino);
                } else {
                    chain.doFilter(request, response);
                }
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    private String checkPerfil(String perfil) {
        String url = "/index.jsf";
        switch (perfil) {
            case "juiz":
                url = "/juiz.jsf";
                break;
            case "advogado":
                url = "/advogado.jsf";
                break;
            case "parte":
                url = "/parte.jsf";
                break;
            default:
                url = "/index.jsf";
        }
        return url;
    }
}
