package com.izzydrive.backend.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Web3Constants {

    private Web3Constants() {}
    public static final String IZZY_DRIVE_ETH_ADDRESS = "0x8928447a9b14D7E7C1920D5fAa18b4988d6452a5";
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    public static final BigDecimal ETH_IN_ONE_RSD = BigDecimal.valueOf(0.000006);
    public static final BigDecimal WEI_IN_ETH = BigDecimal.valueOf(1000000000000000000L);
}
