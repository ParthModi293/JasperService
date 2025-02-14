package com.jasperservice.service;


import com.jasperservice.dto.ExcelRequestDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public void generateExcel(ExcelRequestDto excelRequestDto) {

        if (excelRequestDto == null || excelRequestDto.getColumnHeader() == null || excelRequestDto.getDataList() == null) {
            throw new IllegalArgumentException("Column headers and data cannot be null");
        }

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Sheet 1");
        sheet.setDefaultColumnWidth(15);

        Row headerRow = sheet.createRow(1);
        CellStyle style = workbook.createCellStyle();
       XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(10);
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        Map<String, String> columnHeaders = excelRequestDto.getColumnHeader();
        List<String> headerNames = List.copyOf(columnHeaders.values());

        for (int i = 0; i < headerNames.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerNames.get(i));
            cell.setCellStyle(style);

        }

        List<Map<String, String>> dataList = excelRequestDto.getDataList();
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        for(int i=0; i < dataList.size(); i++){

            Row dataRow = sheet.createRow(i+3);
            Map<String, String> data = dataList.get(i);

            int columnIndex = 0;
           for(String headerKey : columnHeaders.keySet()){
               String value = data.get(headerKey);
               Cell cell = dataRow.createCell(columnIndex++);
               cell.setCellValue(value != null ? value : "");
              cell.setCellStyle(dataStyle);
           }


        }
//        return convertToBase64(workbook);

        try (FileOutputStream out = new FileOutputStream("/home/bizott-2/CodingPractice/test.xlsx")) {
           workbook.write(out);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String convertToBase64(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error while converting Excel to Base64", e);
        }
    }

}
