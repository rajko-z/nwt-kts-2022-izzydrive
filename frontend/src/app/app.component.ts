
import {Component} from '@angular/core';
import {UserSeviceService} from './services/userService/user-sevice.service';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MatSnackBar} from "@angular/material/snack-bar";
import {NotificationM} from "./model/notifications/notification";
// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import  firebase from 'firebase/compat/app';
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAKK1keZb45wDnBvvsR5l6wk201kIZa1sE",
  authDomain: "izzydrive-368421.firebaseapp.com",
  projectId: "izzydrive-368421",
  storageBucket: "izzydrive-368421.appspot.com",
  messagingSenderId: "83743871907",
  appId: "1:83743871907:web:7e0f485634423a89aac016",
  measurementId: "G-F2L4CREP99",
  databaseURL: "https://izzydrive-368421-default-rtdb.europe-west1.firebasedatabase.app"
};

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

  constructor(private userService: UserSeviceService, public snackBar: MatSnackBar) {
    firebase.initializeApp(firebaseConfig);
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

  isUserLoggedIn(): boolean {
    return this.userService.isUserLoggedIn();
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
