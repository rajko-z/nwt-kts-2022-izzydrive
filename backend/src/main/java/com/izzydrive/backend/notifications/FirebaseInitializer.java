package com.izzydrive.backend.notifications;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("izzydrive-29e02-firebase-adminsdk-duajx-065d30a191.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
        return FirebaseMessaging.getInstance(app);
    }
//    @Bean
//    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
//
//    @Bean
//    FirebaseApp firebaseApp(GoogleCredentials credentials) {
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }
//    @Bean
//    GoogleCredentials googleCredentials() throws IOException {
//        if (firebaseProperties.getServiceAccount() != null) {
//            try (InputStream is = firebaseProperties.getServiceAccount().getInputStream()) {
//                return GoogleCredentials.fromStream(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            // Use standard credentials chain. Useful when running inside GKE
//            return GoogleCredentials.getApplicationDefault();
//        }
//    }
}