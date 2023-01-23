import {Component, Input, OnInit} from '@angular/core';
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ReportDriverCheckComponent} from "../report-driver-check/report-driver-check.component";
import {PassengerService} from "../../services/passengerService/passenger.service";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../../services/userService/user-sevice.service";

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

  private stompClient: any;

  constructor(
    private snackBar: MatSnackBar,
    private passengerService: PassengerService,
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

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

  initializeWebSocketConnection() {
    let ws = new SockJS(environment.socket);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openDrivingSocket();
    });
  }

  openDrivingSocket() {
    this.onRideStart();
  }

  private onRideStart() {
    this.stompClient.subscribe('/driving/rideStarted', (message: { body: string }) => {
      console.log(message.body);
      const emails: string[] = JSON.parse(message.body);
      for (const email of emails) {
        if (email === this.userService.getCurrentUserEmail()) {
          this.drivingActive = true;
        }
      }
    });
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
