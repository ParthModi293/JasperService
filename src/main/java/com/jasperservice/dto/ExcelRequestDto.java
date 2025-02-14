package com.jasperservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelRequestDto {

    private List<Map<String,String>> dataList;
    private Map<String,String> columnHeader;


}
