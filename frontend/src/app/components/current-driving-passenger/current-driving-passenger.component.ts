import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Driving, DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ReportDriverCheckComponent} from "../report-driver-check/report-driver-check.component";
import {PassengerService} from "../../services/passengerService/passenger.service";
import {environment} from "../../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../../services/userService/user-sevice.service";
import {EvaluationComponent} from "../driving-history/evaluation/evaluation.component";
import {MatDialog} from "@angular/material/dialog";
import {FavoriteRouteDialogComponent} from "../favorite-route-dialog/favorite-route-dialog.component";
import {FavoriteRoute} from "../../model/route/favoriteRoute";
import {Router} from "@angular/router";

@Component({
  selector: 'app-current-driving-passenger',
  templateUrl: './current-driving-passenger.component.html',
  styleUrls: ['./current-driving-passenger.component.scss']
})
export class CurrentDrivingPassengerComponent implements OnInit, OnDestroy {

  @Input() currentDriving?: DrivingWithLocations;

  minLeft: number;
  interval;

  drivingActive: boolean = false;

  finished: boolean = false;

  waitingForRideToStart: boolean = false;

  private stompClient: any;

  constructor(
    private snackBar: MatSnackBar,
    private passengerService: PassengerService,
    private dialog: MatDialog,
    private userService: UserService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

  ngOnDestroy(): void {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }

  ngOnChanges(): void {
    if (this.currentDriving !== undefined) {
      if (this.currentDriving.drivingState === DrivingState.ACTIVE) {
        this.drivingActive = true;
      } else {
        this.fetchNewTimePeriodically();
      }
    }
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(environment.socket);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openDrivingSocket();
    });
  }

  openDrivingSocket() {
    this.onRideStart();
    this.onRideEnd();
    this.onDriverArrivedAtStart();
  }

  private onRideStart() {
    this.stompClient.subscribe('/driving/rideStarted', (message: { body: string }) => {
      const emails: string[] = JSON.parse(message.body);
      for (const email of emails) {
        if (email === this.userService.getCurrentUserEmail()) {
          this.drivingActive = true;
        }
      }
    });
  }

  private onDriverArrivedAtStart() {
    this.stompClient.subscribe('/notification/driverArrivedStart', (message: { body: string }) => {
      const email = message.body;
      if (email === this.userService.getCurrentUserEmail()) {
        this.waitingForRideToStart = true;
      }
    });
  }

  private onRideEnd() {
    this.stompClient.subscribe('/driving/rideEnded', (message: { body: string }) => {
      const emails: string[] = JSON.parse(message.body);
      for (let email of emails) {
        if (email === this.userService.getCurrentUserEmail()) {
          this.finished = true;
          this.openEvaluationComponent();
          this.openFavouriteRouteDialog();
          this.router.navigateByUrl("/passenger/order-now");
        }
      }
    });
  }

  openEvaluationComponent(): void {
    const driving: Driving = new Driving();
    driving.id = this.currentDriving?.id;
    driving.evaluationAvailable = true;

    this.dialog.open(EvaluationComponent, {
      data: driving
    });
  }

  openFavouriteRouteDialog() {
    const routeId = this.currentDriving.route.id;
    const startLocation: string = this.currentDriving.route.start.name;
    const endLocation: string = this.currentDriving.route.end.name;
    const intermediate: string[] = this.currentDriving.route.intermediateStations.map(p => p.name);
    const route: FavoriteRoute = new FavoriteRoute( routeId, startLocation, endLocation, intermediate);

    this.dialog.open(FavoriteRouteDialogComponent, {
      data: route
    });
  }


  fetchNewTimePeriodically() {
    this.fetchNewTimeAndUpdateTimeLeft();
    this.interval = setInterval(() => this.fetchNewTimeAndUpdateTimeLeft(), 10000);
  }

  fetchNewTimeAndUpdateTimeLeft() {
    this.passengerService.getEstimatedRouteLeftToStartOfDriving()
      .subscribe({
          next: (route) => {
            if (route.coordinates.length <= 2) {
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
