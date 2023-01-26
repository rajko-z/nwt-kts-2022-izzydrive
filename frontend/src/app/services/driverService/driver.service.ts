import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';
import {DrivingWithLocations} from "../../model/driving/driving";
import {TextResponse} from "../../model/response/textresponse";

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

  getWorkingTimeForDriver() {
    return this.httpClientService.getT<TextResponse>(environment.apiUrl + 'working-intervals/get-minutes');
  }

  setDriverStatusToActive() {
    return this.httpClientService.putT<TextResponse>(environment.apiUrl + 'working-intervals/set-driver-active',null);
  }

  getDriverStatus() {
    return this.httpClientService.getT<TextResponse>(environment.apiUrl + 'working-intervals/get-driver-status');
  }

  setDriverStatusToInactive() {
    return this.httpClientService.putT<TextResponse>(environment.apiUrl + 'working-intervals/set-driver-inactive',null);
  }
}
