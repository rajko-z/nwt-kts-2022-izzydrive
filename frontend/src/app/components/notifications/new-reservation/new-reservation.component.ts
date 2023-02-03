import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';
import {DrivingService} from "../../../services/drivingService/driving.service";
import {UserService} from "../../../services/userService/user-sevice.service";

@Component({
  selector: 'app-new-reservation',
  templateUrl: './new-reservation.component.html',
  styleUrls: ['./new-reservation.component.scss']
})
export class NewReservationComponent {

  time: string;
  user: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private router: Router,
              private drivingService: DrivingService,
              private userService: UserService,
              private snackbar: MatSnackBar,
              private responseMessage: ResponseMessageService) {
    this.user = userService.getRoleCurrentUserRole();
    this.time = data.message.reservationTime;
  }
  okClick() {
    if (this.user === 'ROLE_DRIVER') {
      this.router.navigateByUrl('/driver');
    } else if (this.user === 'ROLE_PASSENGER') {
      this.router.navigateByUrl('/passenger');
    }
    this.data.preClose();
  }

  cancelReservationClick() {
    this.drivingService.cancelReservation(this.data.message.drivingId).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/passenger/order-now');
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
    this.data.preClose();
  }
}
