package com.izzydrive.backend.confirmationToken;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE;

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
        User user = verificationToken.getUser();
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
    public void createVerificationToken(User user, String token) {
        ConfirmationToken verificationToken = new ConfirmationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        try{
            confirmationTokenRepository.save(verificationToken);
        }
        catch(Exception exception) {
            throw new BadRequestException(AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE);
        }

    }
}