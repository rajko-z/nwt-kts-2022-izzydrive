import {Component, Input} from '@angular/core';
import {NotificationStatus} from "../../../../model/notifications/notificationStatus";

@Component({
  selector: 'app-notification-image',
  templateUrl: './notification-image.component.html',
  styleUrls: ['./notification-image.component.scss']
})
export class NotificationImageComponent {
  @Input() notificationStatus: NotificationStatus;

  constructor() {
  }

}
