import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {debounceTime, distinctUntilChanged, map, Observable, switchMap} from "rxjs";
import {PlaceOnMap} from "../../../model/map/placeOnMap";
import {FormControl, FormGroup} from "@angular/forms";
import {AngularFireMessaging} from "@angular/fire/compat/messaging";
import {HttpClient} from "@angular/common/http";
import {MapService} from "../../../services/mapService/map.service";
import {MarkerType} from "../../../model/map/markerType";
import {DrivingFinderService} from "../../../services/drivingFinderService/driving-finder.service";
import {DrivingOption} from "../../../model/driving/drivingOption";

@Component({
  selector: 'app-ride-data-basic-form',
  templateUrl: './ride-data-basic-form.component.html',
  styleUrls: ['./ride-data-basic-form.component.scss']
})
export class RideDataBasicFormComponent implements OnInit {

  apiLoading: boolean = false;
  placeOptionsStart$: Observable<PlaceOnMap[]>;
  placeOptionsEnd$: Observable<PlaceOnMap[]>;

  startLocation: PlaceOnMap = null;
  endLocation: PlaceOnMap = null;

  @Output() drivingOptions = new EventEmitter<DrivingOption[]>();

  routeForm = new FormGroup({
    startLocation: new FormControl(''),
    endLocation: new FormControl('')
  })

  constructor(
    private msg: AngularFireMessaging,
    private http: HttpClient,
    private mapService: MapService,
    private drivingFinderService: DrivingFinderService) {
  }

  ngOnInit() {
    this.setUpPlaceOptionsStart();
    this.setUpPlaceOptionsEnd();
  }

  onSubmit() {
    if (this.startLocation == null) {
      this.routeForm.controls.startLocation.setErrors({"incorrect": true});
      return;
    }
    if (this.endLocation == null) {
      this.routeForm.controls.endLocation.setErrors({"incorrect": true});
      return;
    }
    this.apiLoading = true;
    this.drivingFinderService.getSimpleDrivingOptions(this.startLocation.latitude,this.startLocation.longitude, this.endLocation.latitude, this.endLocation.longitude)
      .subscribe(options => {
        this.apiLoading = false;
        this.drivingOptions.emit(options);
      });
  }

  private setUpPlaceOptionsStart() {
    this.placeOptionsStart$ = this.routeForm.controls.startLocation.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(text => {
          return this.getPlacesFromText(text)
        })
      );
  }

  private setUpPlaceOptionsEnd() {
    this.placeOptionsEnd$ = this.routeForm.controls.endLocation.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(text => {
          return this.getPlacesFromText(text)
        })
      );
  }

  private getPlacesFromText(text: string): Observable<PlaceOnMap[]> {
    return this.mapService.getPlacesFromText(text)
      .pipe(
        map(places =>
          places.filter(place => place.name != this.startLocation?.name &&
            place.name != this.endLocation?.name)
        )
      );
  }

  public startPlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.START
    this.startLocation = place;
    this.routeForm.controls.startLocation.disable();
    this.mapService.addPlaceOnMap(place);
  }

  public endPlaceSelected(place: PlaceOnMap) {
    place.markerType = MarkerType.END
    this.endLocation = place;
    this.routeForm.controls.endLocation.disable();
    this.mapService.addPlaceOnMap(place);
  }

  public deleteStartLocation() {
    this.mapService.removePlaceFromMap(this.startLocation);
    this.routeForm.controls.startLocation.setValue('');
    this.routeForm.controls.startLocation.enable();
    this.startLocation = null;
  }

  public deleteEndLocation() {
    this.mapService.removePlaceFromMap(this.endLocation);
    this.routeForm.controls.endLocation.setValue('');
    this.routeForm.controls.endLocation.enable();
    this.endLocation = null;
  }
}
