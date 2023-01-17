import {Component, OnInit} from '@angular/core';
import {PassengerService} from "../../services/passengerService/passenger.service";
import {Router} from "@angular/router";
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";

@Component({
  selector: 'app-current-driving-page',
  templateUrl: './current-driving-page.component.html',
  styleUrls: ['./current-driving-page.component.scss']
})
export class CurrentDrivingPageComponent implements OnInit {

  currentDriving: DrivingWithLocations;

  constructor(
    private passengerService: PassengerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.passengerService.findCurrentDrivingWithLocations()
      .subscribe({
          next: (driving) => {
            if (driving) {
              if (driving.drivingState === DrivingState.PAYMENT) {
                this.router.navigateByUrl('passenger/payment');
              } else if (driving.drivingState === DrivingState.ACTIVE || driving.drivingState === DrivingState.WAITING){
                this.currentDriving = driving;
              }
            } else {
              this.router.navigateByUrl('passenger/order-ride');
            }
          }
        }
      );
  }

}
