/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author cassiano
 */
public class Ferramentas {

    private static Ferramentas instance;

    public static Ferramentas getInstance() {
        if (instance == null) {
            instance = new Ferramentas();
        }
        return instance;
    }

    /**
     * Remove caracteres extras do CPF (. e -) adicionados pelo inputMask do primefaces
     * @param cpf CPF para remover os caracteres
     * @return  CPF sem os caracteres
     */
    public String removerCaracteresCpf(String cpf) {
        String cpfLimpo = cpf.replace(".", "");
        cpfLimpo = cpfLimpo.replace("-", "");
        return cpfLimpo;
    }

    /**
     * Remove caracteres extras do CEP (-) adicionados pelo inputMask do primefaces
     * @param cep CEP para remover os caracteres
     * @return CEP sem os caracteres
     */
    public String removerCaracteresCep(String cep) {
        String cepLimpo = cep.replace("-", "");
        return cepLimpo;
    }

}
