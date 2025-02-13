package com.jasperservice.validator;

import com.jasperservice.config.MessageService;
import com.jasperservice.dto.RequestDto;
import com.jasperservice.repository.FooterMasterRepository;
import com.jasperservice.repository.HeaderMasterRepository;
import org.common.common.Const;
import org.common.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class JasperPdfValidator {


    private final MessageService messageService;
    private final HeaderMasterRepository headerMasterRepository;
    private final FooterMasterRepository footerMasterRepository;

    public JasperPdfValidator(MessageService messageService, HeaderMasterRepository headerMasterRepository, FooterMasterRepository footerMasterRepository) {

        this.messageService = messageService;
        this.headerMasterRepository = headerMasterRepository;
        this.footerMasterRepository = footerMasterRepository;
    }

    public void validateJasperPdfRequest(RequestDto requestDto) {

        if(requestDto.getHeaderMasterId() == null || requestDto.getHeaderMasterId() <= 0 || ObjectUtils.isEmpty(requestDto.getHeaderMasterId())){
            throw new ValidationException(Const.rCode.BAD_REQUEST, HttpStatus.OK,
                    messageService.getMessage("HEADER_MASTER_ID_REQUIRED"),
                    messageService.getMessage("HEADER_MASTER_ID_REQUIRED"), null);
        }

        if(requestDto.getFooterMasterId() == null || requestDto.getFooterMasterId() <= 0 || ObjectUtils.isEmpty(requestDto.getFooterMasterId())){
            throw new ValidationException(Const.rCode.BAD_REQUEST, HttpStatus.OK,
                    messageService.getMessage("FOOTER_MASTER_ID_REQUIRED"),
                    messageService.getMessage("FOOTER_MASTER_ID_REQUIRED"), null);
        }

        if(!headerMasterRepository.existsById(requestDto.getHeaderMasterId())){
            throw new ValidationException(Const.rCode.BAD_REQUEST, HttpStatus.OK,
                    messageService.getMessage("HEADER_MASTER_ID_INVALID"),
                    messageService.getMessage("HEADER_MASTER_ID_INVALID"), null);
        }

        if(!footerMasterRepository.existsById(requestDto.getFooterMasterId())){
            throw new ValidationException(Const.rCode.BAD_REQUEST, HttpStatus.OK,
                    messageService.getMessage("FOOTER_MASTER_ID_INVALID"),
                    messageService.getMessage("FOOTER_MASTER_ID_INVALID"), null);
        }


    }


}
