import {Component, OnInit} from '@angular/core';
import {UserService} from './services/userService/user-sevice.service';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MatSnackBar} from "@angular/material/snack-bar";
import firebase from 'firebase/compat/app';
import {environment} from "../environments/environment";
import {NotificationService} from "./services/notificationService/notification.service";
import {Role} from './model/user/role';
import {ChatService} from './services/chat/chat.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'NWT-KTS 2022 IZZYDRIVE';

  private stompClient: any;

  constructor(
    private userService: UserService,
    public snackBar: MatSnackBar,
    private chatService: ChatService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    firebase.initializeApp(environment.firebaseConfig);
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.listenForMessages();
  }

  listenForMessages() {
    this.chatService.listenForNewMessages();
  }

  isUserLoggedIn(): boolean {
    return this.userService.isUserLoggedIn();
  }

  isAdminLoggedIn(): boolean {
    return this.userService.getRoleCurrentUserRole() == Role.ROLE_ADMIN.toString();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(environment.socket);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.notificationService.showNotificationCancelRideDriver(this.stompClient);
    this.notificationService.showNotificationNewRide(this.stompClient);
    this.notificationService.showNotificationCancelRide(this.stompClient);
    this.notificationService.showNotificationNewReservationDriving(this.stompClient);
    this.notificationService.showNotificationPaymentSessionExpired(this.stompClient);
    this.notificationService.showNotificationPaymentSuccess(this.stompClient);
    this.notificationService.showNotificationPaymentFailure(this.stompClient);
    this.notificationService.showNotificationCancelReservationDriver(this.stompClient);
    this.notificationService.showNotificationNewRideDriver(this.stompClient);
    this.notificationService.showNotificationDriverArrivedAtStart(this.stompClient);
    this.notificationService.showNotificationReportedDriver(this.stompClient);
  }
}
