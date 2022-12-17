import { Injectable } from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DrivingService {

  constructor(private http: HttpClientService) { }

  findAllById(driverId) {
    return this.http.get(environment.apiUrl + `drivings/driver/${driverId}`);
  }
}
