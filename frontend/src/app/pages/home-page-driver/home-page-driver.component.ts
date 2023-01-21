import {Component, OnInit} from '@angular/core';
import {Driving} from "../../model/driving/driving";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {DrivingService} from "../../services/drivingService/driving.service";
import {UserService} from "../../services/userService/user-sevice.service";

@Component({
  selector: 'app-home-page-driver',
  templateUrl: './home-page-driver.component.html',
  styleUrls: ['./home-page-driver.component.scss']
})
export class HomePageDriverComponent implements OnInit {

  currentDriving?: Driving;
  nextDriving?: Driving;
  status: boolean;
  currentStatus: string = "start";
  private stompClient: any;

  constructor(private drivingService: DrivingService, private userService: UserService) {
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.loadData();
    this.status = true;
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
  }

  private onNewCurrentDriving() {
    this.stompClient.subscribe('/driving/loadCurrentDriving', (message: { body: string }) => {
      const driving = JSON.parse(message.body);
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.currentDriving = driving
        this.checkCurrentStatus();
        if (this.currentDriving.id === null) {
          this.currentDriving = null;
        }
      }
    });
  }

  private onNewNextDriving() {
    this.stompClient.subscribe('/driving/loadNextDriving', (message: { body: string }) => {
      const driving: Driving = JSON.parse(message.body);
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.nextDriving = driving;
        if (this.nextDriving.id === null) {
          this.nextDriving = null;
        }
      }
    });
  }

  loadData() {
    this.loadCurrentDriving();
    this.loadNextDriving();
  }

  private loadNextDriving() {
    this.drivingService.getNextDriving().subscribe((res) => {
      const driving: Driving = res as Driving;
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.nextDriving = driving;
      }
    });
  }

  private loadCurrentDriving() {
    this.drivingService.getCurrentDriving().subscribe((res) => {
      const driving: Driving = res as Driving;
      if (driving.driverEmail === this.userService.getCurrentUserEmail()) {
        this.currentDriving =driving
        this.checkCurrentStatus();
      }
    });
  }

  private checkCurrentStatus() {
    if (this.currentDriving?.startDate === null) {
      this.currentStatus = "start";
    } else if (this.currentDriving?.startDate !== null) {
      this.currentStatus = "current";
    }
  }
}
