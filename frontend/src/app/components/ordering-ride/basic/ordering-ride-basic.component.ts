import {Component, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MatStepper} from "@angular/material/stepper";
import {DrivingOption} from "../../../model/driving/drivingOption";

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent {

  drivingOptions: DrivingOption[];
  selectedOption: DrivingOption;

  constructor() {
  }

  onFirstStepNext(drivingOptions: DrivingOption[], stepper: MatStepper) {
    this.drivingOptions = drivingOptions;
    stepper.selected.completed = true;
    stepper.next();
  }

  onSecondStepNext(selectedOption: DrivingOption, stepper: MatStepper) {
    this.selectedOption = selectedOption;
    stepper.selected.completed = true;
    stepper.next();
  }
}
