import {Component, Input, OnInit} from '@angular/core';
import {Driving, DrivingWithLocations} from "../../model/driving/driving";
import {MatDialog} from "@angular/material/dialog";
import {ExplanationDialogComponent} from "../explanation-dialog/explanation-dialog.component";

@Component({
  selector: 'app-display-driving',
  templateUrl: './display-driving.component.html',
  styleUrls: ['./display-driving.component.scss']
})
export class DisplayDrivingComponent implements OnInit {

  @Input()
  currDrivingStatus?: string;

  @Input()
  driving?: DrivingWithLocations;

  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {}

  cancelDriving() {
    this.dialog.open(ExplanationDialogComponent, {data: this.driving.id});
  }

  endDriving() {

  }

  startDriving() {

  }

  cancelReservation(){

  }
}
