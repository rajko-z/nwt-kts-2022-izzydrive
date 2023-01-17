package com.izzydrive.backend.paymentTest;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PaymentProba {

    private static final String INFURA_NODE = "https://goerli.infura.io/v3/1b32d2c4d2b84713a10a114e518eb465";

    private static final String SEPOLIA = "https://sepolia.infura.io/v3/ee1a6a2511724531a7440ca91b7ffabc";
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private static final String RECIPIENT = "0x66CEEb689fdC7837b4351C73406fB435904169c2";
    private static final String SK = "856fa3e9eb2e0189d499c08b94d716b43760e73494eae45de273e756cb5194b5";

    public static void main(String[] args) {
        new PaymentProba();
    }

    private PaymentProba() {
        Web3j web3j = Web3j.build(new HttpService(SEPOLIA));
        TransactionManager transactionManager =
                new RawTransactionManager(web3j, getCredentialsFromMnemonic());
        Transfer transfer = new Transfer(web3j, transactionManager);

        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = transfer.sendFunds(
                RECIPIENT,
                BigDecimal.valueOf(0.005),
                Convert.Unit.ETHER,
                GAS_PRICE,
                GAS_LIMIT
            ).send();
            System.out.println(transactionReceipt.getTransactionHash());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Credentials getCredentialsFromMnemonic() {
        return Credentials.create(SK);
    }
}
