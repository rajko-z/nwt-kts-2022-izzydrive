import {Component, OnInit} from '@angular/core';
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../../services/userService/user-sevice.service";
import {DriverService} from "../../services/driverService/driver.service";
import {MapService} from "../../services/mapService/map.service";
import {FavoriteRouteDialogComponent} from "../../components/favorite-route-dialog/favorite-route-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {
  ChangeDriverStatusCheckComponent
} from "../../components/change-driver-status-check/change-driver-status-check.component";
import {ReportDriverCheckComponent} from "../../components/report-driver-check/report-driver-check.component";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-home-page-driver',
  templateUrl: './home-page-driver.component.html',
  styleUrls: ['./home-page-driver.component.scss']
})
export class HomePageDriverComponent implements OnInit {

  currentDriving?: DrivingWithLocations;
  nextDriving?: DrivingWithLocations;
  driverStatus?: boolean;
  currDrivingStatus: string;

  private stompClient: any;

  constructor(
    private userService: UserService,
    private driverService: DriverService,
    private dialog: MatDialog,
    private mapService: MapService) {
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
    this.onNewCurrentDriving();
    this.onNewNextDriving();
    this.onDriverArrivedAtStart();
  }

  private onDriverArrivedAtStart() {
    this.stompClient.subscribe('/driving/arrivedAtStart', (message: { body: string }) => {
      const email: string = message.body;
      if (email === this.userService.getCurrentUserEmail()) {
        this.currDrivingStatus = "start";
      }
    });
  }

  private onNewCurrentDriving() {
    this.stompClient.subscribe('/driving/loadCurrentDriving', (message: { body: string }) => {
      const driving: DrivingWithLocations = JSON.parse(message.body);
      if (driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.currentDriving = driving;
        this.setCurrDrivingStatus();
        if (this.currentDriving.id === null) {
          this.currentDriving = null;
        }
        this.setUpMap();
      }
    });
  }
  private onNewNextDriving() {
    this.stompClient.subscribe('/driving/loadNextDriving', (message: { body: string }) => {
      const driving: DrivingWithLocations = JSON.parse(message.body);
      if (driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.nextDriving = driving;
        if (this.nextDriving.id === null) {
          this.nextDriving = null;
        }
      }
    });
  }

  loadData() {
    this.loadDriverStatus();
    this.loadCurrentDriving();
    this.loadNextDriving();
  }

  private loadDriverStatus() {
    this.driverService.getDriverStatus().subscribe((status) => {
      this.driverStatus = status.text === 'active';
    });
  }

  private loadNextDriving() {
    this.driverService.getNextDriving().subscribe((driving) => {
      if (driving && driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.nextDriving = driving;
      }
    });
  }

  private loadCurrentDriving() {
    this.driverService.getCurrentDriving().subscribe((driving) => {
      if (driving && driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.currentDriving = driving;
        this.setCurrDrivingStatus();
        this.setUpMap();
      }
    });
  }

  private setCurrDrivingStatus() {
    if (this.currentDriving?.drivingState === DrivingState.WAITING) {
      const coordinates = this.currentDriving.fromDriverToStart.coordinates;
      const lastCoordinate = coordinates.at(coordinates.length - 1);
      const driverCoordinate = this.currentDriving.driver.location;
      if (driverCoordinate.lat === lastCoordinate.lat && driverCoordinate.lon === lastCoordinate.lon) {
        this.currDrivingStatus = "start";
      } else if (coordinates.length <= 2) {
        this.currDrivingStatus = "start";
      } else {
        this.currDrivingStatus = "waiting";
      }
    } else if (this.currentDriving?.drivingState === DrivingState.ACTIVE) {
      const coordinates = this.currentDriving.fromStartToEnd.coordinates;
      const lastCoordinate = coordinates.at(coordinates.length - 1);
      const driverCoordinate = this.currentDriving.driver.location;
      if (driverCoordinate.lat === lastCoordinate.lat && driverCoordinate.lon === lastCoordinate.lon) {
        this.currDrivingStatus = "finish";
      } else {
        this.currDrivingStatus = "active";
      }
    }
  }

  private setUpMap(): void {
    this.mapService.resetEverythingOnMap();

    if (this.currentDriving) {
      this.mapService.addAllFromDriving(this.currentDriving);
    }
  }

  changeDriverStatusClicked() {
    const dialogRef = this.dialog.open(ChangeDriverStatusCheckComponent, {
      data: !this.driverStatus,
    });

    dialogRef.afterClosed().subscribe(yesClicked => {
      if (yesClicked === true) {
        this.driverStatus = !this.driverStatus;
      }
    });
  }
}
