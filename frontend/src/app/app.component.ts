import {Component, OnInit} from '@angular/core';
import {UserService} from './services/userService/user-sevice.service';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MatSnackBar} from "@angular/material/snack-bar";
import {NotificationM} from "./model/notifications/notification";
// Import the functions you need from the SDKs you need
import firebase from 'firebase/compat/app';
import {environment} from "../environments/environment";
import { Role } from './model/user/role';
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional


// Initialize Firebase


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'NWT-KTS 2022 IZZYDRIVE';

  

  // isUserLoggedIn: boolean = false;
  private stompClient: any;

  constructor(private userService: UserService, public snackBar: MatSnackBar) {
    firebase.initializeApp(environment.firebaseConfig);
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

  isUserLoggedIn(): boolean {
    return this.userService.isUserLoggedIn();
  }

  isAdminLoggedIn(): boolean {
    return this.userService.getRoleCurrentUserRole() == Role.ROLE_ADMIN.toString();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS('https://localhost:8443/izzydrive/v1/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }
  openGlobalSocket() {
    this.stompClient.subscribe('/notification/init', (message: { body: string }) => {
      let notification: NotificationM = JSON.parse(message.body);
      if (notification.userEmail === this.userService.getCurrentUserEmail()) {
        this.snackBar.open(notification.message, "OK");
      }

    });
  }
}
