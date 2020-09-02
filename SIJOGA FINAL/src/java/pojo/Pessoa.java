package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "tb_pessoa", schema = "public")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pk_cpf", nullable = false, length = 11)
    private String pkCpf;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "sobrenome", length = 200)
    private String sobrenome;

    @Temporal(TemporalType.DATE)
    @Column(name = "nascimento")
    private Date nascimento;

    @OneToMany(mappedBy = "pessoa", targetEntity = Usuario.class, fetch = FetchType.EAGER)
    private List<Usuario> listOfUsuario;

    @OneToMany(mappedBy = "pessoa", targetEntity = Advogado.class, fetch = FetchType.EAGER)
    private List<Advogado> listOfAdvogado;

    @OneToMany(mappedBy = "pessoa", targetEntity = Parte.class, fetch = FetchType.EAGER)
    private List<Parte> listOfParte;

    @OneToMany(mappedBy = "pessoa", targetEntity = Endereco.class, fetch = FetchType.EAGER)
    private List<Endereco> listOfEndereco;

    @OneToMany(mappedBy = "pessoa", targetEntity = Juiz.class, fetch = FetchType.EAGER)
    private List<Juiz> listOfJuiz;

    public Pessoa() {
        super();
    }

    public void setPkCpf(String pkCpf) {
        this.pkCpf = pkCpf;
    }

    public String getPkCpf() {
        return this.pkCpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Date getNascimento() {
        return this.nascimento;
    }

    public void setListOfUsuario(List<Usuario> listOfUsuario) {
        this.listOfUsuario = listOfUsuario;
    }

    public List<Usuario> getListOfUsuario() {
        return this.listOfUsuario;
    }

    public void setListOfAdvogado(List<Advogado> listOfAdvogado) {
        this.listOfAdvogado = listOfAdvogado;
    }

    public List<Advogado> getListOfAdvogado() {
        return this.listOfAdvogado;
    }

    public void setListOfParte(List<Parte> listOfParte) {
        this.listOfParte = listOfParte;
    }

    public List<Parte> getListOfParte() {
        return this.listOfParte;
    }

    public void setListOfEndereco(List<Endereco> listOfEndereco) {
        this.listOfEndereco = listOfEndereco;
    }

    public List<Endereco> getListOfEndereco() {
        return this.listOfEndereco;
    }

    public void setListOfJuiz(List<Juiz> listOfJuiz) {
        this.listOfJuiz = listOfJuiz;
    }

    public List<Juiz> getListOfJuiz() {
        return this.listOfJuiz;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(pkCpf);
        sb.append("]:");
        sb.append(nome);
        sb.append("|");
        sb.append(sobrenome);
        sb.append("|");
        sb.append(nascimento);
        return sb.toString();
    }

}
