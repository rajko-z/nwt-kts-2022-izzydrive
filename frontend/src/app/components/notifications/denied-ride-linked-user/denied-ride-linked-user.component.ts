import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";

@Component({
  selector: 'app-denied-ride-linked-user',
  templateUrl: './denied-ride-linked-user.component.html',
  styleUrls: ['./denied-ride-linked-user.component.scss']
})
export class DeniedRideLinkedUserComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data) {
  }

  ngOnInit(): void {
  }

  okClick() {
    this.data.preClose();
  }
}
