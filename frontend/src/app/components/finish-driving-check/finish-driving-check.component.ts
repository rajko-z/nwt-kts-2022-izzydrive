import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';
import {DrivingService} from "../../services/drivingService/driving.service";

@Component({
  selector: 'app-finish-driving-check',
  templateUrl: './finish-driving-check.component.html',
  styleUrls: ['./finish-driving-check.component.scss']
})
export class FinishDrivingCheckComponent {

  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data,
    private drivingService: DrivingService,
    private responseMessage: ResponseMessageService) { }
  yesClick() {
    this.drivingService.endDriving()
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
