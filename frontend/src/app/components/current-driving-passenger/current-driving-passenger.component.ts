import {Component, Input, OnInit} from '@angular/core';
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ReportDriverCheckComponent} from "../report-driver-check/report-driver-check.component";
import {PassengerService} from "../../services/passengerService/passenger.service";

@Component({
  selector: 'app-current-driving-passenger',
  templateUrl: './current-driving-passenger.component.html',
  styleUrls: ['./current-driving-passenger.component.scss']
})
export class CurrentDrivingPassengerComponent implements OnInit {

  @Input() currentDriving?: DrivingWithLocations;

  minLeft: number;
  interval;

  drivingActive: boolean = false;

  waitingForRideToStart: boolean = false;

  constructor(
    private snackBar: MatSnackBar,
    private passengerService: PassengerService) {
  }

  ngOnInit(): void {}

  ngOnChanges(): void {
    if (this.currentDriving !== undefined) {
      if (this.currentDriving.drivingState === DrivingState.ACTIVE) {
        this.drivingActive = true;
        return;
      } else {
        this.fetchNewTimePeriodically();
      }
    }
  }

  fetchNewTimePeriodically() {
    this.fetchNewTimeAndUpdateTimeLeft();
    this.interval = setInterval(() => this.fetchNewTimeAndUpdateTimeLeft(), 15000);
  }

  fetchNewTimeAndUpdateTimeLeft() {
    this.passengerService.getEstimatedRouteLeftToStartOfDriving()
      .subscribe({
          next: (route) => {
            if (route.coordinates.length == 0) {
              clearInterval(this.interval);
              this.waitingForRideToStart = true;
            } else {
              this.minLeft = this.convertSecToMin(route.duration);
            }
          }
        }
      );
  }

  convertSecToMin(seconds: number): number {
    return Math.ceil(seconds / 60);
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
