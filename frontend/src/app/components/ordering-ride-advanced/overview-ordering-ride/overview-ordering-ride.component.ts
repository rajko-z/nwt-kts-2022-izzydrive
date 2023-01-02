import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-overview-ordering-ride',
  templateUrl: './overview-ordering-ride.component.html',
  styleUrls: ['./overview-ordering-ride.component.scss']
})
export class OverviewOrderingRideComponent implements OnInit {

  @Input() rideForm?: FormGroup;
  @Input() paymentForm?: FormGroup;

  constructor() {
  }

  ngOnInit(): void {
  }

  onSubmit() {

  }
}
