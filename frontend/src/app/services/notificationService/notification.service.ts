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
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    public snackBar: MatSnackBar,
    private userService: UserService,
    private router: Router,
    ) {}

  showNotificationComponent(message: string, component) {
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

  showNotificationText(message: string, callbackFn = null) {
    const notification: NotificationM = JSON.parse(message);
    if (notification.userEmail === this.userService.getCurrentUserEmail()) {
      this.snackBar.open(notification.message, "OK", {
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      });
      if (callbackFn !== null) {
        callbackFn();
      }
    }
  }

  showNotificationNewRide(stompClient) {
    stompClient.subscribe('/notification/newRide', (message: { body: string }) => {
        this.showNotificationComponent(message.body, NewRideLinkedUserComponent);
      }
    );
  }

  showNotificationCancelRide(stompClient) {
    stompClient.subscribe('/notification/cancelRide', (message: { body: string }) => {
        this.showNotificationComponent(message.body, DeniedRideLinkedUserComponent);
      }
    );
  }

  showNotificationNewReservationDriving(stompClient) {
    stompClient.subscribe('/notification/newReservationDriving', (message: { body: string }) => {
        this.showNotificationComponent(message.body, NewReservationComponent);
      }
    );
  }

  showNotificationCancelRideDriver(stompClient) {
    stompClient.subscribe('/notification/cancelRideDriver', (message: { body: string }) => {
      this.showNotificationText(message.body);
    });
  }

  showNotificationPaymentSessionExpired(stompClient) {
    let callBackFn = () => {
      if (this.router.url === '/passenger/payment') {
        this.router.navigateByUrl('/passenger/order-now');
      }
    }
    stompClient.subscribe('/notification/paymentSessionExpired', (message: { body: string }) => {
      this.showNotificationText(message.body, callBackFn);
    });
  }

  showNotificationPaymentSuccess(stompClient) {
    let callBackFn = () => {
      this.router.navigateByUrl('/passenger/current-driving');
    }
    stompClient.subscribe('/notification/paymentSuccess', (message: { body: string }) => {
      this.showNotificationText(message.body, callBackFn);
    });
  }

  showNotificationPaymentFailure(stompClient) {
    let callBackFn = () => {
      if (this.router.url === '/passenger/payment') {
        this.router.navigateByUrl('/passenger/order-now');
      }
    }
    stompClient.subscribe('/notification/paymentFailure', (message: { body: string }) => {
      this.showNotificationText(message.body, callBackFn);
    });
  }
  
  showNotificationCancelReservationDriver(stompClient) {
    stompClient.subscribe('/notification/cancelReservation', (message: { body: string }) => {
      console.log(message);
      this.showNotificationComponent(message.body, DeniedRideLinkedUserComponent);
    }
  );
  }
}
