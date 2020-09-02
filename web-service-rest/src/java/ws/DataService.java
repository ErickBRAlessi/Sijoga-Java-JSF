package ws;

import model.Intimacao;
import model.OficialJustica;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    Intimacao intimacao = new Intimacao();

    public Intimacao getIntimacao() {
        return intimacao;
    }

    public void setIntimacao(Intimacao intimacao) {
        this.intimacao = intimacao;
    }
    private List<OficialJustica> oficialList = new ArrayList<>();
    private List<Intimacao> intimacaoList = new ArrayList<>();

    private static DataService dataInstance = new DataService();

    public static DataService getInstance() {
        return dataInstance;
    }
    
    public List<OficialJustica> getOficialList() {
        return oficialList;
    }    

    public List<Intimacao> getIntimacaoList() {
        return intimacaoList;
    }

    public void setOficialList(List<OficialJustica> lista) {
        this.oficialList = lista;
    }
    
    public void setIntimacaoList(List<Intimacao> lista) {
        this.intimacaoList = lista;
    }
     
    public void addOficial(OficialJustica oficial){        
        oficialList.add(oficial);        
    }
    
    public void addIntimacao(Intimacao intimacao){        
        intimacaoList.add(intimacao);        
    }

    public OficialJustica getOficialByMatricula(Long id) {
        for (OficialJustica o : oficialList) {
            if (o.getId().equals(id)) {
                return o;
            }
        }
        return null;
    }

    public Intimacao getIntimacaoById(Long id) {
        for (Intimacao i : intimacaoList) {
            if (i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }    
    
}