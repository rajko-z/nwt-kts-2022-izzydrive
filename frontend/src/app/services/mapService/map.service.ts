import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, of} from "rxjs";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {environment} from "../../../environments/environment";
import {Location} from "../../model/map/location";
import {DrawnRoute} from "../../model/map/drawnRoute";
import {DriverLocation} from "../../model/driver/driverLocation";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private placeToAddBS = new BehaviorSubject<PlaceOnMap>(null);
  private placeToRemoveBS = new BehaviorSubject<PlaceOnMap>(null);
  private routeToDrawBS = new BehaviorSubject<DrawnRoute>(null);
  private drawnRoutesToRemoveBS = new BehaviorSubject<void>(null);
  private currentDriverEmailToTrackBS = new BehaviorSubject<string>(null);
  placeToAdd = this.placeToAddBS.asObservable();
  placeToRemove = this.placeToRemoveBS.asObservable();
  routeToDraw = this.routeToDrawBS.asObservable();
  drawnRoutesToRemove = this.drawnRoutesToRemoveBS.asObservable();
  currentDriverEmailToTrack = this.currentDriverEmailToTrackBS.asObservable();

  constructor(private http: HttpClient) { }

  getPlacesFromText(text: string): Observable<PlaceOnMap[]> {
    if (!text.trim()) {
      return of([]);
    }
    return this.http.get<PlaceOnMap[]>(environment.apiUrl + `maps/address-from-text/${text}`);
  }

  getAllDriverLocations(): Observable<DriverLocation[]> {
    return this.http.get<DriverLocation[]>(environment.apiUrl + 'drivers/current-locations');
  }

  addPlaceOnMap(place: PlaceOnMap) {
    this.placeToAddBS.next(place);
  }

  removePlaceFromMap(place: PlaceOnMap) {
    this.placeToRemoveBS.next(place);
  }

  drawRoute(coordinates: Location[], color: string) {
    this.routeToDrawBS.next(new DrawnRoute(coordinates, color));
  }

  removeDrawRoutes() {
    this.drawnRoutesToRemoveBS.next();
  }

  startTrackingCurrentDriverOnMap(driverEmail: string) {
    this.currentDriverEmailToTrackBS.next(driverEmail);
  }
}
