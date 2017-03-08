package org.egov.crn.web.controller;

import org.egov.crn.domain.service.CrnGeneratorService;
import org.egov.crn.web.contract.BaseRequest;
import org.egov.crn.web.contract.ComplaintRegistrationNumber;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crn")
public class CrnController {

    private CrnGeneratorService crnGeneratorService;

    public CrnController(CrnGeneratorService crnGeneratorService) {
        this.crnGeneratorService = crnGeneratorService;
    }

    @PostMapping()
    public ComplaintRegistrationNumber getCrn(@RequestBody BaseRequest request) {
        return new ComplaintRegistrationNumber(crnGeneratorService.generate());
    }
}