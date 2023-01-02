package com.izzydrive.backend.service.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.izzydrive.backend.controller.NotificationsController;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;

//    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
//        this.firebaseMessaging = firebaseMessaging;
//    }


    public String sendNotification(String token) throws FirebaseMessagingException {
        System.out.println("da");

        Notification notification = Notification
                .builder()
                .setTitle("proba naslov")
                .setBody("ovo je telo")
                .build();

//        Message message = Message
//                .builder()
//                .setToken(token)
//                .setNotification(notification)
//                .putAllData(note.getData())
//                .build();

        Message msg = Message.builder()
                .setTopic("nesto")
                .putData("body", "some data")
                .setNotification(notification)
                .build();

        return firebaseMessaging.send(msg);
    }

}
