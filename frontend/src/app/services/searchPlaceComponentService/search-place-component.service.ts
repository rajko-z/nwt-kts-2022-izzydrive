import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SearchPlaceComponentService {

  locationFieldErrorSignalBS = new BehaviorSubject<boolean>(false);
  locationFieldErrorSignal = this.locationFieldErrorSignalBS.asObservable();
  constructor() { }

  sendLocationFieldErrorSignal() {
    this.locationFieldErrorSignalBS.next(true);
  }
}
