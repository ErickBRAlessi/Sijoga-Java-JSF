package pojo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "tb_advogado_parte", schema = "public")
public class AdvogadoParte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "advogadoParteSeq",
            sequenceName = "public.tb_advogado_parte_pk_id_seq",
            initialValue = 5,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advogadoParteSeq")
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;

    @ManyToOne
    @JoinColumn(name = "oab_adv", referencedColumnName = "pk_oab")
    private Advogado advogado;

    @ManyToOne
    @JoinColumn(name = "id_parte", referencedColumnName = "pk_id")
    private Parte parte;

    public AdvogadoParte() {
        super();
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return this.pkId;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public Advogado getAdvogado() {
        return this.advogado;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Parte getParte() {
        return this.parte;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("]:");
        sb.append(pkId);
        return sb.toString();
    }

}
