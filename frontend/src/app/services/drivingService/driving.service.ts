import {Injectable} from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {DrivingOption} from "../../model/driving/drivingOption";
import {DrivingFinderRequest} from "../../model/driving/drivingFinderRequest.";
import {HttpClient} from "@angular/common/http";
import {Driving, DrivingRequest, DrivingWithLocations} from "../../model/driving/driving";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {TextResponse} from "../../model/response/textresponse";
import {Sort} from '@angular/material/sort';

@Injectable({
  providedIn: 'root'
})
export class DrivingService {

  constructor(private httpClientService: HttpClientService,
              private http: HttpClient) {
  }

  findAllByDriverId(driverId) {
    return this.httpClientService.get(environment.apiUrl + `drivings/driver/${driverId}`);
  }

  findAllByPassengerId(passengerId) {
    return this.httpClientService.get(environment.apiUrl + `drivings/passenger/${passengerId}`);
  }

  rejectRegularDrivingDriver(explanation) {
    return this.httpClientService.postT<TextResponse>(environment.apiUrl + `drivings/reject-regular-driver`, explanation);
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

  rejectDrivingLinkedPassenger() {
    return this.httpClientService.get(environment.apiUrl + 'drivings/reject-linked-user');
  }

  findById(id: number) {
    return this.httpClientService.get(environment.apiUrl + `drivings/${id}`);
  }

  getScheduleDrivingOptions(payload: DrivingFinderRequest): Observable<DrivingOption[]> {
    return this.httpClientService.postT<DrivingOption[]>(environment.apiUrl + 'drivings/finder/schedule', payload);
  }

  createReservation(payload: DrivingRequest) {
    return this.httpClientService.postT<TextResponse>(environment.apiUrl + 'drivings/process-reservation', payload);
  }

  getDrivingsHistoryForPassenger(passengerId: number) {
    return this.httpClientService.get(environment.apiUrl + `drivings/passenger/history/${passengerId}`);
  }

  sortData(sort: Sort, dataSource: Driving[]): Driving[] {
    if (!sort.active || sort.direction === '') {
      return dataSource
    }

    let sortedData = dataSource.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'startAddress':
          return this.compare(a.start.name, b.start.name, isAsc);
        case 'endAddress':
          return this.compare(a.end.name, b.end.name, isAsc);
        case 'startTime':
          return this.compare(a.startDate, b.startDate, isAsc);
        case 'endTime':
          return this.compare(a.endDate, b.endDate, isAsc);
        case 'price':
          return this.compare(a.price, b.price, isAsc);
        default:
          return 0;
      }
    });
    return sortedData;
  }


  compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  findPassengerReservations(passengerId: number) {
    return this.httpClientService.get(environment.apiUrl + `drivings/passenger/reservations/${passengerId}`);
  }

  cancelReservation(drivingId: number): Observable<TextResponse> {
    console.log(drivingId);
    return this.httpClientService.deleteT<TextResponse>(environment.apiUrl + `drivings/passenger/cancel-reservation/${drivingId}`)
  }

  deleteDriving(id:number) {
    return this.httpClientService.getT<TextResponse>(environment.apiUrl + `drivings/delete/${id}`);
  }

  startDriving() {
    return this.httpClientService.getT<TextResponse>(environment.apiUrl + 'drivings/start');
  }

  endDriving() {
    return this.httpClientService.getT<TextResponse>(environment.apiUrl + 'drivings/end');
  }

  getReservation() {
    return this.httpClientService.get(environment.apiUrl + 'drivings/reservation');
  }

  getAllCurrent() {
    return this.httpClientService.getT<DrivingWithLocations[]>(environment.apiUrl + 'drivings/all-current');
  }

  getAllCurrentBySearchTerm(term: string) {
    return this.httpClientService.getT<DrivingWithLocations[]>(environment.apiUrl + `drivings/all-current/${term}`);
  }

  findDrivingWithLocationsById(id: number) {
    return this.httpClientService.getT<DrivingWithLocations>(environment.apiUrl + `drivings/with-locations/${id}`);
  }
}
