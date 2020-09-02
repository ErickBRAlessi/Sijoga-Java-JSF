package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "tb_fase", schema = "public")
public class Fase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "faseSeq", 
            sequenceName = "public.tb_fase_pk_id_seq",
            initialValue=5, 
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faseSeq")
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;

    @Column(name = "titulo", nullable = false, length = 50)
    private String titulo;

    @Column(name = "reclamacao_parte", length = 10000)
    private String reclamacaoParte;

    @Column(name = "justificativa_juiz", length = 10000)
    private String justificativaJuiz;

    @Column(name = "status", length = 50)
    private String status;


    @Column(name = "documento")
    private byte[] documento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_abertura")
    private Date dataAbertura;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_decisao")
    private Date dataDecisao;

    @OneToMany(mappedBy = "fase", targetEntity = Intimacao.class, fetch = FetchType.EAGER)
    private List<Intimacao> listOfIntimacao;

    @ManyToOne
    @JoinColumn(name = "parte_abertura", referencedColumnName = "pk_id")
    private Parte parte;

    @ManyToOne
    @JoinColumn(name = "id_processo", referencedColumnName = "pk_id")
    private Processo processo;

    public Fase() {
        super();
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getReclamacaoParte() {
        return reclamacaoParte;
    }

    public void setReclamacaoParte(String reclamacaoParte) {
        this.reclamacaoParte = reclamacaoParte;
    }

    public String getJustificativaJuiz() {
        return justificativaJuiz;
    }

    public void setJustificativaJuiz(String justificativaJuiz) {
        this.justificativaJuiz = justificativaJuiz;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataDecisao() {
        return dataDecisao;
    }

    public void setDataDecisao(Date dataDecisao) {
        this.dataDecisao = dataDecisao;
    }

    public List<Intimacao> getListOfIntimacao() {
        return listOfIntimacao;
    }

    public void setListOfIntimacao(List<Intimacao> listOfIntimacao) {
        this.listOfIntimacao = listOfIntimacao;
    }

    public Parte getParte() {
        return parte;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

 

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("]:");
        sb.append(pkId);
        sb.append("|");
        sb.append(titulo);
        sb.append("|");
        sb.append(reclamacaoParte);
        sb.append("|");
        sb.append(justificativaJuiz);
        sb.append("|");
        sb.append(status);
        sb.append("|");
        sb.append(dataAbertura);
        sb.append("|");
        sb.append(dataDecisao);
        return sb.toString();
    }

}
