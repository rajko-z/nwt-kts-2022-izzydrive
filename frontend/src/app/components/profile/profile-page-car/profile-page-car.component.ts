import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProfilePageData } from 'src/app/model/user/profileData';
import { CarService } from 'src/app/services/carService/car.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-profile-page-car',
  templateUrl: './profile-page-car.component.html',
  styleUrls: ['./profile-page-car.component.scss']
})
export class ProfilePageCarComponent implements OnInit {

  constructor(private carService: CarService, 
    private snackbar : MatSnackBar, 
    private userService : UserService,
    private router: Router) { }
  profilePagedata : ProfilePageData;
  isCarLoaded: boolean = false;

  ngOnInit(): void {
    
    this.carService.getCarByDriverId(this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
       console.log(response)
       this.profilePagedata = this.carService.getProfilePageDataFromCar(response);
       console.log(this.profilePagedata)
       this.isCarLoaded = true;
        ;
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "ERROR")
      } 
    })
  }
}
