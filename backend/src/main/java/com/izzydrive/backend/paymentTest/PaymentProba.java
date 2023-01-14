package com.izzydrive.backend.paymentTest;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;

import java.math.BigInteger;

public class PaymentProba {

    private static final String INFURA_NODE = "https://goerli.infura.io/v3/1b32d2c4d2b84713a10a114e518eb465";
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private static final String RECIPIENT = "0x66CEEb689fdC7837b4351C73406fB435904169c2";
    private static final String SK = "856fa3e9eb2e0189d499c08b94d716b43760e73494eae45de273e756cb5194b5";

    public static void main(String[] args) {
        new PaymentProba();
    }

    private PaymentProba() {
        Web3j web3j = Web3j.build(new HttpService(INFURA_NODE));

        TransactionManager transactionManager =
                new RawTransactionManager(web3j, getCredentialsFromMnemonic());

        Transfer transfer = new Transfer(web3j, transactionManager);

        String privateKey = SK;
        Credentials cs = Credentials.create(privateKey);

        System.out.println(WalletUtils.isValidAddress("0x66CEEb689fdC7837b4351C73406fB435904169c2"));
        System.out.println(WalletUtils.isValidPrivateKey("856fa3e9eb2e0189d499c08b94d716b43760e73494eae45de273e756cb5194b5"));


//        privateKey = cs.getEcKeyPair().getPrivateKey().toString(16);
//        String publicKey = cs.getEcKeyPair().getPublicKey().toString(16);
//        String addr = cs.getAddress();
//
//        System.out.println("Private key: " + privateKey);
//        System.out.println("Public key: " + publicKey);
//        System.out.println("Address: " + addr);


//        TransactionReceipt transactionReceipt = null;
//        try {
//            transactionReceipt = transfer.sendFunds(
//                RECIPIENT,
//                BigDecimal.valueOf(0.05),
//                Convert.Unit.ETHER, // 0.000001
//                GAS_PRICE,
//                GAS_LIMIT
//            ).send();
//            System.out.println(transactionReceipt.getTransactionHash());
//
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
    }

    private Credentials getCredentialsFromMnemonic() {
        return Credentials.create(SK);
    }
}
