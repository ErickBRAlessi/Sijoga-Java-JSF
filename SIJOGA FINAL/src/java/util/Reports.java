/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author Erick
 */
public abstract class Reports {

    public static void gerarAdvEncerradosReport(int oabAdv) throws JRException, IOException, SQLException {
        String path = "/WEB-INF/reports/EncerradosAdv.jrxml";
        InputStream jasperTemplate = FacesContext.getCurrentInstance().
                getExternalContext().getResourceAsStream(path);

        // Compila o template.
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate);

        // Configura os parâmetros.
        Map parametros = new HashMap();
        parametros.put("OAB_ADV", oabAdv);

        // Cria a conexão com o banco de dados.
        Connection conexao = ConnectionFactory.getConnection();

        // Passagem dos parâmetros e preenchimento do relatório - informamos um
        // datasource vazio, pois a query do relatório irá trazer os dados.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

        conexao.close();
        // Obtém a resposta.
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"encerrados.pdf\"");

        // Exportar o relatório para PDF.
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public static void gerarAdvAbertosReport(int oabAdv, Date dataInicio, Date dataFim) throws JRException, IOException, SQLException {
        String path = "/WEB-INF/reports/AbertosAdv.jrxml";
        InputStream jasperTemplate = FacesContext.getCurrentInstance().
                getExternalContext().getResourceAsStream(path);

        // Compila o template.
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate);

        // Configura os parâmetros.
        Map parametros = new HashMap();
        parametros.put("OAB_ADV", oabAdv);
        parametros.put("DATA_INIC", dataInicio);
        parametros.put("DATA_FIM", dataFim);

        // Cria a conexão com o banco de dados.
        Connection conexao = ConnectionFactory.getConnection();

        // Passagem dos parâmetros e preenchimento do relatório - informamos um
        // datasource vazio, pois a query do relatório irá trazer os dados.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

        conexao.close();
        // Obtém a resposta.
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"abertos.pdf\"");

        // Exportar o relatório para PDF.
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public static void gerarParteRelatorio(String cpf) throws JRException, SQLException, IOException {
        String path = "/WEB-INF/reports/TodosParte.jrxml";
        InputStream jasperTemplate = FacesContext.getCurrentInstance().
                getExternalContext().getResourceAsStream(path);

        // Compila o template.
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperTemplate);

        // Configura os parâmetros.
        Map parametros = new HashMap();
        parametros.put("CPF", cpf);

        // Cria a conexão com o banco de dados.
        Connection conexao = ConnectionFactory.getConnection();

        // Passagem dos parâmetros e preenchimento do relatório - informamos um
        // datasource vazio, pois a query do relatório irá trazer os dados.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

        conexao.close();
        // Obtém a resposta.
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"parte.pdf\"");

        // Exportar o relatório para PDF.
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }
}
