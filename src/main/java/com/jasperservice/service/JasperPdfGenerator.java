package com.jasperservice.service;


import com.jasperservice.dto.RequestDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.*;

@Service
public class JasperPdfGenerator {

    public void generateJasperPdf(RequestDto requestDto) throws JRException, FileNotFoundException {

        List<Map<String, String>> list = new ArrayList<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("MainReport");

        JRDesignParameter headerParam = new JRDesignParameter();
        headerParam.setName("header");
        headerParam.setValueClass(JasperReport.class);
        jasperDesign.addParameter(headerParam);

        JRDesignParameter footerParam = new JRDesignParameter();
        footerParam.setName("footer");
        footerParam.setValueClass(JasperReport.class);
        jasperDesign.addParameter(footerParam);

//        jasperDesign.setPageWidth(595);
//        jasperDesign.setPageHeight(842);
//        jasperDesign.setColumnWidth(515);
//        jasperDesign.setColumnSpacing(0);
//        jasperDesign.setLeftMargin(0);
//        jasperDesign.setRightMargin(0);
//        jasperDesign.setTopMargin(0);
//        jasperDesign.setBottomMargin(0);

        {
            JRDesignParameter logo = new JRDesignParameter();
            logo.setName("logo");
            logo.setValueClass(String.class);
            jasperDesign.addParameter(logo);

            JRDesignParameter companyName = new JRDesignParameter();
            companyName.setName("companyName");
            companyName.setValueClass(String.class);
            jasperDesign.addParameter(companyName);

            JRDesignParameter address = new JRDesignParameter();
            address.setName("address");
            address.setValueClass(String.class);
            jasperDesign.addParameter(address);

            JRDesignParameter templateName = new JRDesignParameter();
            templateName.setName("templateName");
            templateName.setValueClass(String.class);
            jasperDesign.addParameter(templateName);
        }

        jasperDesign.setColumnSpacing(2);
        for (int i = 1; i <= 50; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("code", String.valueOf(100 + i));
            map.put("clientName", "Client " + i);
            map.put("debit", String.valueOf(100 + i * 10));
            map.put("credit", String.valueOf(100.5 + i * 5));
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("logo", "/home/bizott3/Downloads/photo.jpg");
        map.put("companyName", "Bizott Pvt. Ltd.");
        map.put("address", "401,Amora arced,Utran,Surat");
        map.put("templateName", "User Add");

        Map<String, String> columnName = new LinkedHashMap<>();
        columnName.put("Code", "code");
        columnName.put("Client Name", "clientName");
        columnName.put("Debit", "debit");
        columnName.put("Credit", "credit");

        int columnCount = columnName.size();
        int columnWidth = jasperDesign.getColumnWidth() / columnCount;
        JRDesignBand headerBand = new JRDesignBand();
        headerBand.setHeight(20);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(18);

        int xPosition = 0;
        for (Map.Entry<String, String> entry : columnName.entrySet()) {
            JRDesignField field = new JRDesignField();
            field.setName(entry.getValue());
            field.setValueClass(String.class);
            jasperDesign.addField(field);

            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(xPosition);
            headerText.setY(0);
            headerText.setWidth(columnWidth);
            headerText.setHeight(20);
            headerText.setText(entry.getKey());
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            headerText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            headerText.setBackcolor(Color.LIGHT_GRAY);
            headerText.setMode(ModeEnum.OPAQUE);
            headerText.getLineBox().getPen().setLineWidth(1f);
            headerText.getLineBox().getPen().setLineColor(Color.BLACK);
            headerBand.addElement(headerText);

            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(xPosition);
            textField.setY(0);
            textField.setWidth(columnWidth);
            textField.setHeight(18);
            textField.setExpression(new JRDesignExpression("$F{" + entry.getValue() + "}"));
            textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            detailBand.addElement(textField);
            textField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            textField.getLineBox().getPen().setLineWidth(1f);
            textField.getLineBox().getPen().setLineColor(Color.BLACK);
            xPosition += columnWidth;
        }

        jasperDesign.setColumnHeader(headerBand);

        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);


        JasperReport headerReport = JasperCompileManager.compileReport("/home/bizott3/JaspersoftWorkspace/MyReports/demoHeader.jrxml");
        JasperReport footerReport = JasperCompileManager.compileReport("/home/bizott3/JaspersoftWorkspace/MyReports/demoFooter.jrxml");

        JRDesignBand hBand = new JRDesignBand();
        JRDesignSubreport headerSR = new JRDesignSubreport(jasperDesign);
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("new net.sf.jasperreports.engine.JREmptyDataSource()");
        headerSR.setDataSourceExpression(expression);
        headerSR.setExpression(new JRDesignExpression("$P{header}"));
        {
            JRDesignSubreportParameter logo = new JRDesignSubreportParameter();
            logo.setName("logo");
            logo.setExpression(new JRDesignExpression("$P{logo}"));
            headerSR.addParameter(logo);

            JRDesignSubreportParameter companyName = new JRDesignSubreportParameter();
            companyName.setName("companyName");
            companyName.setExpression(new JRDesignExpression("$P{companyName}"));
            headerSR.addParameter(companyName);


            JRDesignSubreportParameter address = new JRDesignSubreportParameter();
            address.setName("address");
            address.setExpression(new JRDesignExpression("$P{address}"));
            headerSR.addParameter(address);

            JRDesignSubreportParameter templateName = new JRDesignSubreportParameter();
            templateName.setName("templateName");
            templateName.setExpression(new JRDesignExpression("$P{templateName}"));
            headerSR.addParameter(templateName);
        }
        hBand.addElement(headerSR);

        JRDesignBand fBand = new JRDesignBand();
        JRDesignSubreport footerSR = new JRDesignSubreport(jasperDesign);
        JRDesignExpression expression2 = new JRDesignExpression();
        expression2.setText("new net.sf.jasperreports.engine.JREmptyDataSource()");
        footerSR.setDataSourceExpression(expression2);
        footerSR.setExpression(new JRDesignExpression("$P{footer}"));
        System.out.println(footerSR.getHeight());
        fBand.setHeight(42);
        fBand.addElement(footerSR);

        jasperDesign.setTitle(hBand);
        jasperDesign.setPageFooter(fBand);


        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("header", headerReport);
        parameters.put("footer", footerReport);
        parameters.put("logo", "/home/bizott3/Downloads/photo.jpg");
        parameters.put("companyName", "Bizott Pvt. Ltd.");
        parameters.put("address", "401,Amora arced,Utran,Surat");
        parameters.put("templateName", "User Add");

        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        FileOutputStream fileOutputStream = new FileOutputStream("/home/bizott3/Downloads/zeel1.pdf");
        JasperExportManager.exportReportToPdfStream(jasperPrint, fileOutputStream);
        String s = JasperExportManager.exportReportToXml(jasperPrint);
        System.out.println(s);
    }
}
