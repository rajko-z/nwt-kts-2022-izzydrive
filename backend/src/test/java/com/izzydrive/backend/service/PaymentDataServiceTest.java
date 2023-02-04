package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.dto.payment.PaymentStatusDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.validation.DrivingValidationServiceImpl;
import com.izzydrive.backend.service.payment.data.PaymentDataServiceImpl;
import com.izzydrive.backend.service.payment.web3.Web3ServiceImpl;
import com.izzydrive.backend.service.users.passenger.PassengerServiceImpl;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentDataServiceTest {

    @InjectMocks
    private PaymentDataServiceImpl paymentDataService;

    @Mock
    private PassengerServiceImpl passengerService;

    @Mock
    private DrivingValidationServiceImpl drivingValidationService;

    @Mock
    private Web3ServiceImpl web3Service;

    @Test
    public void test_getting_payment_status_for_logged_passenger() {
        Passenger passenger = new Passenger();
        passenger.setApprovedPaying(true);
        passenger.setEmail("riki");

        Driving driving = new Driving();
        driving.setPrice(1000);
        driving.setDrivingState(DrivingState.PAYMENT);
        Set<Passenger> passengers = new HashSet<>();
        Passenger p1 = new Passenger();
        p1.setEmail("mika");
        Passenger p2 = new Passenger();
        p1.setEmail("zika");
        passengers.add(p1);
        passengers.add(p2);
        driving.setPassengers(passengers);
        passenger.setCurrentDriving(driving);

        BigDecimal priceInEth = new BigDecimal("1");
        when(web3Service.convertRSDToEth(any())).thenReturn(priceInEth);
        when(drivingValidationService.drivingExpiredForPayment(any())).thenReturn(false);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(passenger);

        PaymentStatusDTO paymentStatusDTO = paymentDataService.getPaymentStatusForLoggedPassenger();
        assertFalse(paymentStatusDTO.isSessionExpired());
        assertTrue(paymentStatusDTO.isPassengerApproved());
        assertTrue(paymentStatusDTO.isWaitingForOthers());
        assertEquals(1, paymentStatusDTO.getPriceInETH());
        assertEquals(500, paymentStatusDTO.getPriceInRSD());
    }

    @Test
    public void should_throw_invalid_secret_key() {
        KeyPairDTO keyPairDTO = new KeyPairDTO("secret", "ethAddress");
        when(web3Service.isValidSecretKey("secret")).thenReturn(false);
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentDataService.validatePaymentKeyPair(keyPairDTO));
        assertEquals(ExceptionMessageConstants.ERROR_INVALID_SECRET_KEY, exception.getMessage());
    }

    @Test
    public void should_throw_invalid_eht_address() {
        KeyPairDTO keyPairDTO = new KeyPairDTO("secret", "ethAddress");
        when(web3Service.isValidSecretKey("secret")).thenReturn(true);
        when(web3Service.isValidETHAddress("ethAddress")).thenReturn(false);
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentDataService.validatePaymentKeyPair(keyPairDTO));
        assertEquals(ExceptionMessageConstants.ERROR_INVALID_ETH_ADDRESS, exception.getMessage());
    }

    @Test
    public void should_throw_eth_address_does_not_match_secret_key() {
        KeyPairDTO keyPairDTO = new KeyPairDTO("secret", "ethAddress");
        when(web3Service.isValidSecretKey("secret")).thenReturn(true);
        when(web3Service.isValidETHAddress("ethAddress")).thenReturn(true);
        when(web3Service.addressMatchSecretKey("secret", "ethAddress")).thenReturn(false);
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentDataService.validatePaymentKeyPair(keyPairDTO));
        assertEquals(ExceptionMessageConstants.ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY, exception.getMessage());
    }


}
