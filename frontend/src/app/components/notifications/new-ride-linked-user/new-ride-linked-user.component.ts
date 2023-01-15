import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {UserService} from "../../../services/userService/user-sevice.service";
import {Driving} from "../../../model/driving/driving";

@Component({
  selector: 'app-new-ride-linked-user',
  templateUrl: './new-ride-linked-user.component.html',
  styleUrls: ['./new-ride-linked-user.component.scss']
})
export class NewRideLinkedUserComponent implements OnInit {

  minute: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private router: Router,
              private drivingService: DrivingService) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  ngOnInit(): void {
  }

  openPaymentPageClick() {
    this.router.navigateByUrl('passenger/payment');
    this.data.preClose();
  }

  cancelRideClick() {
    this.drivingService.rejectDrivingLinkedPassenger(this.data.message.drivingId).subscribe((res) => {
      console.log(res);
    });
    this.data.preClose();
  }
}
