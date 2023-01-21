import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouteDTO } from 'src/app/model/route/route';
import { RouteService } from 'src/app/services/routeService/route.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-favorite-routs',
  templateUrl: './favorite-routs.component.html',
  styleUrls: ['./favorite-routs.component.scss']
})
export class FavouriteRoutsComponent implements OnInit {

  constructor(private routeService : RouteService, 
              private userService : UserService, 
              private router : Router,
              private snackbar: MatSnackBar) { }

  favoriteRides : RouteDTO[];

  ngOnInit(): void {
    this.routeService.getPassengerFavouriteRides(this.userService.getCurrentUserId()).subscribe({
      next: (response) => {
        console.log(response)
        this.favoriteRides = response;
      },
      error: (error) => {
        console.log(error)
        this.snackbar.open(error.error.message, "OK");
      }
    })
  }

  getRide(route : RouteDTO){
    if (RouteService.selectedFavouriteRides){
      RouteService.selectedFavouriteRides[this.userService.getCurrentUserId()] = route;
    }
    else {
      let id : number = this.userService.getCurrentUserId();
      RouteService.selectedFavouriteRides = { [id]: route }
    } 
    this.router.navigateByUrl("/passenger/order-now")
  }

}
