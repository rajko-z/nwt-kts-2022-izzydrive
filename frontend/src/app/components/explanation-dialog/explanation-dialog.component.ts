import {Component, Inject} from '@angular/core';
import {DrivingService} from "../../services/drivingService/driving.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {UserSeviceService} from "../../services/userService/user-sevice.service";
import {DrivingNote} from "../../model/driving/drivingNote";

@Component({
  selector: 'app-explanation-dialog',
  templateUrl: './explanation-dialog.component.html',
  styleUrls: ['./explanation-dialog.component.scss']
})
export class ExplanationDialogComponent {
  explanationForm = new FormGroup({
    explanation: new FormControl('')
  });
  drivingNote: DrivingNote = {text: '', userId: 0, timestamp: new Date(), fromPassenger: false, divingId: 0};

  constructor(private drivingService: DrivingService, private userService: UserSeviceService, @Inject(MAT_DIALOG_DATA) public data) {

  }

  onSubmit() {
    this.drivingNote.divingId = this.data;
    this.drivingNote.userId = this.userService.getCurrentUserId();
    this.drivingNote.text = this.explanationForm.value.explanation;
    this.drivingService.rejectDriving(this.drivingNote).subscribe((res) => {
     console.log(res);
    });
  }
}
