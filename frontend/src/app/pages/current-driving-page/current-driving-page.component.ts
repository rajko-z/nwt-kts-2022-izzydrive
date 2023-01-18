import {Component, OnInit} from '@angular/core';
import {PassengerService} from "../../services/passengerService/passenger.service";
import {Router} from "@angular/router";
import {DrivingState, DrivingWithLocations} from "../../model/driving/driving";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MapService} from "../../services/mapService/map.service";
import {PlaceOnMap} from "../../model/map/placeOnMap";
import {MarkerType} from "../../model/map/markerType";

@Component({
  selector: 'app-current-driving-page',
  templateUrl: './current-driving-page.component.html',
  styleUrls: ['./current-driving-page.component.scss']
})
export class CurrentDrivingPageComponent implements OnInit {

  currentDriving: DrivingWithLocations;

  constructor(
    private passengerService: PassengerService,
    private router: Router,
    private snackBar: MatSnackBar,
    private mapService: MapService
  ) { }

  ngOnInit(): void {
    this.passengerService.findCurrentDrivingWithLocations()
      .subscribe({
          next: (driving) => {
            if (driving) {
              if (driving.drivingState === DrivingState.PAYMENT) {
                this.router.navigateByUrl('/passenger/payment');
              } else if (driving.drivingState === DrivingState.ACTIVE || driving.drivingState === DrivingState.WAITING){
                this.currentDriving = driving;
                this.setUpMap();
              }
            } else {
              this.router.navigateByUrl('/passenger/order-now');
            }
          }
        }
      );
  }

  setUpMap(): void {
    this.mapService.resetEverythingOnMap();
    console.log(this.currentDriving);
    this.addStartPlace();
    this.addEndPlace();
    this.addFirstIntermediate();
    this.addSecondIntermediate();
    this.addThirdIntermediate();
    this.mapService.startTrackingCurrentDriverOnMap(this.currentDriving.driver.email);
    this.mapService.drawRoute(this.currentDriving.fromDriverToStart.coordinates, "#5715ff");
    this.mapService.drawRoute(this.currentDriving.fromStartToEnd.coordinates, "#d3081f");
  }

  addStartPlace() {
    let startPlace: PlaceOnMap = this.currentDriving.route.start;
    startPlace.markerType = MarkerType.START;
    this.mapService.addPlaceOnMap(this.currentDriving.route.start);
  }
  addEndPlace() {
    let endPlace: PlaceOnMap = this.currentDriving.route.end;
    endPlace.markerType = MarkerType.END;
    this.mapService.addPlaceOnMap(this.currentDriving.route.end);
  }
  addFirstIntermediate() {
    let inter: PlaceOnMap[] = this.currentDriving.route.intermediateStations;
    if (inter.length >= 1) {
      let place: PlaceOnMap = inter.at(0);
      place.markerType = MarkerType.FIRST_INTERMEDIATE;
      this.mapService.addPlaceOnMap(place);
    }
  }
  addSecondIntermediate() {
    let inter: PlaceOnMap[] = this.currentDriving.route.intermediateStations;
    if (inter.length >= 2) {
      let place: PlaceOnMap = inter.at(1);
      place.markerType = MarkerType.SECOND_INTERMEDIATE;
      this.mapService.addPlaceOnMap(place);
    }
  }
  addThirdIntermediate() {
    let inter: PlaceOnMap[] = this.currentDriving.route.intermediateStations;
    if (inter.length === 3) {
      let place: PlaceOnMap = inter.at(2);
      place.markerType = MarkerType.THIRD_INTERMEDIATE;
      this.mapService.addPlaceOnMap(place);
    }
  }
}
