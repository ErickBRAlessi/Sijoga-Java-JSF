/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;


import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author cassiano
 */
public class ProcessoFacade {
//
//    private ProcessoDummy processoDummy = ProcessoDummy.getInstance();
//    private PerfilDummy perfilDummy = PerfilDummy.getInstance();
//
//    public List<Processo> getTodosProcessos() {
//        return processoDummy.getProcessos();
//    }
//
//    public List<Processo> getProcessosPorCpf(String cpf) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (p.getPartePromovente().getCpf().equals(cpf) || p.getPartePromovida().getCpf().equals(cpf)) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcesosPorJuiz(int oabJuiz) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (p.getJuiz().getOab() == oabJuiz) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosPorAdvogado(int oabAdvogado) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if ((p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oabAdvogado) || (p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oabAdvogado)) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public void addFase(Processo processo, Fase fase) {
//        List<Processo> processos = processoDummy.getProcessos();
//        int index = processos.indexOf(processo);
//        processos.get(index).getFases().add(fase);
//    }
//
//    public int getIdGeral() {
//        return processoDummy.getId();
//    }
//
//    public Juiz getJuizMaisSusse(String promovente, String promovido) {
//        Juiz juiz = null;
//        List<Juiz> juizes = perfilDummy.getJuizes();
//        int index = 0;
//        int menorQuantidade = -1;
//        int quantidadeAtual = 0;
//
//        for (int i = 0; i < juizes.size(); i++) {
//            quantidadeAtual = 0;
//            if (!juizes.get(i).getCpf().equals(promovente) && !juizes.get(i).getCpf().equals(promovido)) {
//                for (Processo p : this.getTodosProcessos()) {
//                    if (p.getJuiz().getOab() == juizes.get(i).getOab()) {
//                        quantidadeAtual++;
//                    }
//                }
//                if (menorQuantidade == -1) {
//                    index = i;
//                    menorQuantidade = quantidadeAtual;
//                } else {
//                    if (quantidadeAtual < menorQuantidade) {
//                        index = i;
//                        menorQuantidade = quantidadeAtual;
//                    }
//                }
//            }
//        }
//        juiz = juizes.get(index);
//        return juiz;
//    }
//
//    public List<Processo> getProcessosAbertos(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (((p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oab) || (p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oab)) && p.getStatus().equals("Aberto")) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosEncerrados(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (((p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oab) || (p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oab)) && p.getStatus().equals("Encerrado")) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosPromovido(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oab) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosPromovente(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        for (Processo p : processoDummy.getProcessos()) {
//            if (p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oab) {
//                processos.add(p);
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosVitoriosos(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        List<Processo> processosEncerrados = new ArrayList<>();
//        processosEncerrados = this.getProcessosEncerrados(oab);
//        if (processosEncerrados.size() != 0) {
//            for (Processo p : processosEncerrados) {
//                if (p.getVencedor() != null) {
//                    if ((p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oab && p.getVencedor().equals("Promovido")) || (p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oab && p.getVencedor().equals("Promovente"))) {
//                        processos.add(p);
//                    }
//                }
//            }
//        }
//        return processos;
//    }
//
//    public List<Processo> getProcessosDerrotados(int oab) {
//        List<Processo> processos = new ArrayList<>();
//        List<Processo> processosEncerrados = new ArrayList<>();
//        processosEncerrados = this.getProcessosEncerrados(oab);
//        if (processosEncerrados.size() != 0) {
//            for (Processo p : processosEncerrados) {
//                if (p.getVencedor() != null) {
//                    if ((p.getAdvogadoPromovido() != null && p.getAdvogadoPromovido().getOab() == oab && p.getVencedor().equals("Promovente")) || (p.getAdvogadoPromovente() != null && p.getAdvogadoPromovente().getOab() == oab && p.getVencedor().equals("Promovido"))) {
//                        processos.add(p);
//                    }
//                }
//            }
//        }
//        return processos;
//    }
//
//    public void atualizarProcesso(Processo processo, String parte) {
//        for (int i = 0; i < this.processoDummy.getProcessos().size(); i++) {
//            if (this.processoDummy.getProcessos().get(i).getId() == processo.getId()) {
//                if (parte.equals("promovente")) {
//                    this.processoDummy.getProcessos().get(i).setAdvogadoPromovente(processo.getAdvogadoPromovente());
//                } else {
//                    this.processoDummy.getProcessos().get(i).setAdvogadoPromovido(processo.getAdvogadoPromovido());
//                }
//                break;
//            }
//        }
//    }
//
//    public void atualizarFaseProcessoDeliberacao(Fase fase, int idProcesso, int indexFase) {
//        int indexProcesso = 0;
//        for (int i = 0; i < this.processoDummy.getProcessos().size(); i++) {
//            if (this.processoDummy.getProcessos().get(i).getId() == idProcesso) {
//                indexProcesso = i;
//                break;
//            }
//        }
//        this.processoDummy.getProcessos().get(indexProcesso).getFases().get(indexFase).setJustificativaJuiz(fase.getJustificativaJuiz());
//        this.processoDummy.getProcessos().get(indexProcesso).getFases().get(indexFase).setStatus(fase.getStatus());
//    }
//
//    public void adicionarFaseEmProcesso(Fase fase, int idProcesso) {
//        int indexProcesso = 0;
//        for (int i = 0; i < this.processoDummy.getProcessos().size(); i++) {
//            if (this.processoDummy.getProcessos().get(i).getId() == idProcesso) {
//                indexProcesso = i;
//                break;
//            }
//        }
//        this.processoDummy.getProcessos().get(indexProcesso).getFases().add(fase);
//    }
//
//    public void atualizarProcessoEncerramento(Processo processo) {
//        for (int i = 0; i < this.processoDummy.getProcessos().size(); i++) {
//            if (this.processoDummy.getProcessos().get(i).getId() == processo.getId()) {
//                this.processoDummy.getProcessos().get(i).setStatus(processo.getStatus());
//                this.processoDummy.getProcessos().get(i).setVencedor(processo.getVencedor());
//                break;
//            }
//        }
//    }

}
