package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingServiceImpl;
import com.izzydrive.backend.service.payment.process.PaymentProcessServiceImpl;
import com.izzydrive.backend.service.payment.process.afterpayment.AfterPaymentServiceImpl;
import com.izzydrive.backend.service.payment.process.transfer.PaymentTransferServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessServiceTest {


    @InjectMocks
    private PaymentProcessServiceImpl paymentProcessService;

    @Mock
    private PassengerServiceImpl passengerService;

    @Mock
    private DrivingServiceImpl drivingService;

    @Mock
    private AfterPaymentServiceImpl afterPaymentService;

    @Mock
    private PaymentValidationServiceImpl paymentValidationService;

    @Mock
    private PaymentTransferServiceImpl paymentTransferService;

    @Test
    public void should_throw_no_driving_for_payment_because_no_ride_exist() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> paymentProcessService.approvePayment(currentPayingDTO));
        assertEquals(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT, exception.getMessage());
    }

    @Test
    public void should_throw_no_driving_for_payment_because_driving_not_in_payment_status() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        Driving d = new Driving();
        d.setDrivingState(DrivingState.FINISHED);
        p.setCurrentDriving(d);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> paymentProcessService.approvePayment(currentPayingDTO));
        assertEquals(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT, exception.getMessage());
    }


    @Test
    public void should_throw_payment_session_expired() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        Driving d = new Driving();
        d.setDrivingState(DrivingState.PAYMENT);
        p.setCurrentDriving(d);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);
        when(paymentValidationService.validateForPaymentSessionExpiration(d)).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> paymentProcessService.approvePayment(currentPayingDTO));
        assertEquals(ExceptionMessageConstants.PAYMENT_SESSION_EXPIRED, exception.getMessage());
    }

    @Test
    public void test_when_there_are_other_users_left_to_pay() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        Driving d = new Driving();
        d.setId(1L);
        d.setDrivingState(DrivingState.PAYMENT);
        p.setCurrentDriving(d);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);
        when(paymentValidationService.validateForPaymentSessionExpiration(d)).thenReturn(true);
        when(paymentValidationService.validateForPassengerPaymentApproval(p,currentPayingDTO)).thenReturn(true);
        when(drivingService.allPassengersApprovedDriving(1L)).thenReturn(false);

        boolean result = paymentProcessService.approvePayment(currentPayingDTO);
        assertTrue(result);
        verifyNoInteractions(paymentTransferService);
    }

    @Test
    public void test_when_all_passenger_approved_to_pay_successfully() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        Driving d = new Driving();
        d.setId(1L);
        d.setLocked(false);
        d.setDrivingState(DrivingState.PAYMENT);
        p.setCurrentDriving(d);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);
        when(paymentValidationService.validateForPaymentSessionExpiration(d)).thenReturn(true);
        when(paymentValidationService.validateForPassengerPaymentApproval(p,currentPayingDTO)).thenReturn(true);
        when(drivingService.allPassengersApprovedDriving(1L)).thenReturn(true);
        when(paymentTransferService.payForAllPassengers(any())).thenReturn(true);

        boolean result = paymentProcessService.approvePayment(currentPayingDTO);
        assertTrue(result);

        verify(paymentTransferService).payForAllPassengers(any());
        verify(afterPaymentService).onSuccess(any());
        verify(afterPaymentService, times(0)).onFailure(any());
    }

    @Test
    public void test_when_all_passenger_approved_to_pay_failure() {
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        Passenger p = new Passenger();
        Driving d = new Driving();
        d.setId(1L);
        d.setLocked(false);
        d.setDrivingState(DrivingState.PAYMENT);
        p.setCurrentDriving(d);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(p);
        when(paymentValidationService.validateForPaymentSessionExpiration(d)).thenReturn(true);
        when(paymentValidationService.validateForPassengerPaymentApproval(p,currentPayingDTO)).thenReturn(true);
        when(drivingService.allPassengersApprovedDriving(anyLong())).thenReturn(true);
        when(paymentTransferService.payForAllPassengers(any())).thenReturn(false);

        boolean result = paymentProcessService.approvePayment(currentPayingDTO);
        assertFalse(result);
        verify(afterPaymentService).onFailure(any());
        verify(afterPaymentService, times(0)).onSuccess(any());
    }

}
