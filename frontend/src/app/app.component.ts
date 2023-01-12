import {Component} from '@angular/core';
import {UserSeviceService} from './services/userService/user-sevice.service';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MatSnackBar} from "@angular/material/snack-bar";
import {NotificationM} from "./model/notifications/notification";

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
