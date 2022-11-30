package com.izzydrive.backend.confirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/confirmation")
public class ConfirmationTokenController {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping()
    public ResponseEntity<?> verify(@RequestParam String token) {
        try {
            confirmationTokenService.verify(token);
            return new ResponseEntity<>("Verified", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}