import {Component, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MatStepper} from "@angular/material/stepper";
import {STEPPER_GLOBAL_OPTIONS} from "@angular/cdk/stepper";

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

  rideForm = new FormGroup({});
  paymentForm = new FormGroup({});

  isValidRideForm: boolean = false;
  isValidPaymentForm: boolean = true;

  addingFinished: boolean = false;
  successfullyFinished: boolean = false;

  constructor() {
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  onFirstStepNext(driverData: FormGroup, stepper: MatStepper) {
    this.rideForm = driverData;
    this.isValidRideForm = true;
    stepper.selected.completed = true;
    stepper.next();
  }

  onSecondStepNext(stepper: MatStepper) {
    stepper.next();
    stepper.selected.completed = true;
  }

  onThirdStepNext(paymentData: FormGroup, stepper: MatStepper) {
    this.paymentForm = paymentData;
    this.isValidPaymentForm = true;
    stepper.selected.completed = true;
    stepper.next();
  }

}
