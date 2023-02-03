import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Car } from 'src/app/model/car/car';
import { CarService } from 'src/app/services/carService/car.service';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.scss']
})
export class EditCarComponent implements OnInit {

  carData : Car;
  isDataLoaded : boolean = false;

  constructor(private carService : CarService, 
    private userService: UserService,
    private snackbar : MatSnackBar,
    private responseMessage: ResponseMessageService) { }

  ngOnInit(): void {
    this.carService.getCarByDriverId(this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
        this.carData = response;
        this.isDataLoaded = true;
      },
      error : (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }

  onFormSubmit(carForm : FormGroup){
    let car : Car = carForm.value;
    car.id = this.carData.id;
    car.driverEmail = this.userService.getCurrentUserEmail();
    this.carService.editCarData(car).subscribe({
      next: (response) => {
        this.responseMessage.openSuccessMessage(response.text)
      },
      error : (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }

}
