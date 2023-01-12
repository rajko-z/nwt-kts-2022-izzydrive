import {Injectable} from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {DrivingOption} from "../../model/driving/drivingOption";
import {DrivingFinderRequest} from "../../model/driving/drivingFinderRequest.";
import {HttpClient} from "@angular/common/http";
import {DrivingRequest} from "../../model/driving/driving";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {TextResponse} from "../../model/response/textresponse";

@Injectable({
  providedIn: 'root'
})
export class DrivingService {

  constructor(private httpClientService: HttpClientService,
              private http: HttpClient,) {
  }

  findAllByDriverId(driverId) {
    return this.httpClientService.get(environment.apiUrl + `drivings/driver/${driverId}`);
  }

  findAllByPassengerId(passengerId) {
    return this.httpClientService.get(environment.apiUrl + `drivings/passenger/${passengerId}`);
  }

  rejectDriving(drivingId, explanation) {
    return this.httpClientService.post(environment.apiUrl + `drivings/reject/${drivingId}`, explanation);
  }

  getSimpleDrivingOptions(payload: PlaceOnMap[]): Observable<DrivingOption[]> {
    return this.http.post<DrivingOption[]>(environment.apiUrl + 'drivings/finder/simple', payload);
  }
  getAdvancedDrivingOptions(payload: DrivingFinderRequest): Observable<DrivingOption[]> {
    return this.httpClientService.postT<DrivingOption[]>(environment.apiUrl + 'drivings/finder/advanced', payload);
  }

  processDrivingRequest(payload: DrivingRequest) {
    return this.httpClientService.postT<TextResponse>(environment.apiUrl + 'drivings/process', payload);
  }
}
