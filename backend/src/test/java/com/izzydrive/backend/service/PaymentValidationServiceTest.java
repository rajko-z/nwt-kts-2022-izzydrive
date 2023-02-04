package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionServiceImpl;
import com.izzydrive.backend.service.driving.validation.DrivingValidationServiceImpl;
import com.izzydrive.backend.service.payment.data.PaymentDataServiceImpl;
import com.izzydrive.backend.service.payment.process.validation.PaymentValidationServiceImpl;
import com.izzydrive.backend.service.users.passenger.PassengerServiceImpl;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.PaymentUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentValidationServiceTest {

    @InjectMocks
    private PaymentValidationServiceImpl paymentValidationService;

    @Mock
    private DrivingValidationServiceImpl drivingValidationService;

    @Mock
    private PassengerServiceImpl passengerService;

    @Mock
    private PaymentDataServiceImpl paymentDataService;

    @Mock
    private DrivingRejectionServiceImpl drivingRejectionService;


    @Test
    public void test_payment_session_did_not_expired() {
        Driving driving = new Driving();
        when(drivingValidationService.drivingExpiredForPayment(driving)).thenReturn(false);
        boolean result = paymentValidationService.validateForPaymentSessionExpiration(driving);
        assertTrue(result);
    }

    @Test
    public void test_payment_session_expired() {
        Driving driving = new Driving();
        driving.setId(1L);
        when(drivingValidationService.drivingExpiredForPayment(driving)).thenReturn(true);
        boolean result = paymentValidationService.validateForPaymentSessionExpiration(driving);
        verify(drivingRejectionService).removeDrivingPaymentSessionExpired(1L);
        assertFalse(result);
    }

    @Test
    public void should_throw_you_already_approved_this_paying() {
        Passenger p = new Passenger();
        p.setApprovedPaying(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentValidationService.validateForPassengerPaymentApproval(p, new CurrentPayingDTO()));

        assertEquals(ExceptionMessageConstants.YOU_ALREADY_APPROVED_THIS_PAYING, exception.getMessage());
    }

    @Test
    public void should_throw_cant_use_existing_paying_data() {
        Passenger p = new Passenger();
        p.setApprovedPaying(false);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();
        when(passengerService.passengerDoesNotHavePayingData(p)).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentValidationService.validateForPassengerPaymentApproval(p, currentPayingDTO));

        assertEquals(ExceptionMessageConstants.CANT_USE_EXISTING_PAYING_DATA, exception.getMessage());
    }

    @Test
    public void test_with_validating_provided_payment_data() {
        Passenger p = new Passenger();
        p.setApprovedPaying(false);
        CurrentPayingDTO currentPayingDTO = new CurrentPayingDTO();
        currentPayingDTO.setUsingExistingPayingInfo(false);
        KeyPairDTO keyPairDTO = new KeyPairDTO("secret", "ethAddress");
        currentPayingDTO.setOnceTimeKeyPair(keyPairDTO);

        when(passengerService.passengerDoesNotHavePayingData(p)).thenReturn(false);
        paymentValidationService.validateForPassengerPaymentApproval(p, currentPayingDTO);
        verify(paymentDataService).validatePaymentKeyPair(keyPairDTO);
    }


}
