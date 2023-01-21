import { Injectable } from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {FavoriteRoute} from "../../model/route/favoriteRoute";
import { Route } from '@angular/router';
import { Observable } from 'rxjs';
import { RouteDTO } from 'src/app/model/route/route';
import { TextResponse } from 'src/app/model/response/textresponse';

@Injectable({
  providedIn: 'root'
})
export class RouteService {

  public static selectedFavouriteRides = {};

  constructor(private http: HttpClientService ) { }

  addFavoriteRoute(favoriteRoute: FavoriteRoute): Observable<TextResponse>{
    return this.http.postT<TextResponse>(
      environment.apiUrl + "routes/addFavorite",
      favoriteRoute
    )
  }

  getPassengerFavouriteRides(passengerId : number) : Observable<RouteDTO[]>{
    return this.http.getT<RouteDTO[]>(environment.apiUrl + `routes/favorite-routes/${passengerId}`)
  }

  removeFromFavoriteRoutes(routeId : number, passengerId : number): Observable<TextResponse>{
    return this.http.deleteT<TextResponse>(environment.apiUrl + `routes/remove-favorite/${routeId}/${passengerId}`
      )
  }

  addNewFavoriteRoute(favoriteRoute : FavoriteRoute):Observable<TextResponse>{
    return this.http.postT<TextResponse>(environment.apiUrl + `routes/add-new`, favoriteRoute )
  }
}
