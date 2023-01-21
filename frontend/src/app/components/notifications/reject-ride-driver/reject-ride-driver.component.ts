import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {NotificationService} from "../../../services/notificationService/notification.service";

@Component({
  selector: 'app-reject-ride-driver',
  templateUrl: './reject-ride-driver.component.html',
  styleUrls: ['./reject-ride-driver.component.scss']
})
export class RejectRideDriverComponent implements OnInit {

  minute: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
              private drivingService: DrivingService,
              private notificationService: NotificationService) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  ngOnInit(): void {
  }

  onYesClick() {
    //choose new driver
    this.data.preClose();
  }

  onNoClick() {
    this.drivingService.deleteDriving(this.data.message.drivingId).subscribe((res) => {
      console.log(res);
    });
    this.notificationService.deleteNotificationFromAdmin(this.data.message.drivingId).subscribe((res) => {
      console.log(res);
    })
    this.data.preClose();
  }
}
