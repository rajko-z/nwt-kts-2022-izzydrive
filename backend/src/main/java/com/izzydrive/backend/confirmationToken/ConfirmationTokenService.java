package com.izzydrive.backend.confirmationToken;

import com.izzydrive.backend.model.users.User;

public interface ConfirmationTokenService {
    void verify(String token) throws Exception;

    void createVerificationToken(User user, String token);
}