package com.izzydrive.backend.service.payment.web3;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Web3Service {

    boolean isValidETHAddress(String address);

    boolean isValidSecretKey(String key);

    boolean addressMatchSecretKey(String key, String address);

    BigDecimal convertRSDToEth(BigDecimal amount);

    BigDecimal getBalanceFromEthAddress(String address) throws ExecutionException, InterruptedException, TimeoutException;

    boolean pay(String mySecretKey, BigDecimal value) throws ExecutionException, InterruptedException, TimeoutException;
}
