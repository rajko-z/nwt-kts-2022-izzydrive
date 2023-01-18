import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {MapService} from "../../../../services/mapService/map.service";
import {CarType} from "../../../../model/car/carType";

@Component({
  selector: 'app-ride-data-table',
  templateUrl: './ride-data-table.component.html',
  styleUrls: ['./ride-data-table.component.scss']
})
export class RideDataTableComponent implements OnInit, OnChanges {

  @Input() drivingOptions: DrivingOption[]

  @Output() selectedOptionEvent = new EventEmitter<DrivingOption>();

  selectedOption: DrivingOption;

  constructor(private mapService: MapService) {}

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.drivingOptions !== undefined && this.drivingOptions.length > 0) {
      this.showOptionOnMap(this.drivingOptions[0]);
    }
  }

  onSubmit() {
    this.selectedOptionEvent.emit(this.selectedOption);
  }

  showOptionOnMap(drivingOption: DrivingOption) {
    this.selectedOption = drivingOption;
    this.mapService.removeDrawRoutes();
    if(!drivingOption.reservation){
      this.mapService.drawRoute(drivingOption.driverToStartPath.coordinates, "#5715ff");
    }
    this.mapService.drawRoute(drivingOption.startToEndPath.coordinates, "#d3081f");

    this.mapService.startTrackingCurrentDriverOnMap(drivingOption.driver.email);
  }

}
