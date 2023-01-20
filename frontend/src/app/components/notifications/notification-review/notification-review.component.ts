import {Component, OnInit} from '@angular/core';
import {NotificationM} from "../../../model/notifications/notification";
import {NotificationService} from "../../../services/notificationService/notification.service";

@Component({
  selector: 'app-notification-review',
  templateUrl: './notification-review.component.html',
  styleUrls: ['./notification-review.component.scss']
})
export class NotificationReviewComponent implements OnInit {

  notifications: NotificationM[];

  constructor(private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.notificationService.findAll().subscribe((res) => {
      this.notifications = res as NotificationM[];
      console.log(this.notifications);
    });
  }

  deleteNotification(id: number) {
    this.notificationService.deleteNotification(id).subscribe((res) => {
      this.loadData();
      console.log(res);
    })
  }
}
