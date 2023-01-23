import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {UserService} from "../../../services/userService/user-sevice.service";

@Component({
  selector: 'app-new-reservation',
  templateUrl: './new-reservation.component.html',
  styleUrls: ['./new-reservation.component.scss']
})
export class NewReservationComponent implements OnInit {

  time: string;
  user: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private router: Router,
              private drivingService: DrivingService,
              private userService: UserService) {
    this.user = userService.getRoleCurrentUserRole();
    const date = new Date(data.message.reservationTime);
    this.time = `${date.getHours()}:${date.getMinutes()}  ${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}.`
  }

  ngOnInit(): void {
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
    //reject reservation
    this.drivingService.cancelReservation(this.data.drivingId).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/passenger');
        // this.snackbar.open(response.text, "OK")
      },
      error: (error) => {
        // this.snackbar.open(error.error.message, "OK")
      }
    })
    this.data.preClose();
  }
}
