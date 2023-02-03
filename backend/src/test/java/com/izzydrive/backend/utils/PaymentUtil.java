package com.izzydrive.backend.utils;

import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.dto.payment.KeyPairDTO;

public class PaymentUtil {

    public static CurrentPayingDTO getExistingCurrPaymentInfo() {
        return new CurrentPayingDTO(true, null);
    }

    public static CurrentPayingDTO getWithoutExistingWithInvalidPrivateKey() {
        return new CurrentPayingDTO(false, new KeyPairDTO("54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799323fds","0x0739eb5cb09cB2acCf324eE434cFF7805d90A168"));
    }

    public static CurrentPayingDTO getWithoutExistingWithInvalidPublicKey() {
        return new CurrentPayingDTO(false, new KeyPairDTO("54b1aaea6ae9f74b3c280f391ca7e179322e178d472240b44ce7ab9444ef9799", "0x0739eb5cb09cB2acCf324eE43fdsfsgff4cFF7805d90A168"));
    }

    public static CurrentPayingDTO getWithoutExistingWithInvalidKeyPairCombination() {
        return new CurrentPayingDTO(false, new KeyPairDTO("d533521eb0cc70a0660a91cedeec92fcc919f416063d0d2be1d99d58d6140929","0x0739eb5cb09cB2acCf324eE434cFF7805d90A168"));
    }

    public static CurrentPayingDTO getWithoutExistingWithNotEnoughFounds() {
        return new CurrentPayingDTO(false, new KeyPairDTO("cb152afdf9004fc8d60a3cbcc96dbcd53bd31425479655624f943e97150c7123","0xd92E11030327cE455D12116c0328134AA32DBE8e"));
    }
}
