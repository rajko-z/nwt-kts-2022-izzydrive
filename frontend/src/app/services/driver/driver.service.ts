import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private http: HttpClient) { }

  addDriver(driverData){
    return this.http.post(
      environment.apiUrl + "drivers/add",
      driverData
    )
  }
}
