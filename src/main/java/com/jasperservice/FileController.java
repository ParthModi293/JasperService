package com.jasperservice;

import com.jasperservice.dto.FileDto;
import com.jasperservice.service.FileService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.common.common.ResponseBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/get-pdf")
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping()
    public ResponseEntity<ResponseBean<String>> getBill(@Valid @RequestBody FileDto fileDto) throws JRException {
        ResponseBean<String> responseBean = fileService.generateJasperPdf(fileDto);
        return new ResponseEntity<>(responseBean, responseBean.getRStatus());
    }
}
