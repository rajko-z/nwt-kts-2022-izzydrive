import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {StepperSelectionEvent, STEPPER_GLOBAL_OPTIONS} from '@angular/cdk/stepper';
import {MatStepper, MatStepperModule} from '@angular/material/stepper';
import { FloatLabelType } from '@angular/material/form-field';
import { CarType, getCarType } from 'src/app/model/car/CarType';
import { DriverService } from 'src/app/services/driver/driver.service';
import { TmplAstRecursiveVisitor } from '@angular/compiler';
import { MatSnackBar } from '@angular/material/snack-bar';


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
  driverForm = new FormGroup({});
  carForm = new FormGroup({});

  isValidDriverForm : boolean = false;
  isValidCarForm : boolean = true;

  addingFinished : boolean = false;
  successfullyFinished: boolean = false;

  onFirstStepNext(driverData : FormGroup, stepper: MatStepper){
    this.driverForm = driverData;
    stepper.selected.completed = true;
    this.isValidDriverForm = true;
    stepper.next();
  }

  onSecondStepNext(carData: FormGroup, stepper: MatStepper){
    this.carForm = carData;
    this.isValidCarForm = true;
    stepper.next();
    stepper.selected.completed = true;
    this.driverForm.addControl("carData" , this.carForm);
    this.driverService.addDriver(this.driverForm.value).subscribe(
      ({
        next : (responce) => {
          this.addingFinished = true;
          this.successfullyFinished = true;
       
      },
        error: (error )=> {
          this.addingFinished = true;
          this.successfullyFinished = false;
          this.openErrorMessage(error.error.message);
      }
      })
    )
  }
 

  constructor(private driverService: DriverService , private messageTooltip: MatSnackBar  ) { }

  ngOnInit(): void {
  }

  openErrorMessage(message: string): void{
    this.messageTooltip.open(message, 'Close', {
      horizontalPosition: "center",
      verticalPosition: "top",
      panelClass: ['messageTooltip']
    });
  }
}
