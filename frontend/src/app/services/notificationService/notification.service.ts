import {Injectable} from '@angular/core';
import {NotificationM} from "../../model/notifications/notification";
import {
  NewRideLinkedUserComponent
} from "../../components/notifications/new-ride-linked-user/new-ride-linked-user.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserService} from "../userService/user-sevice.service";
import {
  DeniedRideLinkedUserComponent
} from "../../components/notifications/denied-ride-linked-user/denied-ride-linked-user.component";
import {NewReservationComponent} from "../../components/notifications/new-reservation/new-reservation.component";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(public snackBar: MatSnackBar, private userService: UserService) {
  }

  showNotification(message: string, component) {
    const notification: NotificationM = JSON.parse(message);
    if (notification.userEmail === this.userService.getCurrentUserEmail()) {
      this.snackBar.openFromComponent(component, {
        data: {
          message: notification,
          preClose: () => {
            this.snackBar.dismiss()
          }
        },
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      });
    }
  }

  showNotificationNewRide(stompClient) {
    stompClient.subscribe('/notification/newRide', (message: { body: string }) => {
        this.showNotification(message.body, NewRideLinkedUserComponent);
      }
    );
  }

  showNotificationCancelRide(stompClient) {
    stompClient.subscribe('/notification/cancelRide', (message: { body: string }) => {
        this.showNotification(message.body, DeniedRideLinkedUserComponent);
      }
    );
  }

  showNotificationNewReservationDriving(stompClient) {
    stompClient.subscribe('/notification/newReservationDriving', (message: { body: string }) => {
        this.showNotification(message.body, NewReservationComponent);
      }
    );
  }

  showNotificationCancelRideDriver(stompClient) {
    stompClient.subscribe('/notification/cancelRideDriver', (message: { body: string }) => {
      let notification: NotificationM = JSON.parse(message.body);
      if (notification.userEmail === this.userService.getCurrentUserEmail()) {
        this.snackBar.open(notification.message, "OK");
      }
    });
  }
}
