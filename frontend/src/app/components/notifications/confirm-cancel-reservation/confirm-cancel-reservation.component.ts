import {Component, Inject, OnInit} from '@angular/core';
import {MatSnackBar, MAT_SNACK_BAR_DATA} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {DrivingService} from 'src/app/services/drivingService/driving.service';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-confirm-cancel-reservation',
  templateUrl: './confirm-cancel-reservation.component.html',
  styleUrls: ['./confirm-cancel-reservation.component.scss']
})
export class ConfirmCancelReservationComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data, 
              private router: Router, 
              private drivingService: DrivingService,
              private responseMessage: ResponseMessageService) {
              }

  ngOnInit(): void {
  }

  onCancel() {
    this.data.preClose();
  }

  onConfirm() {
    this.data.preClose();
    this.drivingService.cancelReservation(this.data.drivingId).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/passenger');
        this.responseMessage.openSuccessMessage(response.text)
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }
}
