import {Component, EventEmitter, Output} from '@angular/core';
import {PlaceOnMap} from "../../../../model/map/placeOnMap";
import {AngularFireMessaging} from "@angular/fire/compat/messaging";
import {HttpClient} from "@angular/common/http";
import {MapService} from "../../../../services/mapService/map.service";
import {MarkerType} from "../../../../model/map/markerType";
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {
  SearchPlaceComponentService
} from "../../../../services/searchPlaceComponentService/search-place-component.service";
import {DrivingService} from "../../../../services/drivingService/driving.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-ride-data-basic-form',
  templateUrl: './ride-data-basic-form.component.html',
  styleUrls: ['./ride-data-basic-form.component.scss']
})
export class RideDataBasicFormComponent {

  apiLoading: boolean = false;

  startPlace: PlaceOnMap = null;
  endPlace: PlaceOnMap = null;
  selectedLocations: PlaceOnMap[] = [];

  @Output() fetchedDrivingOptionsEvent = new EventEmitter<DrivingOption[]>()

  constructor(
    private msg: AngularFireMessaging,
    private http: HttpClient,
    private mapService: MapService,
    private drivingService: DrivingService,
    private snackBar: MatSnackBar,
    private responsemessage: ResponseMessageService,
    private searchPlaceComponentService: SearchPlaceComponentService) {
  }

  onSubmit(event) {
    event.preventDefault();

    if (this.startPlace == null || this.endPlace == null) {
      this.searchPlaceComponentService.sendLocationFieldErrorSignal();
      return;
    }

    this.apiLoading = true;
    this.drivingService.getSimpleDrivingOptions([this.startPlace, this.endPlace])
      .subscribe({
          next: (options) => {
            this.apiLoading = false;
            this.fetchedDrivingOptionsEvent.emit(options);
          },
          error: (error) => {
            this.apiLoading = false;
            this.responsemessage.openErrorMessage(error.error.message)
         }
        }
      );
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

}
