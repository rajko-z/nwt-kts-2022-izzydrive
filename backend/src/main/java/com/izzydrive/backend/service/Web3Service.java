package com.izzydrive.backend.service;

public interface Web3Service {

    boolean isValidETHAddress(String address);

    boolean isValidSecretKey(String key);

    boolean addressMatchSecretKey(String key, String address);
}
