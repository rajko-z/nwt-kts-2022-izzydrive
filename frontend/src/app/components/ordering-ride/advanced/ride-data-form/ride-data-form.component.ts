import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {OtherUsersDialogComponent} from "../../../other-users-dialog/other-users-dialog.component";
import {PlaceOnMap} from "../../../../model/map/placeOnMap";
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {MarkerType} from "../../../../model/map/markerType";
import {AngularFireMessaging} from "@angular/fire/compat/messaging";
import {HttpClient} from "@angular/common/http";
import {MapService} from "../../../../services/mapService/map.service";
import {
  SearchPlaceComponentService
} from "../../../../services/searchPlaceComponentService/search-place-component.service";
import {IntermediateStationsOrderType, OptimalDrivingType} from "../../../../model/driving/driving";
import {CarAccommodation} from "../../../../model/car/carAccommodation";
import {DrivingFinderRequest} from "../../../../model/driving/drivingFinderRequest.";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../../../services/drivingService/driving.service";

@Component({
  selector: 'app-ride-data-form',
  templateUrl: './ride-data-form.component.html',
  styleUrls: ['./ride-data-form.component.scss']
})
export class RideDataFormComponent {

  apiLoading: boolean = false;

  startPlace: PlaceOnMap = null;
  endPlace: PlaceOnMap = null;
  firstIntermediatePlace: PlaceOnMap = null;
  secondIntermediatePlace: PlaceOnMap = null;
  thirdIntermediatePlace: PlaceOnMap = null;
  selectedLocations: PlaceOnMap[] = [];

  intermediatePanelOpened: boolean = false;

  @Output() fetchedDrivingOptionsEvent = new EventEmitter<DrivingOption[]>()
  @Output() drivingFinderRequestEvent = new EventEmitter<DrivingFinderRequest>();

  routeForm = new FormGroup({
    optimalDriving: new FormControl(''),
    stationsOrder: new FormControl(''),
    babyOption: new FormControl(false),
    baggageOption: new FormControl(false),
    petOption: new FormControl(false),
    foodOption: new FormControl(false),
  })

  constructor(
    public dialog: MatDialog,
    private msg: AngularFireMessaging,
    private http: HttpClient,
    private mapService: MapService,
    private drivingService: DrivingService,
    private messageTooltip: MatSnackBar,
    private searchPlaceComponentService: SearchPlaceComponentService) {
  }

  onSubmit(event) {
    event.preventDefault();

    if (!this.allFieldsValid()) {
      return;
    }

    let drivingFinderRequest: DrivingFinderRequest = this.createDrivingFinderRequest();

    console.log(drivingFinderRequest);
    this.apiLoading = true;
    this.drivingService.getAdvancedDrivingOptions(drivingFinderRequest)
      .subscribe({
          next: (options) => {
            this.apiLoading = false;
            console.log(options);
            this.fetchedDrivingOptionsEvent.emit(options);
            this.drivingFinderRequestEvent.emit(drivingFinderRequest);
          },
          error: (error) => {
            this.apiLoading = false;
            this.openErrorMessage(error.error.message);
          }
        }
      );
  }

  openErrorMessage(message: string): void{
    this.messageTooltip.open(message, 'Close', {
      horizontalPosition: "center",
      verticalPosition: "top",
      panelClass: ['messageTooltip']
    });
  }

  private createDrivingFinderRequest() : DrivingFinderRequest {
    let drivingFinderRequest = new DrivingFinderRequest();
    drivingFinderRequest.startLocation = this.startPlace;
    drivingFinderRequest.endLocation = this.endPlace;
    drivingFinderRequest.intermediateLocations = this.getIntermediateStations();
    drivingFinderRequest.optimalDrivingType = this.getOptimalDrivingType();
    drivingFinderRequest.intermediateStationsOrderType = this.getStationsOrderType();
    drivingFinderRequest.carAccommodation = this.getCarAccommodation();
    drivingFinderRequest.linkedPassengersEmails = this.getLinkedPassengers();
    return drivingFinderRequest;
  }

  private getLinkedPassengers(): string[] {
    let linkedPassengers: string[] = [];
    // TODO:: just for testing here
    //linkedPassengers.push('natasha.lakovic@gmail.com');
    return linkedPassengers;
  }

  private getIntermediateStations(): PlaceOnMap[] {
    let intermediateStations: PlaceOnMap[] = [];
    if (this.firstIntermediatePlace !== null) {
      intermediateStations.push(this.firstIntermediatePlace);
    }
    if (this.secondIntermediatePlace !== null) {
      intermediateStations.push(this.secondIntermediatePlace);
    }
    if (this.thirdIntermediatePlace !== null) {
      intermediateStations.push(this.thirdIntermediatePlace);
    }
    return intermediateStations;
  }

  private allFieldsValid() : boolean {
    if (this.startPlace == null || this.endPlace == null) {
      this.searchPlaceComponentService.sendLocationFieldErrorSignal();
      return false;
    }
    return true;
  }

  private getStationsOrderType(): IntermediateStationsOrderType {
    let stationsOrder = this.routeForm.controls.stationsOrder.value;
    if (stationsOrder === 'system') {
      return IntermediateStationsOrderType.SYSTEM_CALCULATE;
    }
    return IntermediateStationsOrderType.IN_ORDER;
  }

  private getOptimalDrivingType(): OptimalDrivingType {
    let optimalDriving = this.routeForm.controls.optimalDriving.value;
    if (optimalDriving === 'cheapest') {
      return OptimalDrivingType.CHEAPEST_RIDE;
    } else if (optimalDriving === 'travelTime') {
      return OptimalDrivingType.SHORTEST_TRAVEL_TIME;
    }
    return OptimalDrivingType.NO_PREFERENCE;
  }

  private getCarAccommodation(): CarAccommodation {
    let babyOption   : boolean = this.routeForm.controls.babyOption.value;
    let foodOption   : boolean = this.routeForm.controls.foodOption.value;
    let baggageOption: boolean = this.routeForm.controls.baggageOption.value;
    let petOption    : boolean = this.routeForm.controls.petOption.value;
    return new CarAccommodation(foodOption, petOption, baggageOption, babyOption);
  }

  public startPlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.START
    this.startPlace = place;
    this.mapService.addPlaceOnMap(place);
    this.selectedLocations.push(this.startPlace);
  }
  public startPlaceDeleted() {
    this.mapService.removePlaceFromMap(this.startPlace);
    this.selectedLocations = this.selectedLocations.filter(l => l != this.startPlace);
    this.startPlace = null;
  }

  public endPlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.END
    this.endPlace = place;
    this.mapService.addPlaceOnMap(place);
    this.selectedLocations.push(this.endPlace);
  }

  public endPlaceDeleted() {
    this.mapService.removePlaceFromMap(this.endPlace);
    this.selectedLocations = this.selectedLocations.filter(l => l != this.endPlace);
    this.endPlace = null;
  }

  public firstIntermediatePlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.FIRST_INTERMEDIATE
    this.firstIntermediatePlace = place;
    this.mapService.addPlaceOnMap(place);
    this.selectedLocations.push(this.firstIntermediatePlace);
  }

  public firstIntermediatePlaceDeleted() {
    this.mapService.removePlaceFromMap(this.firstIntermediatePlace);
    this.selectedLocations = this.selectedLocations.filter(l => l != this.firstIntermediatePlace);
    this.firstIntermediatePlace = null;
  }

  public secondIntermediatePlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.SECOND_INTERMEDIATE
    this.secondIntermediatePlace = place;
    this.mapService.addPlaceOnMap(place);
    this.selectedLocations.push(this.secondIntermediatePlace);
  }

  public secondIntermediatePlaceDeleted() {
    this.mapService.removePlaceFromMap(this.secondIntermediatePlace);
    this.selectedLocations = this.selectedLocations.filter(l => l != this.secondIntermediatePlace);
    this.secondIntermediatePlace = null;
  }

  public thirdIntermediatePlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.THIRD_INTERMEDIATE
    this.thirdIntermediatePlace = place;
    this.mapService.addPlaceOnMap(place);
    this.selectedLocations.push(this.thirdIntermediatePlace);
  }

  public thirdIntermediatePlaceDeleted() {
    this.mapService.removePlaceFromMap(this.thirdIntermediatePlace);
    this.selectedLocations = this.selectedLocations.filter(l => l != this.thirdIntermediatePlace);
    this.thirdIntermediatePlace = null;
  }

  openDialogOtherUsers() {
    this.dialog.open(OtherUsersDialogComponent);
  }

  // openDialog() {
  //   this.dialog.open(FavoriteRouteDialogComponent, {
  //     data: {startLocation: this.routeForm.value.startLocation, endLocation: this.routeForm.value.endLocation},
  //   });
  // }


}
