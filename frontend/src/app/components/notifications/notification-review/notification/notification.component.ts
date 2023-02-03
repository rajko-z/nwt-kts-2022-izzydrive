import {Component, Input} from '@angular/core';
import {NotificationM} from "../../../../model/notifications/notification";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent {
  @Input() option: NotificationM;

  constructor() {
  }

}
