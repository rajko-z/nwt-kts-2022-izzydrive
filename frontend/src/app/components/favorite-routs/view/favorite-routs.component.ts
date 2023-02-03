import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ro } from 'date-fns/locale';
import { RouteDTO } from 'src/app/model/route/route';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
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
              private snackbar: MatSnackBar,
              private responseMessage: ResponseMessageService) { }

  favoriteRoutes : RouteDTO[];
  isDataLoaded: boolean = false;

  ngOnInit(): void {
    this.routeService.getPassengerFavouriteRides(this.userService.getCurrentUserId()).subscribe({
      next: (response) => {
        this.favoriteRoutes = response;
        this.isDataLoaded = true;
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message);
      }
    })
  }

  getRide(route : RouteDTO, forNow: boolean){
    if (RouteService.selectedFavouriteRides){
      RouteService.selectedFavouriteRides[this.userService.getCurrentUserId()] = route;
    }
    else {
      let id : number = this.userService.getCurrentUserId();
      RouteService.selectedFavouriteRides = { [id]: route }
    }
    if (forNow) {
      this.router.navigateByUrl("/passenger/order-now")
    } else {
      this.router.navigateByUrl("/passenger/order-for-later")
    }
  }

  removeFromFavorites(route : RouteDTO){
    this.routeService.removeFromFavoriteRoutes(route.id,  this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
        this.responseMessage.openSuccessMessage(response.text)
        this.favoriteRoutes = this.favoriteRoutes.filter((r) => r.id !== route.id)
      },
      error : (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }

}
