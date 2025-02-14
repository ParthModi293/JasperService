package com.jasperservice.service;

import com.jasperservice.dto.ExcelRequestDto;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    public String generateCsv(ExcelRequestDto excelRequestDto) {

        try {
            File f = new File("/home/bizott-2/CodingPractice/example.csv");
            FileWriter fileWriter=new FileWriter(f);

             CSVWriter csvWriter = new CSVWriter(fileWriter);


            Map<String, String> columnHeaders = excelRequestDto.getColumnHeader();
            List<String> headerNames = List.copyOf(columnHeaders.values());

            csvWriter.writeNext(headerNames.toArray(new String[0]));

            List<Map<String, String>> dataList = excelRequestDto.getDataList();
            for (Map<String, String> data : dataList) {

                String[] rowData = new String[headerNames.size()];
                int index = 0;

                for (String headerKey : columnHeaders.keySet()) {
                    rowData[index++] =  data.getOrDefault(headerKey, "");
                }

                csvWriter.writeNext(rowData);
            }
            csvWriter.flush();
            csvWriter.close();
//           return  f.getAbsolutePath();
            return convertFileToBase64(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String convertFileToBase64(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
