package com.izzydrive.backend.confirmationToken;

import com.izzydrive.backend.dto.TextResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/confirmation")
@AllArgsConstructor
public class ConfirmationTokenController {

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public ResponseEntity<?> verify(@RequestParam String token) {
        confirmationTokenService.verify(token);
        return new ResponseEntity<>("Verified", HttpStatus.OK);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> verifyResetPasswordLink(@RequestParam String token) {
        confirmationTokenService.verifyResetPasswordLink(token);
        return new ResponseEntity<>(new TextResponse("Verified"), HttpStatus.OK);
    }
}