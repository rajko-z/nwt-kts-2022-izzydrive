package com.izzydrive.backend.confirmationToken;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.ForbiddenAccessException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final UserRepository userRepository;

    @Override
    public void verify(String token) {
        Optional<ConfirmationToken> verificationTokenOpt = confirmationTokenRepository.findFirstByToken(token);
        if (verificationTokenOpt.isEmpty()) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.INVALID_VERIFICATION_TOKEN);
        }

        ConfirmationToken verificationToken = verificationTokenOpt.get();
        User user = verificationToken.getUser();
        if (user.isEnabled()) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.EMAIL_HAS_ALREADY_BEEN_VERIFIED);
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.VERIFICATION_TOKEN_HAS_EXPIRED);
        }

        user.setActivated(true);
        userRepository.save(user);
    }

    @Override
    public void verifyResetPasswordLink(String token) {
        Optional<ConfirmationToken> verificationTokenOpt = confirmationTokenRepository.findFirstByToken(token);
        if (verificationTokenOpt.isEmpty()) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.INVALID_VERIFICATION_TOKEN);
        }

        ConfirmationToken verificationToken = verificationTokenOpt.get();

        if (verificationToken.isVerified()) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.ALREADY_USED_THIS_LINK);
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ForbiddenAccessException(ExceptionMessageConstants.VERIFICATION_TOKEN_HAS_EXPIRED);
        }

        verificationToken.setVerified(true);
        confirmationTokenRepository.save(verificationToken);
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
            exception.printStackTrace();
            throw new BadRequestException(AlREADY_SEND_REGISTRATION_REQUEST_MESSAGE);
        }
    }

    @Override
    public ConfirmationToken fingByToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findFirstByToken(token).
                orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.INVALID_RESET_PASSWORD_TOKEN));;
        return confirmationToken;
    }
}