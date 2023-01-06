import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {DrivingOption} from "../../model/driving/drivingOption";

@Injectable({
  providedIn: 'root'
})
export class DrivingFinderService {

  constructor(
    private http: HttpClient
  ) { }

  getSimpleDrivingOptions(latP1:number, lonP1:number, latP2:number, lonP2:number): Observable<DrivingOption[]> {
    return this.http.get<DrivingOption[]>(environment.apiUrl + `driving-finder/simple/${latP1}/${lonP1}/${latP2}/${lonP2}`);
  }
}
