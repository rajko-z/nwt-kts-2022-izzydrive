import {Component, OnInit} from '@angular/core';
import {PassengerService} from "../../services/passengerService/passenger.service";
import {Router} from "@angular/router";
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MapService} from "../../services/mapService/map.service";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-current-driving-page',
  templateUrl: './current-driving-page.component.html',
  styleUrls: ['./current-driving-page.component.scss']
})
export class CurrentDrivingPageComponent implements OnInit {

  currentDriving: DrivingWithLocations;

  private stompClient: any;

  constructor(
    private passengerService: PassengerService,
    private router: Router,
    private snackBar: MatSnackBar,
    private mapService: MapService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.loadData();
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
    this.onDeleteCurrentDriving();
    this.onRefreshedCurrentDriving();
  }

  private onRefreshedCurrentDriving() {
    this.stompClient.subscribe('/driving/refreshedDrivingForPassengers', (message: { body: string }) => {
      const driving: DrivingWithLocations = JSON.parse(message.body);
      for (const email of driving.passengers) {
        if (email === this.userService.getCurrentUserEmail()) {
          this.currentDriving = driving;
          this.setUpMap();
        }
      }
    });
  }

  private onDeleteCurrentDriving() {
    this.stompClient.subscribe('/driving/deleteDriving', (message: { body: string }) => {
      if (message.body === this.userService.getCurrentUserEmail()) {
        this.router.navigateByUrl('/passenger/order-now');
        this.snackBar.open("The ride was canceled by the driver", "Ok", {
          duration: 5000,
          verticalPosition: 'bottom',
          horizontalPosition: 'right',
        })
      }
    });
  }

  private loadData() {
    this.passengerService.findCurrentDrivingWithLocations()
      .subscribe({
          next: (driving) => {
            if (driving) {
              if (driving.drivingState === DrivingState.PAYMENT) {
                this.router.navigateByUrl('/passenger/payment');
              } else if (driving.drivingState === DrivingState.ACTIVE || driving.drivingState === DrivingState.WAITING) {
                this.currentDriving = driving;
                this.setUpMap();
              }
            } else {
              this.router.navigateByUrl('/passenger/order-now');
            }
          }
        }
      );
  }

  setUpMap(): void {
    this.mapService.resetEverythingOnMap();
    this.mapService.addAllFromDriving(this.currentDriving);
  }
}
