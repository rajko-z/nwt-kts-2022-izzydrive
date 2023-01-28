import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {DrivingService} from "../../../services/drivingService/driving.service";

@Component({
  selector: 'app-payment-reservation',
  templateUrl: './payment-reservation.component.html',
  styleUrls: ['./payment-reservation.component.scss']
})
export class PaymentReservationComponent implements OnInit {
  minute: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private router: Router,
              private drivingService: DrivingService,
              private snackbar: MatSnackBar) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  ngOnInit(): void {
  }

  openPaymentPageClick() {
    this.router.navigateByUrl('passenger/payment');
    this.data.preClose();
  }

  cancelRideClick() {
    //delete reservation
    this.drivingService.cancelReservation(this.data.message.drivingId).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/passenger');
        // this.snackbar.open(response.text, "OK")
      },
      error: (error) => {
        this.snackbar.open(error.error.message, "OK")
      }
    })
    this.data.preClose();
  }

}
