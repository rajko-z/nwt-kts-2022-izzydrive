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

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data, private router: Router, private drivingService: DrivingService) { }

  ngOnInit(): void {
  }

  okClick() {
    this.router.navigateByUrl('/passenger');
    this.data.preClose();
  }

  cancelReservationClick() {
    //ovo proveri da li je okej
    this.drivingService.rejectDrivingLinkedPassenger(this.data.message.drivingId).subscribe((res) => {
      console.log(res);
    });
    this.data.preClose();
  }
}
