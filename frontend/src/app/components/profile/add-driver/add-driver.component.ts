import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {StepperSelectionEvent, STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import {MatStepper, MatStepperModule} from '@angular/material/stepper';
import { FloatLabelType } from '@angular/material/form-field';
import { CarType, getCarType } from 'src/app/model/car/CarType';


@Component({
  selector: 'app-add-driver',
  templateUrl: './add-driver.component.html',
  styleUrls: ['./add-driver.component.scss'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
})
export class AddDriverComponent implements OnInit {

  name_regexp = "^[a-zA-Z]+$";

  carTypes = CarType;

  driverForm = new FormGroup({
    firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    email: new FormControl('', [Validators.email, Validators.required]),
    phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]*$"),
                                                          Validators.minLength(13), 
                                                          Validators.maxLength(13)]),
  });
  
  carForm = new FormGroup({});

  onFirstStepNext(driverData : FormGroup, stepper: MatStepper){
    this.driverForm = driverData;
    console.log(this.driverForm);
    stepper.next();
  }

  onSecondStepNext(carData: FormGroup, stepper: MatStepper){
    this.carForm = carData;
    console.log(this.carForm);
    stepper.next();
  }
 

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
  }

  onSubmit(): void {

  }
}
