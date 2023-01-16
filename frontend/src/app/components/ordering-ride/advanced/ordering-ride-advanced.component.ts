import {Component, Input, OnInit} from '@angular/core';
import {MatStepper} from "@angular/material/stepper";
import {STEPPER_GLOBAL_OPTIONS} from "@angular/cdk/stepper";
import {DrivingOption} from "../../../model/driving/drivingOption";
import {DrivingFinderRequest} from "../../../model/driving/drivingFinderRequest.";

@Component({
  selector: 'app-ordering-ride-advanced',
  templateUrl: './ordering-ride-advanced.component.html',
  styleUrls: ['./ordering-ride-advanced.component.scss'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
})
export class OrderingRideAdvancedComponent implements OnInit {

  drivingFinderRequest: DrivingFinderRequest;
  drivingOptions: DrivingOption[];

  selectedOption: DrivingOption;
  @Input() scheduleRide: boolean;

  constructor() {
  }

  ngOnInit(): void {
  }

  onFirstStepNext(drivingOptions: DrivingOption[], stepper: MatStepper) {
    this.drivingOptions = drivingOptions;
    stepper.selected.completed = true;
    stepper.next();
  }

  saveDrivingFinderRequest(request: DrivingFinderRequest) {
    this.drivingFinderRequest = request;
  }

  onSecondStepNext(selectedOption: DrivingOption, stepper: MatStepper) {
    this.selectedOption = selectedOption;
    stepper.selected.completed = true;
    stepper.next();
  }
}
