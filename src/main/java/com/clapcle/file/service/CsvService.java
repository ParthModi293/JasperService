package com.clapcle.file.service;

import com.clapcle.core.common.ConstCore;
import com.clapcle.core.common.LogUtil;
import com.clapcle.core.common.ResponseBean;
import com.clapcle.core.exception.ValidationException;
import com.clapcle.file.config.MessageService;
import com.clapcle.file.dto.RequestDto;
import com.opencsv.CSVWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    private final MessageService messageService;

    public CsvService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param requestDto The {@link RequestDto}
     * @return A Base64-encoded string representation of the generated CSV file.
     * @throws IOException         If an error occurs during CSV creation or encoding.
     * @throws ValidationException If the request is empty or contains invalid data.
     * @apiNote Generates a CSV file based on the provided data and returns it as a Base64-encoded string.
     * @author [Parth]
     */
    public ResponseBean<String> generateCsv(RequestDto requestDto) throws IOException {
        if (requestDto == null || requestDto.getColumnHeader() == null || requestDto.getDataList() == null) {
            throw new ValidationException(ConstCore.rCode.BAD_REQUEST, HttpStatus.OK,
                    messageService.getMessage("CSV_REQUEST_EMPTY"),
                    messageService.getMessage("CSV_REQUEST_EMPTY"), null);
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            CSVWriter csvWriter = new CSVWriter(outputStreamWriter);

            Map<String, String> columnHeaders = requestDto.getColumnHeader();
            List<String> headerNames = List.copyOf(columnHeaders.values());

            csvWriter.writeNext(headerNames.toArray(new String[0]));

            List<Map<String, String>> dataList = requestDto.getDataList();
            for (Map<String, String> data : dataList) {
                String[] rowData = new String[headerNames.size()];
                int index = 0;
                for (String headerKey : columnHeaders.keySet()) {
                    rowData[index++] = data.getOrDefault(headerKey, "");
                }
                csvWriter.writeNext(rowData);
            }

            csvWriter.flush();

            return new ResponseBean<>(HttpStatus.OK, ConstCore.rCode.SUCCESS, "CSV_DOWNLOAD", "CSV_DOWNLOAD", Base64.getEncoder().encodeToString(outputStream.toByteArray()));

        } catch (Exception e) {
            LogUtil.printErrorStackTraceLog(e);
            throw e;
        }

    }
}
