import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {DrivingService} from "../../../services/drivingService/driving.service";

@Component({
  selector: 'app-new-reservation',
  templateUrl: './new-reservation.component.html',
  styleUrls: ['./new-reservation.component.scss']
})
export class NewReservationComponent implements OnInit {

  time: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data, private router: Router, private drivingService: DrivingService) {
    const date = new Date(data.message.reservationTime);
    this.time = `${date.getHours()}:${date.getMinutes()}  ${date.getDate()}.${date.getMonth()+1}.${date.getFullYear()}.`
  }

  ngOnInit(): void {
  }

  okClick() {
    this.router.navigateByUrl('/passenger');
    this.data.preClose();
  }

  cancelReservationClick() {
    //reject reservation
    this.data.preClose();
  }
}
