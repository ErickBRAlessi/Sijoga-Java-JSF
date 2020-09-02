package pojo;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "tb_processo", schema = "public")
public class Processo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "processoSeq", 
            sequenceName = "public.tb_processo_pk_id_seq",
            initialValue=2, 
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processoSeq")
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_abertura")
    private Date dataAbertura;

    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "promovente", referencedColumnName = "pk_id")
    private Parte promovente;

    @ManyToOne
    @JoinColumn(name = "promovido", referencedColumnName = "pk_id")
    private Parte promovido;

    @ManyToOne
    @JoinColumn(name = "vencedor", referencedColumnName = "pk_id")
    private Parte vencedor;

    @ManyToOne
    @JoinColumn(name = "oab_juiz", referencedColumnName = "pk_oab")
    private Juiz juiz;

    @OneToMany(mappedBy = "processo", targetEntity = Fase.class, fetch = FetchType.EAGER)
    private List<Fase> listOfFase;

    public Processo() {
        super();
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return this.pkId;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataAbertura() {
        return this.dataAbertura;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setPromovente(Parte promovente) {
        this.promovente = promovente;
    }

    public Parte getPromovente() {
        return this.promovente;
    }

    public void setPromovido(Parte promovido) {
        this.promovido = promovido;
    }

    public Parte getPromovido() {
        return this.promovido;
    }

    public void setVencedor(Parte vencedor) {
        this.vencedor = vencedor;
    }

    public Parte getVencedor() {
        return this.vencedor;
    }

    public void setJuiz(Juiz juiz) {
        this.juiz = juiz;
    }

    public Juiz getJuiz() {
        return this.juiz;
    }

    public void setListOfFase(List<Fase> listOfTbFase) {
        this.listOfFase = listOfFase;
    }

    public List<Fase> getListOfFase() {
        return this.listOfFase;
    }

    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("]:");
        sb.append(pkId);
        sb.append("|");
        sb.append(dataAbertura);
        sb.append("|");
        sb.append(status);
        return sb.toString();
    }

}
