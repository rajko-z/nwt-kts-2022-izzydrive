import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import {PassengerService} from "../../services/passengerService/passenger.service";

@Component({
  selector: 'app-report-driver-check',
  templateUrl: './report-driver-check.component.html',
  styleUrls: ['./report-driver-check.component.scss']
})
export class ReportDriverCheckComponent implements OnInit {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data,
    private passengerService: PassengerService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  yesClick() {
    this.passengerService.reportDriver()
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
