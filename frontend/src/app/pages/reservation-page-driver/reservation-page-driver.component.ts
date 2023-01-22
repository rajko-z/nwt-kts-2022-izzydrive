import { Component, OnInit } from '@angular/core';
import {Driving} from "../../model/driving/driving";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {DrivingService} from "../../services/drivingService/driving.service";
import {UserService} from "../../services/userService/user-sevice.service";

@Component({
  selector: 'app-reservation-page-driver',
  templateUrl: './reservation-page-driver.component.html',
  styleUrls: ['./reservation-page-driver.component.scss']
})
export class ReservationPageDriverComponent implements OnInit {
  reservation?: Driving;
  private stompClient: any;

  constructor(private drivingService: DrivingService, private userService: UserService) { }

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
      const driving = JSON.parse(message.body);
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.reservation = driving
        if (this.reservation.id === null) {
          this.reservation = null;
        }
      }
    });
  }

  private loadData() {
    this.drivingService.getReservation().subscribe((res) => {
      const driving: Driving = res as Driving;
      console.log(driving);
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.reservation = driving;
      }
    });
  }
}
