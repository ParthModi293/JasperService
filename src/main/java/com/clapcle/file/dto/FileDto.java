package com.clapcle.file.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FileDto {

    private List<Map<String, String>> dataList;
    private Map<String, String> headerParameters;
    private Map<String, String> footerParameters;
    private String headerFilePath;
    private String footerFilePath;
    private Map<String, String> columnNames;
}
