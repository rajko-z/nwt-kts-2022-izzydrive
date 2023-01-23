import {Component, Inject, OnInit} from '@angular/core';
import {MatSnackBar, MAT_SNACK_BAR_DATA} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {DrivingService} from 'src/app/services/drivingService/driving.service';

@Component({
  selector: 'app-confirm-cancel-reservation',
  templateUrl: './confirm-cancel-reservation.component.html',
  styleUrls: ['./confirm-cancel-reservation.component.scss']
})
export class ConfirmCancelReservationComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data, private router: Router, private snackbar: MatSnackBar, private drivingService: DrivingService) {
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
        this.snackbar.open(response.text, "OK")
      },
      error: (error) => {
        this.snackbar.open(error.error.message, "OK")
      }
    })
  }
}
