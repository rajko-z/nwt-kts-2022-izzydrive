import {Component, Inject} from '@angular/core';
import {DrivingService} from "../../services/drivingService/driving.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../services/userService/user-sevice.service";
import {CancellationReason} from "../../model/driving/cancelationReason";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";
import {TextResponse} from "../../model/response/textresponse";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-explanation-dialog',
  templateUrl: './explanation-dialog.component.html',
  styleUrls: ['./explanation-dialog.component.scss']
})
export class ExplanationDialogComponent {
  explanationForm = new FormGroup({
    explanation: new FormControl('')
  });
  cancellationReason: CancellationReason;

  constructor(private drivingService: DrivingService,
              private userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data,
              private snackBar: MatSnackBar,
              private dialogRef: MatDialogRef<ExplanationDialogComponent>,
              private messageresponse: ResponseMessageService) {
  }

  onSubmit() {
    this.cancellationReason = new CancellationReason();
    this.cancellationReason.drivingId = this.data.drivingId;
    this.cancellationReason.text = this.explanationForm.value.explanation;

    if (this.cancellationReason.text == null || this.cancellationReason?.text.trim() == '') {
      this.messageresponse.openErrorMessage("Please input explanation text");
      return;
    }

    let response: Observable<TextResponse>;

    if (this.data.reservation) {
      response = this.drivingService.rejectReservationDrivingDriver(this.cancellationReason);
    } else {
      response = this.drivingService.rejectRegularDrivingDriver(this.cancellationReason);
    }

    response.subscribe({
        next: (_) => {
          this.messageresponse.openErrorMessage("You have successfully declined the ride")
        },
        error: (error) => {
          this.messageresponse.openErrorMessage(error.error.message)
        }
      }
    );
    this.closeDialog();
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
