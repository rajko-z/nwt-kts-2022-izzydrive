import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
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
              private responseMessage: ResponseMessageService) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  ngOnInit(): void {
  }

  openPaymentPageClick() {
    this.router.navigateByUrl('passenger/payment');
    this.data.preClose();
  }

  cancelRideClick() {
    this.drivingService.cancelReservation(this.data.message.drivingId).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/passenger');
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
    this.data.preClose();
  }

}
