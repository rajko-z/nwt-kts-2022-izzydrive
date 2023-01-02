import { Injectable } from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {FavoriteRoute} from "../../model/route/favoriteRoute";

@Injectable({
  providedIn: 'root'
})
export class RouteService {

  constructor(private http: HttpClientService) { }

  addFavoriteRoute(favoriteRoute: FavoriteRoute){
    console.log("pozvano")
    return this.http.post(
      environment.apiUrl + "routes/addFavorite",
      favoriteRoute
    )
  }
}
