package com.izzydrive.backend.confirmationToken;

import com.izzydrive.backend.model.users.User;

public interface ConfirmationTokenService {
    void verify(String token);

    void verifyResetPasswordLink(String token);

    void createVerificationToken(User user, String token);

    ConfirmationToken fingByToken(String token);
}