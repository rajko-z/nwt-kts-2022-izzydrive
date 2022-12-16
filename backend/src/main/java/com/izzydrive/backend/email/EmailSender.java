package com.izzydrive.backend.email;

public interface EmailSender {

    void sendConfirmationAsync(String email, String token, String firstName);

    void sendDriverRegistrationMail(String email, String password);
}
