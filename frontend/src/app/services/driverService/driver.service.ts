import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private http: HttpClientService) { }

  addDriver(driverData){
    return this.http.post(
      environment.apiUrl + "drivers/add",
      driverData
    )
  }

  findAll() {
    return this.http.get(environment.apiUrl + "drivers");
  }
}
