package com.bidnamu.bidnamubackend.credit.service;

import com.bidnamu.bidnamubackend.credit.domain.CreditChangeHistory;
import com.bidnamu.bidnamubackend.credit.domain.CreditCharge;
import com.bidnamu.bidnamubackend.credit.domain.PaymentStatus;
import com.bidnamu.bidnamubackend.credit.dto.CreditChangeDto;
import com.bidnamu.bidnamubackend.credit.repository.CreditChangeHistoryRepository;
import com.bidnamu.bidnamubackend.credit.repository.CreditChargeRepository;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final IamportClient iamportClient;
    private final CreditChargeRepository creditChargeRepository;
    private final CreditChangeHistoryRepository creditChangeHistoryRepository;
    private final UserService userService;

    @Transactional
    public IamportResponse<Payment> createPayment(
        final String username,
        final String impUid
    ) throws IamportResponseException, IOException {
        final IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        final Payment body = response.getResponse();
        final PaymentStatus status = PaymentStatus.of(body.getStatus());

        final int amount = body.getAmount().intValue();
        final User user = userService.findByEmail(username);
        creditChargeRepository.save(
            CreditCharge.builder().amount(amount).iamportUid(impUid).status(status).user(user)
                .build());
        user.changeCredit(amount);

        return response;
    }

    @Transactional
    public CreditChangeHistory changeCredit(final CreditChangeDto dto) {
        dto.validateAndApply();
        return creditChangeHistoryRepository.save(dto.toEntity());
    }

}
