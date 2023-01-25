import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Car } from 'src/app/model/car/car';
import { CarService } from 'src/app/services/carService/car.service';
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
    private snackbar : MatSnackBar) { }

  ngOnInit(): void {
    this.carService.getCarByDriverId(this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
        this.carData = response;
        this.isDataLoaded = true;
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "ERROR")
      }
    })
  }

  onFormSubmit(carForm : FormGroup){
    let car : Car = carForm.value;
    car.id = this.carData.id;
    car.driverEmail = this.userService.getCurrentUserEmail();
    this.carService.editCarData(car).subscribe({
      next: (response) => {
        this.snackbar.open(response.text, "Ok")
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "ERROR")
      }
    })
  }

}