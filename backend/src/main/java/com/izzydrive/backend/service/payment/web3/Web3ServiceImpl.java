package com.izzydrive.backend.service.payment.web3;

import com.izzydrive.backend.utils.Web3Constants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class Web3ServiceImpl implements Web3Service {

    private static final Logger LOG = LoggerFactory.getLogger(Web3ServiceImpl.class);

    private final Web3j web3j;

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

    @Override
    public BigDecimal convertRSDToEth(BigDecimal amount) {
        return amount.multiply(Web3Constants.ETH_IN_ONE_RSD);
    }

    @Override
    public BigDecimal getBalanceFromEthAddress(String address) throws ExecutionException, InterruptedException, TimeoutException {
        EthGetBalance balanceResponse = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get(20, TimeUnit.SECONDS);
        BigInteger unscaledBalance = balanceResponse.getBalance();
        return new BigDecimal(unscaledBalance)
                .divide(Web3Constants.WEI_IN_ETH, 18, RoundingMode.HALF_DOWN);
    }

    @Override
    public boolean pay(String mySecretKey, BigDecimal value) throws ExecutionException, InterruptedException, TimeoutException {
        Credentials credentials = Credentials.create(mySecretKey);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        Transfer transfer = new Transfer(web3j, transactionManager);
        TransactionReceipt transactionReceipt = transfer.sendFunds(
                Web3Constants.IZZY_DRIVE_ETH_ADDRESS,
                value,
                Convert.Unit.ETHER,
                Web3Constants.GAS_PRICE,
                Web3Constants.GAS_LIMIT
            ).sendAsync().get(180, TimeUnit.SECONDS);
        if (transactionReceipt != null) {
            LOG.info("Tx hash: " + transactionReceipt.getTransactionHash());
            return true;
        }
        return false;
    }
}
