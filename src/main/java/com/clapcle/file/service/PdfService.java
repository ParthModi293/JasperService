package com.clapcle.file.service;

import com.clapcle.core.common.ConstCore;
import com.clapcle.core.common.ResponseBean;
import com.clapcle.file.dto.FileDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfService {

    /**
     * @param fileDto
     * @return String
     * @throws JRException
     * @author Zeel
     * @apiNote This api is used to create pdf with dynamic data ( with dynamic header,footer and data content also )
     * and return base64 of the pdf
     */
    public ResponseBean<String> generateJasperPdf(FileDto fileDto) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("MainReport");
        jasperDesign.setColumnSpacing(2);

        JRDesignParameter headerParam = new JRDesignParameter();
        headerParam.setName("header");
        headerParam.setValueClass(JasperReport.class);
        jasperDesign.addParameter(headerParam);

        JRDesignParameter footerParam = new JRDesignParameter();
        footerParam.setName("footer");
        footerParam.setValueClass(JasperReport.class);
        jasperDesign.addParameter(footerParam);

        for (Map.Entry<String, String> map : fileDto.getHeaderParameters().entrySet()) {
            JRDesignParameter param = new JRDesignParameter();
            param.setName(map.getKey());
            param.setValueClass(String.class);
            jasperDesign.addParameter(param);
        }

        if (fileDto.getFooterParameters() != null && !fileDto.getFooterParameters().isEmpty()) {
            for (Map.Entry<String, String> map : fileDto.getFooterParameters().entrySet()) {
                JRDesignParameter param = new JRDesignParameter();
                param.setName(map.getKey());
                param.setValueClass(String.class);
                jasperDesign.addParameter(param);
            }
        }

        int columnCount = fileDto.getColumnNames().size();
        int columnWidth = jasperDesign.getColumnWidth() / columnCount;
        JRDesignBand headerBand = new JRDesignBand();
        headerBand.setHeight(20);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(18);

        int xPosition = 0;
        for (Map.Entry<String, String> entry : fileDto.getColumnNames().entrySet()) {
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

        JasperReport headerReport = JasperCompileManager.compileReport(fileDto.getHeaderFilePath());
        JasperReport footerReport = JasperCompileManager.compileReport(fileDto.getFooterFilePath());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("header", headerReport);
        parameters.put("footer", footerReport);

        JRDesignBand hBand = new JRDesignBand();
        JRDesignSubreport headerSR = new JRDesignSubreport(jasperDesign);
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("new net.sf.jasperreports.engine.JREmptyDataSource()");
        headerSR.setDataSourceExpression(expression);
        headerSR.setExpression(new JRDesignExpression("$P{header}"));

        for (Map.Entry<String, String> map : fileDto.getHeaderParameters().entrySet()) {
            JRDesignSubreportParameter subParam = new JRDesignSubreportParameter();
            subParam.setName(map.getKey());
            subParam.setExpression(new JRDesignExpression("$P{" + map.getKey() + "}"));
            headerSR.addParameter(subParam);
            parameters.put(map.getKey(), map.getValue());
        }

        hBand.addElement(headerSR);

        JRDesignBand fBand = new JRDesignBand();
        JRDesignSubreport footerSR = new JRDesignSubreport(jasperDesign);
        JRDesignExpression expression2 = new JRDesignExpression();
        expression2.setText("new net.sf.jasperreports.engine.JREmptyDataSource()");
        footerSR.setDataSourceExpression(expression2);
        footerSR.setExpression(new JRDesignExpression("$P{footer}"));
        if (fileDto.getFooterParameters() != null && !fileDto.getFooterParameters().isEmpty()) {
            for (Map.Entry<String, String> map : fileDto.getFooterParameters().entrySet()) {
                JRDesignSubreportParameter subParam = new JRDesignSubreportParameter();
                subParam.setName(map.getKey());
                subParam.setExpression(new JRDesignExpression("$P{" + map.getKey() + "}"));
                headerSR.addParameter(subParam);
                parameters.put(map.getKey(), map.getValue());
            }
        }
        fBand.setHeight(42);
        fBand.addElement(footerSR);
        jasperDesign.setTitle(hBand);
        jasperDesign.setPageFooter(fBand);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(fileDto.getDataList());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        return new ResponseBean<>(HttpStatus.OK, ConstCore.rCode.SUCCESS, "Pdf fetched successfully", "Pdf fetched successfully",
                Base64.getEncoder().encodeToString(outputStream.toByteArray()));
    }
}
