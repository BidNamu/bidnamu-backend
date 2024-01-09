package com.bidnamu.bidnamubackend.credit.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bidnamu.bidnamubackend.credit.domain.CreditCharge;
import com.bidnamu.bidnamubackend.credit.repository.CreditChargeRepository;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private IamportClient iamportClient;

    @Mock
    private CreditChargeRepository creditChargeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CreditService creditService;

    private final String username = "test@example.com";
    private final String impUid = "imp_123456789";

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws IamportResponseException, IOException {
        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn("paid");
        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(10000));

        IamportResponse<Payment> iamportResponse;
        iamportResponse = (IamportResponse<Payment>) mock(IamportResponse.class);
        when(iamportResponse.getResponse()).thenReturn(payment);

        when(iamportClient.paymentByImpUid(impUid)).thenReturn(iamportResponse);
    }

    @Test
    void createPayment_Success() throws IamportResponseException, IOException {
        // Given
        User mockUser = new User(username, username, username);
        when(userService.findByEmail(username)).thenReturn(mockUser);

        // When
        IamportResponse<Payment> response = creditService.createPayment(username, impUid);

        // Then
        assertEquals("paid", response.getResponse().getStatus());
        verify(creditChargeRepository, times(1)).save(any(CreditCharge.class));
        assertEquals(10000, mockUser.getCredit());
    }

    // 추가적인 테스트 케이스 (예외 처리, 실패 케이스 등)를 작성할 수 있습니다.
}
