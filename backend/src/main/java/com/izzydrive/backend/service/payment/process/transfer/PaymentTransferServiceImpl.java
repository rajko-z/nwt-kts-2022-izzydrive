package com.izzydrive.backend.service.payment.process.transfer;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.payment.web3.Web3Service;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class PaymentTransferServiceImpl implements PaymentTransferService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentTransferServiceImpl.class);

    private final Web3Service web3Service;

    @Override
    public boolean payForAllPassengers(Driving driving) {
        if (!everyPassengerHaveEnoughOnBalance(driving)) {
            return false;
        }
        BigDecimal priceToPay = getPriceInEthPassengerHasToPay(driving);

        for (Passenger p : driving.getPassengers()) {
            String sk = p.isPayingUsingExistingInfo() ? p.getSecretKey() : p.getOnceTimeSecretKey();
            try {
                if (!web3Service.pay(sk, priceToPay)) {
                    return false;
                }
            } catch (ExecutionException | TimeoutException e) {
                LOG.error("From payment process timeout or execution: " + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOG.error("From payment process interrupt: " + e.getMessage());
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    private BigDecimal getPriceInEthPassengerHasToPay(Driving driving) {
        BigDecimal priceInRSD = BigDecimal.valueOf(driving.getPrice() / driving.getPassengers().size());
        return this.web3Service.convertRSDToEth(priceInRSD);
    }

    private boolean everyPassengerHaveEnoughOnBalance(Driving driving) {
        Set<Passenger> passengers = driving.getPassengers();
        BigDecimal priceToPay = getPriceInEthPassengerHasToPay(driving);
        BigDecimal minimumPriceToHaveOnBalance = priceToPay.add(new BigDecimal("0.01"));

        for (Passenger p : passengers) {
            String passengerAddress = p.isPayingUsingExistingInfo() ?
                    p.getEthAddress() : p.getOnceTimeEthAddress();

            try {
                BigDecimal balance = web3Service.getBalanceFromEthAddress(passengerAddress);
                if (balance.compareTo(minimumPriceToHaveOnBalance) < 0) {
                    return false;
                }
            } catch (ExecutionException | TimeoutException e) {
                LOG.error("From getbalance:" + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOG.error("From getbalance:" + e.getMessage());
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }
}
