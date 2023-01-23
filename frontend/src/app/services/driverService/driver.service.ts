import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';
import {DrivingWithLocations} from "../../model/driving/driving";

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private httpClientService: HttpClientService) { }

  addDriver(driverData){
    return this.httpClientService.post(
      environment.apiUrl + "drivers/add",
      driverData
    )
  }

  getCurrentDriving() {
    return this.httpClientService.getT<DrivingWithLocations>(environment.apiUrl + 'drivers/current-driving');
  }

  getNextDriving() {
    return this.httpClientService.getT<DrivingWithLocations>(environment.apiUrl + 'drivers/next-driving');
  }

  findAll() {
    return this.httpClientService.get(environment.apiUrl + "drivers");
  }
}
