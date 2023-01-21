import { Injectable } from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {FavoriteRoute} from "../../model/route/favoriteRoute";
import { Route } from '@angular/router';
import { Observable } from 'rxjs';
import { RouteDTO } from 'src/app/model/route/route';

@Injectable({
  providedIn: 'root'
})
export class RouteService {

  public static selectedFavouriteRides = {};

  constructor(private http: HttpClientService ) { }

  addFavoriteRoute(favoriteRoute: FavoriteRoute){
    console.log("pozvano")
    return this.http.post(
      environment.apiUrl + "routes/addFavorite",
      favoriteRoute
    )
  }

  getPassengerFavouriteRides(passengerId : number) : Observable<RouteDTO[]>{
    return this.http.getT<RouteDTO[]>(environment.apiUrl + `routes/favorite-routes/${passengerId}`)
  }
}
