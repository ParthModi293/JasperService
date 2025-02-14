package com.jasperservice;

import com.jasperservice.dto.ExcelRequestDto;
import com.jasperservice.dto.FileDto;
import com.jasperservice.service.CsvService;
import com.jasperservice.service.ExcelService;
import com.jasperservice.service.FileService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.common.common.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/get-pdf")
@CrossOrigin
public class FileController {

    private final FileService fileService;


    private final ExcelService excelService;


    private final CsvService csvService;

    public FileController(FileService fileService, ExcelService excelService, CsvService csvService) {
        this.fileService = fileService;
        this.excelService = excelService;
        this.csvService = csvService;
    }

    @PostMapping()
    public ResponseEntity<ResponseBean<String>> generateJasperPdf(@Valid @RequestBody FileDto fileDto) throws JRException {
        ResponseBean<String> responseBean = fileService.generateJasperPdf(fileDto);
        return new ResponseEntity<>(responseBean, responseBean.getRStatus());
    }

    @PostMapping("/excel")
    public ResponseEntity<?> downloadExcel(@RequestBody ExcelRequestDto excelRequestDto) {

        excelService.generateExcel(excelRequestDto);
        return ResponseEntity.ok()
                .body("Download");


    }

    @PostMapping("/csv")
    public ResponseEntity<?> downloadCsv(@RequestBody ExcelRequestDto excelRequestDto) {

        csvService.generateCsv(excelRequestDto);
        return ResponseEntity.ok()
                .body("Download");


    }
}
