import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {PassengerService} from "../../services/passengerService/passenger.service";
import {DrivingState} from "../../model/driving/driving";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home-page-logged',
  templateUrl: './home-page-logged.component.html',
  styleUrls: ['./home-page-logged.component.scss']
})
export class HomePageLoggedComponent implements OnInit {

  reservation: boolean;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private passengerService: PassengerService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.reservation = data.reservation;
    });
    this.passengerService.findCurrentDrivingWithLocations()
      .subscribe({
          next: (driving) => {
            if (driving) {
              if (driving.drivingState === DrivingState.PAYMENT) {
                this.router.navigateByUrl('/passenger/payment');
              } else if (driving.drivingState === DrivingState.ACTIVE || driving.drivingState === DrivingState.WAITING){
                this.router.navigateByUrl('/passenger/current-driving');
              }
            }
          }
        }
      );
  }

}
