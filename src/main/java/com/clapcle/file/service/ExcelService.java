package com.clapcle.file.service;


import com.clapcle.core.common.ConstCore;
import com.clapcle.core.common.LogUtil;
import com.clapcle.core.common.ResponseBean;
import com.clapcle.core.exception.ValidationException;
import com.clapcle.file.config.FileLocaleService;
import com.clapcle.file.dto.RequestDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    private final FileLocaleService fileLocaleService;

    public ExcelService(FileLocaleService fileLocaleService) {
        this.fileLocaleService = fileLocaleService;
    }

    /**
     * @param requestDto {@link RequestDto}
     * @return Base64-encoded string of the generated Excel file.
     * @throws ValidationException If the request DTO or its required fields are null.
     * @apiNote Generates an Excel file from the provided {@link RequestDto}.
     * @author Parth
     */
    public ResponseBean<String> generateExcel(RequestDto requestDto) throws IOException {

        if (requestDto == null || requestDto.getColumnHeader() == null || requestDto.getDataList() == null) {
            throw new ValidationException(ConstCore.rCode.BAD_REQUEST, HttpStatus.OK,
                    fileLocaleService.getMessage("EXCEL_REQUEST_EMPTY"),
                    fileLocaleService.getMessage("EXCEL_REQUEST_EMPTY"), null);
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

        Map<String, String> columnHeaders = requestDto.getColumnHeader();
        List<String> headerNames = List.copyOf(columnHeaders.values());

        for (int i = 0; i < headerNames.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerNames.get(i));
            cell.setCellStyle(style);

        }

        List<Map<String, String>> dataList = requestDto.getDataList();
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < dataList.size(); i++) {

            Row dataRow = sheet.createRow(i + 3);
            Map<String, String> data = dataList.get(i);

            int columnIndex = 0;
            for (String headerKey : columnHeaders.keySet()) {
                String value = data.get(headerKey);
                Cell cell = dataRow.createCell(columnIndex++);
                cell.setCellValue(value != null ? value : "");
                cell.setCellStyle(dataStyle);
            }

        }

        return new ResponseBean<>(HttpStatus.OK, ConstCore.rCode.SUCCESS, "EXCEL_DOWNLOAD", "EXCEL_DOWNLOAD", convertToBase64(workbook));

    }


    public String convertToBase64(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            LogUtil.printErrorStackTraceLog(e);
            throw e;
        }
    }

}
