package pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "tb_intimacao", schema = "public")
public class Intimacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "intimacaoSeq", 
            sequenceName = "public.tb_intimacao_pk_id_seq",
            initialValue=5, 
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intimacaoSeq")
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;

    @Column(name = "oficial_de_justica")
    private Integer oficialDeJustica;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_abertura")
    private Date dataAbertura;

    @ManyToOne
    @JoinColumn(name = "fase", referencedColumnName = "pk_id")
    private Fase fase;
    
    @ManyToOne
    @JoinColumn(name = "intimado", referencedColumnName = "pk_id")
    private Parte intimado;

    @Column(name = "isefetivada")
    private boolean isEfetivada;
    
    @Column(name = "dataexecucaointimacao")
    private Date dataExecucaoIntimacao;
    
    public Intimacao() {
        super();
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return this.pkId;
    }

    public void setOficialDeJustica(Integer oficialDeJustica) {
        this.oficialDeJustica = oficialDeJustica;
    }

    public Integer getOficialDeJustica() {
        return this.oficialDeJustica;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataAbertura() {
        return this.dataAbertura;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Fase getFase() {
        return this.fase;
    }

    public Parte getIntimado() {
        return intimado;
    }

    public void setIntimado(Parte intimado) {
        this.intimado = intimado;
    }

    public boolean isIsEfetivada() {
        return isEfetivada;
    }

    public void setIsEfetivada(boolean isEfetiva) {
        this.isEfetivada = isEfetiva;
    }

    public Date getDataExecucaoIntimacao() {
        return dataExecucaoIntimacao;
    }

    public void setDataExecucaoIntimacao(Date dataExecucaoIntimacao) {
        this.dataExecucaoIntimacao = dataExecucaoIntimacao;
    }    

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("]:");
        sb.append(pkId);
        sb.append("|");
        sb.append(oficialDeJustica);
        sb.append("|");
        sb.append(dataAbertura);
        return sb.toString();
    }

}
