package pojo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "tb_endereco", schema = "public")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pk_cpf", nullable = false, length = 11)
    private String pkCpf;

    @Column(name = "cep", nullable = false)
    private Integer cep;

    @Column(name = "complemento", nullable = false, length = 32)
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "pk_cpf", referencedColumnName = "pk_cpf", insertable = false, updatable = false)
    private Pessoa pessoa;

    public Endereco() {
        super();
    }

    public void setPkCpf(String pkCpf) {
        this.pkCpf = pkCpf;
    }

    public String getPkCpf() {
        return this.pkCpf;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Integer getCep() {
        return this.cep;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getComplemento() {
        return this.complemento;
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
        sb.append(cep);
        sb.append("|");
        sb.append(complemento);
        return sb.toString();
    }

}
