import { Component, OnInit } from '@angular/core';
import { Route } from '@angular/router';
import { RouteService } from 'src/app/services/routeService/route.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-favorite-routs',
  templateUrl: './favorite-routs.component.html',
  styleUrls: ['./favorite-routs.component.scss']
})
export class FavouriteRoutsComponent implements OnInit {

  constructor(private routeService : RouteService, private userService : UserService) { }

  favoriteRides : Route[] = [];

  ngOnInit(): void {
    this.routeService.getPassengerFavouriteRides(this.userService.getCurrentUserId()).subscribe({
      next: (response) => {
        console.log(response)
        this.favoriteRides = response;
      },
      error: (error) => {
        console.log(error.error)
        
      }

    })
  }

}
