import {Component, Input, OnInit} from '@angular/core';
import {NotificationM} from "../../../../model/notifications/notification";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
  @Input() option: NotificationM;

  constructor() {
  }

  ngOnInit(): void {
  }

}
