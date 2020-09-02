/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pojo.Processo;
import service.ProcessoListViewHelper;

/**
 *
 * @author cassiano
 */
@Named(value = "processoSession")
@SessionScoped
public class ProcessoSession implements Serializable {

    private Processo processoSel;
    private ProcessoListViewHelper processoHelperSel;
    private static ProcessoSession instance;
    
    public ProcessoSession() {
    }
    
    public static ProcessoSession getInstance() {
        if (instance == null) {
            instance = new ProcessoSession();
        }
        return instance;
    }

    public Processo getProcessoSel() {
        return processoSel;
    }
    
    public void setProcessoSel(Processo processoSel) {
        this.processoSel = processoSel;
    }

    public ProcessoListViewHelper getProcessoHelperSel() {
        return processoHelperSel;
    }

    public void setProcessoHelperSel(ProcessoListViewHelper processoHelperSel) {
        this.processoHelperSel = processoHelperSel;
    }
    
    public void encerrarSessao() {
        this.processoSel = null;
    }
    
}
