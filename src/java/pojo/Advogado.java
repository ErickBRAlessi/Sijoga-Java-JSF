package pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "tb_advogado", schema = "public")
public class Advogado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pk_oab", nullable = false)
    private Integer pkOab;

    @OneToMany(mappedBy = "advogado", targetEntity = AdvogadoParte.class, fetch = FetchType.EAGER)
    private List<AdvogadoParte> listOfAdvogadoParte;

    @ManyToOne
    @JoinColumn(name = "cpf", referencedColumnName = "pk_cpf")
    private Pessoa pessoa;

    public Advogado() {
        super();
    }

    public void setPkOab(Integer pkOab) {
        this.pkOab = pkOab;
    }

    public Integer getPkOab() {
        return this.pkOab;
    }

    public void setListOfAdvogadoParte(List<AdvogadoParte> listOfAdvogadoParte) {
        this.listOfAdvogadoParte = listOfAdvogadoParte;
    }

    public List<AdvogadoParte> getListOfAdvogadoParte() {
        return this.listOfAdvogadoParte;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(pkOab);
        sb.append("]:");
        return sb.toString();
    }

}
