/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pojo.Endereco;
import pojo.Pessoa;
import pojo.Usuario;
import service.EnderecoService;
import service.PessoaService;
import service.UsuarioService;
import util.MD5;

/**
 *
 * @author cassiano
 */
public class PessoaFacade {

    public static void inserirUsuario(String nome, String sobreNome, String cpf, Date nascimento, int cep, String complementoEndereco, String email, String senhaSemMD5, boolean admin) throws NoSuchAlgorithmException {
        Pessoa p = new Pessoa();
        p.setNome(nome);
        p.setSobrenome(sobreNome);
        p.setPkCpf(cpf);
        p.setNascimento(nascimento);
        PessoaService.saveOrUpdate(p);
       
        Endereco e = new Endereco();
        e.setPkCpf(cpf);
        e.setCep(cep);
        e.setComplemento(complementoEndereco);
        e.setPessoa(p);
        EnderecoService.saveOrUpdate(e);
       
        Usuario user = new Usuario();
        user.setPkCpf(cpf);
        user.setEmail(email);
        user.setSenha(MD5.toMD5(senhaSemMD5));
        user.setAdmin(Boolean.TRUE);
        user.setPessoa(p);
        UsuarioService.saveOrUpdate(user);

    }

    //trás prenchido a lista de usuário e de endereço
    public static Pessoa findByEmail(String email) {
        return PessoaService.findByEmail(email.trim());
    }
    
    //autentica baseado no email e senha
    public static boolean canLogin(String email, String senha) {
        List<Usuario> usuarios = new ArrayList<>();
        boolean found = false;
        usuarios = UsuarioService.findAll();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                found = true;
                break;
            }
        }
        return found;
    }

    //apenas para testes.. não faz sentido pessoas serem deletadas de um app judicial
    public static void deletePessoaByCpf(String cpf) {
        EnderecoService.deleteByCPF(cpf);
        UsuarioService.deleteByCPF(cpf);
        PessoaService.deleteByCPF(cpf);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//============================== COISAS VELHAS ===================================//
//    // TODO: Ajustar método para conversar com banco de dados
//    public Pessoa findByUsuario(Usuario usuario) {
//        Pessoa pessoaEncontrada = null;
//
//        for (Pessoa p : pessoaDummy.getPessoas()) {
//            if (p.getUsuario().getEmail().equals(usuario.getEmail()) && p.getUsuario().getSenha().equals(usuario.getSenha())) {
//                pessoaEncontrada = p;
//                break;
//            }
//        }
//        return pessoaEncontrada;
//    }
//
//    public String findTipoPerfil(String cpf) {
//        String perfil = "";
//        for (Pessoa p : perfilDummy.getJuizes()) {
//            if (p.getCpf().equals(cpf)) {
//                perfil = "juiz";
//                break;
//            }
//        }
//        if (perfil.equals("")) {
//            for (Pessoa p : perfilDummy.getAdvogados()) {
//                if (p.getCpf().equals(cpf)) {
//                    perfil = "advogado";
//                    break;
//                }
//            }
//        }
//        if (perfil.equals("")) {
//            for (Pessoa p : perfilDummy.getPartes()) {
//                if (p.getCpf().equals(cpf)) {
//                    perfil = "parte";
//                    break;
//                }
//            }
//        }
//        if (perfil.equals("")) {
//            perfil = "parte";
//        }
//        return perfil;
//    }
//
//    public Pessoa getPerfilEspecifico(String cpf, String perfil) {
//        Pessoa pessoa = null;
//        switch (perfil) {
//            case "juiz":
//                for (Pessoa p : perfilDummy.getJuizes()) {
//                    if (p.getCpf().equals(cpf)) {
//                        pessoa = p;
//                        break;
//                    }
//                }
//                break;
//            case "advogado":
//                for (Pessoa p : perfilDummy.getAdvogados()) {
//                    if (p.getCpf().equals(cpf)) {
//                        pessoa = p;
//                        break;
//                    }
//                }
//                break;
//            case "parte":
//                for (Pessoa p : perfilDummy.getPartes()) {
//                    if (p.getCpf().equals(cpf)) {
//                        pessoa = p;
//                        break;
//                    }
//                }
//                break;
//            default:
//                pessoa = null;
//        }
//        return pessoa;
//    }
//    
//    public Pessoa getPessoaByCpf(String cpf) {
//        Pessoa pessoa = null;
//        for (Pessoa p : pessoaDummy.getPessoas()) {
//            if (p.getCpf().equals(cpf)) {
//                pessoa = p;
//            }
//        }
//        return pessoa;
//    }
//    
//    public Advogado getAdvogadoByOab(int oab) {
//        Advogado advogado = null;
//        for (Advogado a : perfilDummy.getAdvogados()) {
//            if (a.getOab() == oab) {
//                advogado = a;
//            }
//        }
//        return advogado;
//    }
//    
//    public Juiz getJuizByOab(int oab) {
//        Juiz juiz = null;
//        for (Juiz j : perfilDummy.getJuizes()) {
//            if (j.getOab() == oab) {
//                juiz = j;
//            }
//        }
//        return juiz;
//    }
//    
//    public void adicionarPessoa(Pessoa pessoa) {
//        this.pessoaDummy.getPessoas().add(pessoa);
//    }
//    
//    public void atualizarPessoa(Pessoa pessoa) {
//        int index = -1;
//        for (int i = 0; i < this.pessoaDummy.getPessoas().size(); i++) {
//            if (this.pessoaDummy.getPessoas().get(i).getCpf().equals(pessoa.getCpf())) {
//                index = i;
//                break;
//            }
//        }
//        if (index == -1) {
//            return;
//        }
//        this.pessoaDummy.getPessoas().get(index).setNome(pessoa.getNome());
//        this.pessoaDummy.getPessoas().get(index).setCpf(pessoa.getCpf());
//        this.pessoaDummy.getPessoas().get(index).setNascimento(pessoa.getNascimento());
//        this.pessoaDummy.getPessoas().get(index).setSobrenome(pessoa.getSobrenome());
//        this.pessoaDummy.getPessoas().get(index).setEndereco(pessoa.getEndereco());
//        this.pessoaDummy.getPessoas().get(index).setUsuario(pessoa.getUsuario());
//    }
//    
//    public void adcionarUsuario(Usuario usuario) {
//        this.usuarioDummy.getUsuarios().add(usuario);
//    }
//    
//    public void adicionarAdvogado(Advogado advogado) {
//        this.perfilDummy.getAdvogados().add(advogado);
//    }
//    
//    public void adicionarJuiz(Juiz juiz) {
//        this.perfilDummy.getJuizes().add(juiz);
//    }
}
