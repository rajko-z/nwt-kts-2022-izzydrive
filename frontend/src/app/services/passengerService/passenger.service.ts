import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";
import {DrivingWithLocations} from "../../model/driving/driving";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PassengerService {

  constructor(private http: HttpClientService) {}

  findAll() {
    return this.http.get(environment.apiUrl + "passengers");
  }

  findCurrentDrivingWithLocations(): Observable<DrivingWithLocations> {
    return this.http.getT<DrivingWithLocations>(environment.apiUrl + 'passengers/current-driving');
  }


}
