import {Component, OnInit} from '@angular/core';
import {Driving, DrivingWithLocations} from "../../model/driving/driving";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../../services/userService/user-sevice.service";
import {DriverService} from "../../services/driverService/driver.service";
import {MapService} from "../../services/mapService/map.service";

@Component({
  selector: 'app-reservation-page-driver',
  templateUrl: './reservation-page-driver.component.html',
  styleUrls: ['./reservation-page-driver.component.scss']
})
export class ReservationPageDriverComponent implements OnInit {
  reservation?: DrivingWithLocations;
  private stompClient: any;

  constructor(
    private driverService: DriverService,
    private userService: UserService,
    private mapService: MapService
  ) { }

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
    this.onReservation();
  }

  private onReservation() {
    this.stompClient.subscribe('/driving/loadReservation', (message: { body: string }) => {
      const driving: DrivingWithLocations = JSON.parse(message.body);
      if (driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.reservation = driving
        if (this.reservation.id === null) {
          this.reservation = null;
        }
        this.setUpMap();
      }
    });
  }

  private loadData() {
    this.driverService.getReservation().subscribe((driving) => {
      if (driving && driving.driver.email === this.userService.getCurrentUserEmail()) {
        this.reservation = driving;
        this.setUpMap();
      }
    });
  }

  private setUpMap(): void {
    this.mapService.resetEverythingOnMap();

    if (this.reservation) {
      this.mapService.addAllFromDriving(this.reservation);
    }
  }

}
