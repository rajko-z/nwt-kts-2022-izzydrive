import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, of} from "rxjs";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {environment} from "../../../environments/environment";
import {Location} from "../../model/map/location";
import {DrawnRoute} from "../../model/map/drawnRoute";
import {DriverLocation} from "../../model/driver/driverLocation";
import {DrivingWithLocations} from "../../model/driving/driving";
import {MarkerType} from "../../model/map/markerType";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private placeToAddBS = new BehaviorSubject<PlaceOnMap>(null);
  private placeToRemoveBS = new BehaviorSubject<PlaceOnMap>(null);
  private routeToDrawBS = new BehaviorSubject<DrawnRoute>(null);
  private drawnRoutesToRemoveBS = new BehaviorSubject<void>(null);
  private currentDriverEmailToTrackBS = new BehaviorSubject<string>(null);
  private toResetMapViewBS = new BehaviorSubject<void>(null);
  placeToAdd = this.placeToAddBS.asObservable();
  placeToRemove = this.placeToRemoveBS.asObservable();
  routeToDraw = this.routeToDrawBS.asObservable();
  drawnRoutesToRemove = this.drawnRoutesToRemoveBS.asObservable();
  currentDriverEmailToTrack = this.currentDriverEmailToTrackBS.asObservable();
  toResetMapView = this.toResetMapViewBS.asObservable();

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

  resetEverythingOnMap() {
    this.toResetMapViewBS.next();
  }

  addAllFromDriving(driving: DrivingWithLocations) {
    let startPlace: PlaceOnMap = driving.route.start;
    startPlace.markerType = MarkerType.START;
    this.addPlaceOnMap(driving.route.start);

    let endPlace: PlaceOnMap = driving.route.end;
    endPlace.markerType = MarkerType.END;
    this.addPlaceOnMap(driving.route.end);

    let inter: PlaceOnMap[] = driving.route.intermediateStations;
    if (inter.length >= 1) {
      let place: PlaceOnMap = inter.at(0);
      place.markerType = MarkerType.FIRST_INTERMEDIATE;
      this.addPlaceOnMap(place);
    }
    if (inter.length >= 2) {
      let place: PlaceOnMap = inter.at(1);
      place.markerType = MarkerType.SECOND_INTERMEDIATE;
      this.addPlaceOnMap(place);
    }
    if (inter.length === 3) {
      let place: PlaceOnMap = inter.at(2);
      place.markerType = MarkerType.THIRD_INTERMEDIATE;
      this.addPlaceOnMap(place);
    }
    this.startTrackingCurrentDriverOnMap(driving.driver.email);
    this.drawRoute(driving.fromDriverToStart.coordinates, "#5715ff");
    this.drawRoute(driving.fromStartToEnd.coordinates, "#d3081f");
  }

}
