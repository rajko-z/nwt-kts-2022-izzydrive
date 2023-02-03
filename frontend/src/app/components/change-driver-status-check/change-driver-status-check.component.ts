import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import {DriverService} from "../../services/driverService/driver.service";

@Component({
  selector: 'app-change-driver-status-check',
  templateUrl: './change-driver-status-check.component.html',
  styleUrls: ['./change-driver-status-check.component.scss']
})
export class ChangeDriverStatusCheckComponent implements OnInit {

  toSetActive?: boolean;

  constructor(
    private driverService: DriverService,
    @Inject(MAT_DIALOG_DATA) public data: boolean,
    private dialogRef: MatDialogRef<ChangeDriverStatusCheckComponent>,
    public snackBar: MatSnackBar,
    private messageresponse: ResponseMessageService
  ) {}

  ngOnInit() {
    this.toSetActive = this.data;
  }

  yesClick() {
    if (this.toSetActive) {
      this.driverService.setDriverStatusToActive()
        .subscribe({
            next: (_) => {
              this.closeDialog(true);
            },
            error: (error) => {
              this.messageresponse.openErrorMessage(error.error.message);
              this.closeDialog(false);
            }
          }
        );
    } else {
      this.driverService.setDriverStatusToInactive()
        .subscribe({
            next: (_) => {
              this.closeDialog(true);
            },
            error: (error) => {
              this.messageresponse.openErrorMessage(error.error.message);
              this.closeDialog(false);
            }
          }
        );
    }
  }

  closeDialog(yesClicked) {
    this.dialogRef.close(yesClicked);
  }

}
