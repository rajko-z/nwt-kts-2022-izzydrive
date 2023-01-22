import {Component, Input, OnInit} from '@angular/core';
import {DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ReportDriverCheckComponent} from "../report-driver-check/report-driver-check.component";

@Component({
  selector: 'app-current-driving-passenger',
  templateUrl: './current-driving-passenger.component.html',
  styleUrls: ['./current-driving-passenger.component.scss']
})
export class CurrentDrivingPassengerComponent implements OnInit {

  @Input() currentDriving: DrivingWithLocations;

  minLeft: number;

  constructor( private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }




  reportDriver() {
    this.snackBar.openFromComponent(ReportDriverCheckComponent, {
      data: {
        driving: 'driving.id', preClose: () => {
          this.snackBar.dismiss()
        }
      },
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
    });
  }

}
