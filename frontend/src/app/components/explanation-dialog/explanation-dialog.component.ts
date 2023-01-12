import {Component, Inject, OnInit} from '@angular/core';
import {DrivingService} from "../../services/drivingService/driving.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../services/userService/user-sevice.service";
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
  // drivingNote: DrivingNote = {text: '', userId: 0, timestamp: new Date()};
  // private drivingNote: DrivingNote;

  constructor(private drivingService: DrivingService, private userService: UserService, @Inject(MAT_DIALOG_DATA) public data) {
  //   this.drivingNote.userId = this.userService.getCurrentUserId();
  //   this.drivingNote.timestamp = new Date();
  }

  onSubmit() {
    console.log(this.explanationForm.value.explanation)
    // let drivingNote = {text: '', userId: 0, timestamp: new Date()};
    // this.drivingNote.text = this.explanationForm.value.explanation;
    // this.drivingService.rejectDriving(this.data, this.drivingNote);
  }
}
