package com.jasperservice;

import com.jasperservice.dto.RequestDto;
import com.jasperservice.dto.FileDto;
import com.jasperservice.service.CsvService;
import com.jasperservice.service.ExcelService;
import com.jasperservice.service.FileService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.common.common.ResponseBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<ResponseBean<String>> downloadExcel(@RequestBody RequestDto requestDto) throws IOException {
        ResponseBean<String> responseBean = excelService.generateExcel(requestDto);
        return new  ResponseEntity<>(responseBean, responseBean.getRStatus());


    }

    @PostMapping("/csv")
    public ResponseEntity<ResponseBean<String>> downloadCsv(@RequestBody RequestDto requestDto) throws IOException {
        ResponseBean<String> responseBean = csvService.generateCsv(requestDto);
        return new  ResponseEntity<>(responseBean, responseBean.getRStatus());



    }
}
