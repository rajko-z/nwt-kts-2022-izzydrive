import {Component, ElementRef, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {debounceTime, distinctUntilChanged, map, Observable, switchMap} from "rxjs";
import {PlaceOnMap} from "../../../../model/map/placeOnMap";
import {FormControl} from "@angular/forms";
import {MapService} from "../../../../services/mapService/map.service";
import {SearchPlaceComponentService} from "../../../../services/searchPlaceComponentService/search-place-component.service";

@Component({
  selector: 'app-search-place',
  templateUrl: './search-place.component.html',
  styleUrls: ['./search-place.component.scss']
})
export class SearchPlaceComponent implements OnInit {

  @Input() alreadySelectedPlaces: PlaceOnMap[];

  @Input() typeOfPlace: string;

  @Input() isPlaceRequired: boolean = true;

  @Output() placeSelectedEvent = new EventEmitter<PlaceOnMap>;

  @Output() placeRemovedEvent = new EventEmitter<void>;

  @ViewChildren('inputField') inputField: QueryList<ElementRef>;

  @Input() placeFromFavouriteRoute : PlaceOnMap;

  placeOptions$: Observable<PlaceOnMap[]>;

  isPlaceSelected: boolean = false;
  placeFormControl: FormControl = new FormControl('');

  constructor(
    private mapService: MapService,
    private searchPlaceComponentService: SearchPlaceComponentService
  ){}

  ngOnInit() {
    this.searchPlaceComponentService.locationFieldErrorSignal.subscribe(s => s === true && this.setErrorMessageToField());
    this.setUpPlaceOptions();
    if (this.placeFromFavouriteRoute) {
      this.placeFormControl.setValue(this.placeFromFavouriteRoute.name)
      this.placeSelectedEvent.emit(this.placeFromFavouriteRoute)
    }
  }

  private setErrorMessageToField() {
    if (!this.isPlaceSelected && this.isPlaceRequired) {
      let inputField = this.inputField.first.nativeElement as HTMLElement;
      inputField.focus();
      this.placeFormControl.setErrors({"incorrect": true});
    }
  }

  private setUpPlaceOptions() {
    this.placeOptions$ = this.placeFormControl.valueChanges
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
          places.filter(place => !this.placeAlreadySelected(place))
        )
      );
  }
  private placeAlreadySelected(place: PlaceOnMap): boolean {
    return this.alreadySelectedPlaces.find(p => p.name === place.name) !== undefined;
  }

  public placeSelectedClick(place: PlaceOnMap): void {
    this.placeFormControl.disable();
    this.isPlaceSelected = true;
    this.placeSelectedEvent.emit(place);
  }

  public deletePlaceClick(): void {
    this.placeFormControl.setValue('');
    this.placeFormControl.enable();
    this.isPlaceSelected = false;
    this.placeRemovedEvent.emit();
  }

}
