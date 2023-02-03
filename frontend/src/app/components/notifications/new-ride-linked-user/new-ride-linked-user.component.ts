import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {DrivingService} from "../../../services/drivingService/driving.service";

@Component({
  selector: 'app-new-ride-linked-user',
  templateUrl: './new-ride-linked-user.component.html',
  styleUrls: ['./new-ride-linked-user.component.scss']
})
export class NewRideLinkedUserComponent {

  minute: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private router: Router,
              private drivingService: DrivingService) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  openPaymentPageClick() {
    this.router.navigateByUrl('passenger/payment');
    this.data.preClose();
  }

  cancelRideClick() {
    this.drivingService.rejectDrivingLinkedPassenger().subscribe((res) => {
    });
    this.data.preClose();
  }
}
