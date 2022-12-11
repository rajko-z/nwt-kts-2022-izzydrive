import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {StepperSelectionEvent, STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import {MatStepper, MatStepperModule} from '@angular/material/stepper';
import { FloatLabelType } from '@angular/material/form-field';
import { CarType, getCarType } from 'src/app/model/car/CarType';
import { DriverService } from 'src/app/services/driver/driver.service';


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

  

  driverForm = new FormGroup({});
  
  carForm = new FormGroup({});

  isValidDriverForm : boolean = false;
  isValidCarForm : boolean = false;

  onFirstStepNext(driverData : FormGroup, stepper: MatStepper){
    this.driverForm = driverData;
    this.isValidDriverForm = true;
    console.log(this.driverForm);
    stepper.next();
  }

  onSecondStepNext(carData: FormGroup, stepper: MatStepper){
    this.carForm = carData;
    this.isValidCarForm = true;
    this.driverForm.addControl("carData" , this.carForm);
    this.driverService.addDriver(this.driverForm.value).subscribe(
      ({
        next : (responce) => {
          stepper.next();
          console.log(responce)
       
      },
        error: (error )=> {
          
         console.log(error)
          
      }
      })
    )
  }
 

  constructor(private formBuilder: FormBuilder, private driverService: DriverService  ) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    
  }
}
