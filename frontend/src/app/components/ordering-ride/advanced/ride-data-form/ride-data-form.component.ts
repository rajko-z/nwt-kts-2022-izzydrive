import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {PlaceOnMap} from "../../../../model/map/placeOnMap";
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {MarkerType} from "../../../../model/map/markerType";
import {MapService} from "../../../../services/mapService/map.service";
import {
  SearchPlaceComponentService
} from "../../../../services/searchPlaceComponentService/search-place-component.service";
import {IntermediateStationsOrderType, OptimalDrivingType} from "../../../../model/driving/driving";
import {CarAccommodation} from "../../../../model/car/carAccommodation";
import {DrivingFinderRequest} from "../../../../model/driving/drivingFinderRequest.";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../../../services/drivingService/driving.service";
import {UserService} from "../../../../services/userService/user-sevice.service";
import {LoggedUserService} from "../../../../services/loggedUser/logged-user.service";
import {User} from "../../../../model/user/user";
import {addHours, addMinutes} from 'date-fns'
import {RouteService} from 'src/app/services/routeService/route.service';
import {RouteDTO} from 'src/app/model/route/route';
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';


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

  timeMax = new Date();
  minuteMax = this.timeMax.getMinutes();
  hourMax = addHours(this.timeMax, 5).getHours();
  timeNow = addMinutes(new Date(), 30);
  minuteNow =this.timeNow.getMinutes();
  hourNow = this.timeNow.getHours();
  startStr = `${this.hourNow}:${this.minuteNow}`;
  endStr = `${this.hourMax}:${this.minuteMax}`;

  @Input() scheduleRide: boolean;
  @Output() fetchedDrivingOptionsEvent = new EventEmitter<DrivingOption[]>()
  @Output() drivingFinderRequestEvent = new EventEmitter<DrivingFinderRequest>();

  users: User [];


  routeForm = new FormGroup({
    optimalDriving: new FormControl(''),
    stationsOrder: new FormControl(''),
    babyOption: new FormControl(false),
    baggageOption: new FormControl(false),
    petOption: new FormControl(false),
    foodOption: new FormControl(false),
    userEmailFriendsFirst: new FormControl('', [Validators.email, this.checkEmailWithCurrentUser(), this.checkUserHasAccount()]),
    userEmailFriendsSecond: new FormControl('', [Validators.email, this.checkEmailWithCurrentUser(), this.checkUserHasAccount()]),
    userEmailFriendsThird: new FormControl('', [Validators.email, this.checkEmailWithCurrentUser(), this.checkUserHasAccount()]),
    scheduleTime: new FormControl('')
  });

  constructor(
    public dialog: MatDialog,
    private mapService: MapService,
    private drivingService: DrivingService,
    private messageTooltip: MatSnackBar,
    private searchPlaceComponentService: SearchPlaceComponentService,
    private userService: UserService,
    private loggedUserService: LoggedUserService,
    private snackBar: MatSnackBar,
    private responseMessage: ResponseMessageService
  ) {
    this.loggedUserService.getAllUsers().subscribe((res) => {
      this.users = res;
    });
    this.checkIsOrderedFromFavoriteRides()
  }

  checkIsOrderedFromFavoriteRides(){
    if (RouteService.selectedFavouriteRides && this.userService.getCurrentUserId() in RouteService.selectedFavouriteRides){
      let route : RouteDTO = RouteService.selectedFavouriteRides[this.userService.getCurrentUserId()]
      this.startPlaceSelected(route.start)
      this.endPlaceSelected(route.end)
      for( let i = 0 ; i < route.intermediateStations.length ; i++){
          if (i === 0) this.firstIntermediatePlaceSelected(route.intermediateStations[i])
          if (i === 1) this.secondIntermediatePlaceSelected(route.intermediateStations[i])
          if (i === 2) this.thirdIntermediatePlaceSelected(route.intermediateStations[i])
      }
      delete RouteService.selectedFavouriteRides[this.userService.getCurrentUserId()];
    }
  }

  onSubmit(event) {
    event.preventDefault();

    if (!this.allFieldsValid()) {
      return;
    }

    if (this.scheduleRide && !this.checkTime()) {
      return;
    }

    let drivingFinderRequest: DrivingFinderRequest = this.createDrivingFinderRequest();

    if (this.scheduleRide) {
      this.getScheduleDrivingOptions(drivingFinderRequest);
    } else {
      this.getAdvancedDrivingOptions(drivingFinderRequest);
    }
  }

  private getScheduleDrivingOptions(drivingFinderRequest: DrivingFinderRequest) {
    this.apiLoading = true;
    this.drivingService.getScheduleDrivingOptions(drivingFinderRequest)
      .subscribe({
          next: (options) => {
            this.apiLoading = false;
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

  private getAdvancedDrivingOptions(drivingFinderRequest: DrivingFinderRequest) {
    this.apiLoading = true;
    this.drivingService.getAdvancedDrivingOptions(drivingFinderRequest)
      .subscribe({
          next: (options) => {
            this.apiLoading = false;
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

  openErrorMessage(message: string): void {
    this.responseMessage.openErrorMessage(message)
  }

  private createDrivingFinderRequest(): DrivingFinderRequest {
    let drivingFinderRequest = new DrivingFinderRequest();
    drivingFinderRequest.startLocation = this.startPlace;
    drivingFinderRequest.endLocation = this.endPlace;
    drivingFinderRequest.intermediateLocations = this.getIntermediateStations();
    drivingFinderRequest.optimalDrivingType = this.getOptimalDrivingType();
    drivingFinderRequest.intermediateStationsOrderType = this.getStationsOrderType();
    drivingFinderRequest.carAccommodation = this.getCarAccommodation();
    drivingFinderRequest.linkedPassengersEmails = this.getLinkedPassengers();
    drivingFinderRequest.reservation = this.scheduleRide;
    if (this.scheduleRide) {
        drivingFinderRequest.scheduleTime = this.getScheduleTime();
    }
    return drivingFinderRequest;
  }

  private getScheduleTime() {
    const time = this.routeForm.value.scheduleTime.split(':');
    const date = new Date();
    const dateTime = new Date(date.getFullYear(), date.getMonth(), date.getDate(), +time[0], +time[1]);
    if ((this.hourNow >= 19)) {
      if (+time[0] < 4 && +time[0] > 0) {
        dateTime.setDate(date.getDate() + 1);
      }
    }
    return this.formatDate(dateTime);
  }

  padTo2Digits(num) {
    return num.toString().padStart(2, '0');
  }

  formatDate(date) {
    return (
      [
        date.getFullYear(),
        this.padTo2Digits(date.getMonth() + 1),
        this.padTo2Digits(date.getDate()),
      ].join('-') +
      ' ' +
      [
        this.padTo2Digits(date.getHours()),
        this.padTo2Digits(date.getMinutes()),
        this.padTo2Digits(date.getSeconds()),
      ].join(':')
    );
  }

  private checkTime(): boolean {
    if (!this.routeForm.value.scheduleTime ||
        this.routeForm.value.scheduleTime === '' ||
        !this.routeForm.value.scheduleTime.includes(":") ) {
      this.openErrorMessage("Please specify time when you want to schedule ride");
      return false;
    }

    const time = this.routeForm.value.scheduleTime.split(':');
    const date = new Date();
    const dateTime = new Date(date.getFullYear(), date.getMonth(), date.getDate(), +time[0], +time[1]);
    if ((this.hourNow >= 19)) {
      if (+time[0] < 4 && +time[0] > 0) {
        dateTime.setDate(date.getDate() + 1);
      }
      const dateMin = addMinutes(new Date(), 30);
      const dateMax = addHours(new Date(), 5);
      if (dateTime < dateMin || dateTime > dateMax) {
        this.responseMessage.openErrorMessage("The ride is scheduled at least 30 minutes before and at most 5 hours before")
        return false;
      }
    }
    return true;
  }

  checkEmailWithCurrentUser(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
      if (control.value === this.userService.getCurrentUserEmail()) {
        return {'email': true};
      }
      return null;
    };
  }

  checkUserHasAccount(): ValidatorFn {
    return ((control: AbstractControl): { [key: string]: boolean } | null => {
      if (control.value === '') {
        return null;
      }
      return this.users?.find((user) => control.value === user.email) ? null : {'email': true};
    })
  }

  private getLinkedPassengers(): string[] {
    let linkedPassengers: string[] = [];
    if (this.routeForm.value.userEmailFriendsFirst != '') {
      linkedPassengers.push(this.routeForm.value.userEmailFriendsFirst);
    }
    if (this.routeForm.value.userEmailFriendsSecond != '') {
      linkedPassengers.push(this.routeForm.value.userEmailFriendsSecond);
    }
    if (this.routeForm.value.userEmailFriendsThird != '') {
      linkedPassengers.push(this.routeForm.value.userEmailFriendsThird);
    }
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

  private allFieldsValid(): boolean {
    if (this.startPlace == null || this.endPlace == null) {
      this.searchPlaceComponentService.sendLocationFieldErrorSignal();
      return false;
    }

    if (this.secondIntermediatePlace !== null && this.firstIntermediatePlace == null) {
      this.responseMessage.openErrorMessage("First intermediate stations should be selected before second");
      return false;
    }
    if (this.thirdIntermediatePlace !== null && (this.firstIntermediatePlace == null || this.secondIntermediatePlace == null)) {
      this.responseMessage.openErrorMessage("First and second intermediate stations should be selected before third");
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
    let babyOption: boolean = this.routeForm.controls.babyOption.value;
    let foodOption: boolean = this.routeForm.controls.foodOption.value;
    let baggageOption: boolean = this.routeForm.controls.baggageOption.value;
    let petOption: boolean = this.routeForm.controls.petOption.value;
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

  public deleteFirstUserClick() {
    this.routeForm.patchValue({
      userEmailFriendsFirst: ''
    });
  }

  public deleteSecondUserClick() {
    this.routeForm.patchValue({
      userEmailFriendsSecond: ''
    });
  }

  public deleteThirdUserClick() {
    this.routeForm.patchValue({
      userEmailFriendsThird: ''
    });
  }
}
