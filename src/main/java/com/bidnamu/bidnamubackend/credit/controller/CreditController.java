package com.bidnamu.bidnamubackend.credit.controller;

import com.bidnamu.bidnamubackend.credit.service.CreditService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @PostMapping("/transactions/{impUid}")
    public IamportResponse<Payment> chargeCredit(Principal principal, @PathVariable String impUid)
        throws IamportResponseException, IOException {
        return creditService.createPayment(principal.getName(), impUid);
    }
}
