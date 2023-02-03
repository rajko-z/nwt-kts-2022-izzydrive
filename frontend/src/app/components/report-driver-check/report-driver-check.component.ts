import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from "@angular/material/snack-bar";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import {PassengerService} from "../../services/passengerService/passenger.service";

@Component({
  selector: 'app-report-driver-check',
  templateUrl: './report-driver-check.component.html',
  styleUrls: ['./report-driver-check.component.scss']
})
export class ReportDriverCheckComponent {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data,
    private passengerService: PassengerService,
    private responseMessage: ResponseMessageService) { }
  yesClick() {
    this.passengerService.reportDriver()
      .subscribe({
          next: (response) => {
            this.responseMessage.openSuccessMessage(response.text)
          },
          error: (error) => {
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
    this.data.preClose();
  }

  noClick() {
    this.data.preClose();
  }
}
