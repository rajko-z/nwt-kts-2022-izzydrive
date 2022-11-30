package com.izzydrive.backend.confirmationToken;
import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void verify(String token) throws Exception {
        Optional<ConfirmationToken> verificationTokenOpt = confirmationTokenRepository.findFirstByToken(token);
        if (verificationTokenOpt.isEmpty()) {
            throw new Exception("Invalid verification token!");
        }

        ConfirmationToken verificationToken = verificationTokenOpt.get();
        MyUser user = verificationToken.getUser();
        if (user.isEnabled()) {
            throw new Exception("E-Mail has already been verified!");
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new Exception("Your token has expired!");
        }

        user.setActivated(true);
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(MyUser user, String token) {
        ConfirmationToken verificationToken = new ConfirmationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        confirmationTokenRepository.save(verificationToken);
    }
}