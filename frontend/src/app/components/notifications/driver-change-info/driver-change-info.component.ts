import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { NotificationService } from 'src/app/services/notificationService/notification.service';

@Component({
  selector: 'app-driver-change-info',
  templateUrl: './driver-change-info.component.html',
  styleUrls: ['./driver-change-info.component.scss']
})
export class DriverChangeInfoComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
  private notificationService: NotificationService) { }

  ngOnInit(): void {
  }

}
