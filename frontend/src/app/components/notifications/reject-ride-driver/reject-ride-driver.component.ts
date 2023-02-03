import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";

@Component({
  selector: 'app-reject-ride-driver',
  templateUrl: './reject-ride-driver.component.html',
  styleUrls: ['./reject-ride-driver.component.scss']
})
export class RejectRideDriverComponent {

  minute: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data) {
    this.minute = new Date(data.message.duration * 1000).toISOString().slice(14, 19);
  }

  okClicked() {
    this.data.preClose();
  }
}
