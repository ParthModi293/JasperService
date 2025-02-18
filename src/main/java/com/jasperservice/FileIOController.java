package com.jasperservice;

import com.jasperservice.dto.RequestDto;
import com.jasperservice.dto.FileDto;
import com.jasperservice.service.CsvService;
import com.jasperservice.service.ExcelService;
import com.jasperservice.service.PdfService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JRException;
import org.common.common.ResponseBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/get-file")
@CrossOrigin
@Log4j2
public class FileIOController {

    private final PdfService fileService;


    private final ExcelService excelService;


    private final CsvService csvService;

    public FileIOController(PdfService fileService, ExcelService excelService, CsvService csvService) {
        this.fileService = fileService;
        this.excelService = excelService;
        this.csvService = csvService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<ResponseBean<String>> getBill(@Valid @RequestBody FileDto fileDto) throws JRException {
        log.info("Create pdf: {}", fileDto);
        ResponseBean<String> responseBean = fileService.generateJasperPdf(fileDto);
        return new ResponseEntity<>(responseBean, responseBean.getRStatus());
    }

    @PostMapping("/excel")
    public ResponseEntity<ResponseBean<String>> downloadExcel(@RequestBody RequestDto requestDto) throws IOException {
        log.info("Create excel: {}", requestDto);
        ResponseBean<String> responseBean = excelService.generateExcel(requestDto);
        return new  ResponseEntity<>(responseBean, responseBean.getRStatus());


    }

    @PostMapping("/csv")
    public ResponseEntity<ResponseBean<String>> downloadCsv(@RequestBody RequestDto requestDto) throws IOException {
        log.info("Create csv: {}", requestDto);
        ResponseBean<String> responseBean = csvService.generateCsv(requestDto);
        return new  ResponseEntity<>(responseBean, responseBean.getRStatus());



    }
}
