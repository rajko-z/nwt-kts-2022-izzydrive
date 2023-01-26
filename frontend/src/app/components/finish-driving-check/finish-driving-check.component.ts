import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../services/drivingService/driving.service";

@Component({
  selector: 'app-finish-driving-check',
  templateUrl: './finish-driving-check.component.html',
  styleUrls: ['./finish-driving-check.component.scss']
})
export class FinishDrivingCheckComponent implements OnInit {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data,
    private drivingService: DrivingService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  yesClick() {
    this.drivingService.endDriving()
      .subscribe({
          next: (response) => {
            this.snackBar.open(response.text, "OK", {
              duration: 5000,
            })
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
    this.data.preClose();
  }

  noClick() {
    this.data.preClose();
  }

}
