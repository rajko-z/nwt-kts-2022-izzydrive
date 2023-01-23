import {Component, Inject} from '@angular/core';
import {DrivingService} from "../../services/drivingService/driving.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../services/userService/user-sevice.service";
import {DrivingNote} from "../../model/driving/drivingNote";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-explanation-dialog',
  templateUrl: './explanation-dialog.component.html',
  styleUrls: ['./explanation-dialog.component.scss']
})
export class ExplanationDialogComponent {
  explanationForm = new FormGroup({
    explanation: new FormControl('')
  });
  drivingNote: DrivingNote = {text: '', driverEmail: '', timestamp: new Date(), fromPassenger: false, drivingId: 0};

  constructor(private drivingService: DrivingService,
              private userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data,
              private snackBar: MatSnackBar,
              private dialogRef: MatDialogRef<ExplanationDialogComponent>,) {
  }

  onSubmit() {
    this.drivingNote.drivingId = this.data;
    this.drivingNote.driverEmail = this.userService.getCurrentUserEmail();
    this.drivingNote.text = this.explanationForm.value.explanation;
    this.drivingNote.timestamp = new Date();
    this.drivingService.rejectDriving(this.drivingNote).subscribe((res) => {
      this.snackBar.open("You have successfully declined the ride", "Ok", {
        duration: 3000,
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      })
    });
    this.closeDialog();
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
