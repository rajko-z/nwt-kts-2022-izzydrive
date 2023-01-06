import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {DrivingOption} from "../../../model/driving/drivingOption";
import {MapService} from "../../../services/mapService/map.service";
import {PlaceOnMap} from "../../../model/map/placeOnMap";

@Component({
  selector: 'app-ride-data-table',
  templateUrl: './ride-data-table.component.html',
  styleUrls: ['./ride-data-table.component.scss']
})
export class RideDataTableComponent implements OnInit, OnChanges {

  @Output() formData = new EventEmitter<void>();
  @Input() rideForm?: FormGroup;

  @Input() drivingOptions: DrivingOption[]

  selectedOption: DrivingOption;

  constructor(
    private mapService: MapService
  ) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log(this.drivingOptions);
    this.showOptionOnMap(this.drivingOptions[0]);
  }

  onSubmit() {
    this.formData.emit();
  }

  showOptionOnMap(drivingOption: DrivingOption) {
    this.selectedOption = drivingOption;
    this.mapService.removeDrawRoutes();
    this.mapService.drawRoute(drivingOption.driverToStartPath.coordinates, "#5715ff");
    this.mapService.drawRoute(drivingOption.startToEndPath.coordinates, "#d3081f");

    this.mapService.startTrackingCurrentDriverOnMap(drivingOption.driver.email);
  }

}
