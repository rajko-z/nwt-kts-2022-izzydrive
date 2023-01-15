package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.service.Web3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

@Service
@AllArgsConstructor
public class Web3ServiceImpl implements Web3Service {

    @Override
    public boolean isValidETHAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }

    @Override
    public boolean isValidSecretKey(String key) {
        return WalletUtils.isValidPrivateKey(key);
    }

    @Override
    public boolean addressMatchSecretKey(String key, String address) {
        Credentials cs = Credentials.create(key);
        String secretKey = cs.getEcKeyPair().getPrivateKey().toString(16).toLowerCase();
        String addr = cs.getAddress().toLowerCase();
        return key.toLowerCase().equals(secretKey) && address.toLowerCase().equals(addr);
    }
}
