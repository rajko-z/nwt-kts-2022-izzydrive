import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";

@Component({
  selector: 'app-reported-driver',
  templateUrl: './reported-driver.component.html',
  styleUrls: ['./reported-driver.component.scss']
})
export class ReportedDriverComponent {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data) {
  }

  okClicked() {
    this.data.preClose();
  }

}
