package com.izzydrive.backend.confirmationToken;

import com.izzydrive.backend.model.users.MyUser;

public interface ConfirmationTokenService {
    void verify(String token) throws Exception;

    void createVerificationToken(MyUser user, String token);
}