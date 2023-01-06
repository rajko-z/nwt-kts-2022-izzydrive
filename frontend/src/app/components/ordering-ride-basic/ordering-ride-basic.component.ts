import {Component, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MatStepper} from "@angular/material/stepper";
import {DrivingOption} from "../../model/driving/drivingOption";

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent implements OnInit {

  rideForm = new FormGroup({});

  drivingOptions: DrivingOption[];

  isValidRideForm: boolean = false;

  constructor() {
  }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  onFirstStepNext(drivingOptions: DrivingOption[], stepper: MatStepper) {
    this.drivingOptions = drivingOptions;
    this.isValidRideForm = true;
    stepper.selected.completed = true;
    stepper.next();
  }

  onSecondStepNext(stepper: MatStepper) {
    stepper.next();
    stepper.selected.completed = true;
  }
}
