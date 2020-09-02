package pojo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "tb_usuario", schema = "public")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pk_cpf", nullable = false, length = 11)
    private String pkCpf;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "senha", nullable = false, length = 32)
    private String senha;

    @Column(name = "admin")
    private Boolean admin;

    @ManyToOne
    @JoinColumn(name = "pk_cpf", referencedColumnName = "pk_cpf", insertable = false, updatable = false)
    private Pessoa pessoa;

    public Usuario() {
        super();
    }

    public void setPkCpf(String pkCpf) {
        this.pkCpf = pkCpf;
    }

    public String getPkCpf() {
        return this.pkCpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getAdmin() {
        return this.admin;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(pkCpf);
        sb.append("]:");
        sb.append(email);
        sb.append("|");
        sb.append(senha);
        sb.append("|");
        sb.append(admin);
        return sb.toString();
    }

}
