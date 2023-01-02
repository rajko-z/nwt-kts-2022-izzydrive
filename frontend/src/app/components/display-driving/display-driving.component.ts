import {Component, Input, OnInit} from '@angular/core';
import {Driving} from "../../model/driving/driving";
import {MatDialog} from "@angular/material/dialog";
import {ExplanationDialogComponent} from "../explanation-dialog/explanation-dialog.component";

@Component({
  selector: 'app-display-driving',
  templateUrl: './display-driving.component.html',
  styleUrls: ['./display-driving.component.scss']
})
export class DisplayDrivingComponent implements OnInit {

  @Input()
  time: string;

  @Input()
  driving: Driving;
  formatStartDate: string;


  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {
    let startDate = this.driving.startDate;
    this.formatStartDate = `${startDate.getHours()}:${startDate.getMinutes()}  ${startDate.getDay()}-${startDate.getMonth()}-${startDate.getFullYear()}`;
  }

  cancelDriving() {
    this.dialog.open(ExplanationDialogComponent, {data: this.driving.id});
  }

  endDriving() {

  }

  startDriving() {

  }
}
