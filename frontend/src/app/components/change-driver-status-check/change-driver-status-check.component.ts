import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
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
    public snackBar: MatSnackBar
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
              this.snackBar.open(error.error.message, "ERROR", {
                duration: 5000,
              });
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
              this.snackBar.open(error.error.message, "ERROR", {
                duration: 5000,
              });
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
