package pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "tb_juiz", schema = "public")
public class Juiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pk_oab", nullable = false)
    private Integer pkOab;

    @ManyToOne
    @JoinColumn(name = "cpf", referencedColumnName = "pk_cpf")
    private Pessoa pessoa;

    @OneToMany(mappedBy = "juiz", targetEntity = Processo.class)
    private List<Processo> listOfProcesso;

    public Juiz() {
        super();
    }

    public void setPkOab(Integer pkOab) {
        this.pkOab = pkOab;
    }

    public Integer getPkOab() {
        return this.pkOab;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setListOfProcesso(List<Processo> listOfProcesso) {
        this.listOfProcesso = listOfProcesso;
    }

    public List<Processo> getListOfProcesso() {
        return this.listOfProcesso;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(pkOab);
        sb.append("]:");
        return sb.toString();
    }

}
