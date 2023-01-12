import {Injectable} from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class DrivingService {

  constructor(private http: HttpClientService) {
  }

  findAllByDriverId(driverId) {
    return this.http.get(environment.apiUrl + `drivings/driver/${driverId}`);
  }

  findAllByPassengerId(passengerId) {
    return this.http.get(environment.apiUrl + `drivings/passenger/${passengerId}`);
  }

  rejectDriving(explanation) {
    return this.http.post(environment.apiUrl + `driving-notes/reject`, explanation);
  }
}
