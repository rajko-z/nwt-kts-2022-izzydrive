import {Component, OnInit} from '@angular/core';
import {NotificationM} from "../../../model/notifications/notification";
import {NotificationService} from "../../../services/notificationService/notification.service";
import {DrivingService} from "../../../services/drivingService/driving.service";

@Component({
  selector: 'app-notification-review',
  templateUrl: './notification-review.component.html',
  styleUrls: ['./notification-review.component.scss']
})
export class NotificationReviewComponent implements OnInit {

  notifications: NotificationM[];

  constructor(private notificationService: NotificationService, private drivingService: DrivingService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.notificationService.findAll().subscribe((res) => {
      this.notifications = res as NotificationM[];
    });
  }

  deleteNotification(id: number) {
    this.notificationService.deleteNotification(id).subscribe((res) => {
      this.loadData();
    })
  }

  onYesClick(drivingId: number, id: number) {

  }

  onNoClick(drivingId: number, id: number) {
    this.drivingService.deleteDriving(drivingId).subscribe((res) => {

    });
    this.deleteNotification(id);
  }
}
