/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Erick
 */
public abstract class ConnectionFactory {
    
    public static Connection getConnection(){
     Connection conexao = null;
     try {
       Class.forName("org.postgresql.Driver");
       conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sijoga", "postgres", "admin");
     } catch (Exception e) {
        e.printStackTrace();
     }
     return conexao;
    }
}
