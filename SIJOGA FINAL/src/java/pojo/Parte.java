package pojo;

import java.io.Serializable;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "tb_parte", schema = "public")
public class Parte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "parteSeq", 
            sequenceName = "public.tb_parte_pk_id_seq",
            initialValue=5, 
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parteSeq")
    @Column(name = "pk_id", nullable = false)
    private Integer pkId;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @OneToMany(mappedBy = "parte", targetEntity = AdvogadoParte.class, fetch = FetchType.EAGER)
    private List<AdvogadoParte> listOfAdvogadoParte;

    @OneToMany(mappedBy = "promovido", targetEntity = Processo.class, fetch = FetchType.EAGER)
    private List<Processo> listOfProcessoComoPromovido;

    @OneToMany(mappedBy = "promovente", targetEntity = Processo.class, fetch = FetchType.EAGER)
    private List<Processo> listOfProcessoComoPromovente;

    @OneToMany(mappedBy = "vencedor", targetEntity = Processo.class, fetch = FetchType.EAGER)
    private List<Processo> listOfProcessoComoVencedor;

    @OneToMany(mappedBy = "parte", targetEntity = Fase.class, fetch = FetchType.EAGER)
    private List<Fase> listOfFase;
    
    @OneToMany(mappedBy = "intimado", targetEntity = Intimacao.class, fetch = FetchType.EAGER)
    private List<Intimacao> listOfIntimacao;

    @ManyToOne
    @JoinColumn(name = "cpf", referencedColumnName = "pk_cpf")
    private Pessoa pessoa;

    public Parte() {
        super();
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<AdvogadoParte> getListOfAdvogadoParte() {
        return listOfAdvogadoParte;
    }

    public void setListOfAdvogadoParte(List<AdvogadoParte> listOfAdvogadoParte) {
        this.listOfAdvogadoParte = listOfAdvogadoParte;
    }

    public List<Processo> getListOfProcessoComoPromovido() {
        return listOfProcessoComoPromovido;
    }

    public void setListOfProcessoComoPromovido(List<Processo> listOfProcessoComoPromovido) {
        this.listOfProcessoComoPromovido = listOfProcessoComoPromovido;
    }

    public List<Processo> getListOfProcessoComoPromovente() {
        return listOfProcessoComoPromovente;
    }

    public void setListOfProcessoComoPromovente(List<Processo> listOfProcessoComoPromovente) {
        this.listOfProcessoComoPromovente = listOfProcessoComoPromovente;
    }

    public List<Processo> getListOfProcessoComoVencedor() {
        return listOfProcessoComoVencedor;
    }

    public void setListOfProcessoComoVencedor(List<Processo> listOfProcessoComoVencedor) {
        this.listOfProcessoComoVencedor = listOfProcessoComoVencedor;
    }

    public List<Fase> getListOfFase() {
        return listOfFase;
    }

    public void setListOfFase(List<Fase> listOfFase) {
        this.listOfFase = listOfFase;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("]:");
        sb.append(pkId);
        sb.append("|");
        sb.append(tipo);
        return sb.toString();
    }

}
