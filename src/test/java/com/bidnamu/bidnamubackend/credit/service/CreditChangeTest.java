package com.bidnamu.bidnamubackend.credit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bidnamu.bidnamubackend.credit.domain.CreditChangeHistory;
import com.bidnamu.bidnamubackend.credit.domain.CreditChangeReason;
import com.bidnamu.bidnamubackend.credit.dto.CreditChangeDto;
import com.bidnamu.bidnamubackend.credit.repository.CreditChangeHistoryRepository;
import com.bidnamu.bidnamubackend.user.domain.User;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreditChangeTest {

    @InjectMocks
    private CreditService creditService;

    @Mock
    private CreditChangeHistoryRepository creditChangeHistoryRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .nickname("nickname")
            .email("email@email.com")
            .password("password")
            .build();
    }

    @Test
    @DisplayName("크레딧 변경 요청 시 사용자의 크레딧 잔고에 반영되어야 한다")
    void givenCreditChangeRequest_whenProcessed_thenShouldReflectInUserBalance() {
        // Given
        final int balanceBefore = user.getCredit();
        final int changes1 = 1000;
        final int changes2 = -500;
        final var dto1 = new CreditChangeDto(user, changes1, CreditChangeReason.CHARGE);
        final var dto2 = new CreditChangeDto(user, changes2, CreditChangeReason.BID);

        // When
        creditService.changeCredit(dto1);
        creditService.changeCredit(dto2);

        // Then
        assertEquals(user.getCredit(), balanceBefore + changes1 + changes2);
    }

    @Test
    @DisplayName("크레딧 변경 요청 시 변경 정보가 DB에 저장되어야 한다")
    void givenCreditChangeRequest_whenProcessed_thenChangeShouldBePersistedInDatabase() {
        // Given
        final int changes = 1000;
        final var dto = new CreditChangeDto(user, changes, CreditChangeReason.CHARGE);
        final ArgumentCaptor<CreditChangeHistory> captor =
            ArgumentCaptor.forClass(CreditChangeHistory.class);

        // When
        creditService.changeCredit(dto);

        // Then
        verify(creditChangeHistoryRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("크레딧 감소 시 잔고가 부족한 경우 예외를 던져야 한다")
    void givenInsufficientBalance_whenDecreasingCredit_thenShouldThrowException()
        throws NoSuchFieldException, IllegalAccessException {
        // Given
        setCreditZero(user);

        // When
        final var dto = new CreditChangeDto(user, -200, CreditChangeReason.BID);

        // Then
        assertThrows(IllegalArgumentException.class, () -> creditService.changeCredit(dto));
    }

    @Test
    @DisplayName("크레딧 변동량과 사유가 일관적이지 않으면 예외를 던져야 한다")
    void givenInconsistentCreditChangeAndReason_whenProcessed_thenShouldThrowException() {
        // Given
        final var dto1 = new CreditChangeDto(user, -200, CreditChangeReason.CHARGE);
        final var dto2 = new CreditChangeDto(user, 200, CreditChangeReason.BID);

        // Then
        assertThrows(IllegalArgumentException.class, () -> creditService.changeCredit(dto1));
        assertThrows(IllegalArgumentException.class, () -> creditService.changeCredit(dto2));
    }

    private void setCreditZero(final User user)
        throws NoSuchFieldException, IllegalAccessException {
        final Field field = user.getClass().getDeclaredField("credit");
        field.setAccessible(true);
        field.set(user, 0);
    }
}
