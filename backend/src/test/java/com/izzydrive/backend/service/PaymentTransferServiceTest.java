package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.payment.process.transfer.PaymentTransferServiceImpl;
import com.izzydrive.backend.service.payment.web3.Web3ServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentTransferServiceTest {

    @InjectMocks
    private PaymentTransferServiceImpl paymentTransferService;

    @Mock
    private Web3ServiceImpl web3Service;

    @Test
    public void test_paying_for_all_passengers_when_there_is_not_enough_on_balance() throws ExecutionException, InterruptedException, TimeoutException {
        Driving driving = new Driving();
        driving.setPrice(1000);

        Set<Passenger> passengers = new HashSet<>();
        passengers.add(getMockPassenger());
        passengers.add(getMockPassenger());
        driving.setPassengers(passengers);

        BigDecimal priceInEth = new BigDecimal("10000000.0");
        when(web3Service.convertRSDToEth(any())).thenReturn(priceInEth);

        BigDecimal balance = new BigDecimal("100.0");
        when(web3Service.getBalanceFromEthAddress(anyString())).thenReturn(balance);

        boolean result = paymentTransferService.payForAllPassengers(driving);
        assertFalse(result);
    }


    @Test
    public void test_paying_for_all_passengers_successfully() throws ExecutionException, InterruptedException, TimeoutException {
        Driving driving = new Driving();
        driving.setPrice(1000);

        Set<Passenger> passengers = new HashSet<>();
        passengers.add(getMockPassenger());
        passengers.add(getMockPassenger());
        driving.setPassengers(passengers);

        BigDecimal priceInEth = new BigDecimal("1.0");
        when(web3Service.convertRSDToEth(any())).thenReturn(priceInEth);

        BigDecimal balance = new BigDecimal("100000.0");
        when(web3Service.getBalanceFromEthAddress(anyString())).thenReturn(balance);

        when(web3Service.pay(anyString(), any())).thenReturn(true);

        boolean result = paymentTransferService.payForAllPassengers(driving);
        assertTrue(result);
    }

    private Passenger getMockPassenger() {
        Passenger p1 = new Passenger();
        p1.setEmail("mika");
        p1.setPayingUsingExistingInfo(true);
        p1.setEthAddress("address1");
        p1.setSecretKey("secret1");
        return p1;
    }

}
