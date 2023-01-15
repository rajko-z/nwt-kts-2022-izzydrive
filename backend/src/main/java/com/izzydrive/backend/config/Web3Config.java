package com.izzydrive.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

@Configuration
@Getter
public class Web3Config {

    @Value("${infura-node.url}")
    private String nodeUrl;
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
}
