import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {MapService} from "../../../../services/mapService/map.service";
import {IntermediateStationsOrderType} from "../../../../model/driving/driving";
import {DrivingFinderRequest} from "../../../../model/driving/drivingFinderRequest.";
import {MarkerType} from "../../../../model/map/markerType";
import {PlaceOnMap} from "../../../../model/map/placeOnMap";

@Component({
  selector: 'app-ride-data-table',
  templateUrl: './ride-data-table.component.html',
  styleUrls: ['./ride-data-table.component.scss']
})
export class RideDataTableComponent implements OnInit, OnChanges {

  @Input() drivingOptions: DrivingOption[]
  @Input() drivingFinderRequest?: DrivingFinderRequest;

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
    this.mapService.drawRoute(drivingOption.driverToStartPath.coordinates, "#5715ff");
    this.mapService.drawRoute(drivingOption.startToEndPath.coordinates, "#d3081f");
    this.mapService.startTrackingCurrentDriverOnMap(drivingOption.driver.email);

    if (this.drivingFinderRequest && drivingOption.startToEndPath.reorderedIntermediate) {
      this.showReorderedIntermediateStations(drivingOption.startToEndPath.reorderedIntermediate);
    }
  }

  showReorderedIntermediateStations(newOrderedStations: PlaceOnMap[]) {
    if (this.drivingFinderRequest.intermediateStationsOrderType === IntermediateStationsOrderType.SYSTEM_CALCULATE) {
      let stations = this.drivingFinderRequest.intermediateLocations;
      if (stations && stations.length <= 1) {
        return;
      }
      this.removeOldIntermediateStations(stations);
      this.addNewIntermediateStations(newOrderedStations);
    }
  }
  addNewIntermediateStations(stations: PlaceOnMap[]) {
    let firstI: PlaceOnMap = stations.at(0);
    firstI.markerType = MarkerType.FIRST_INTERMEDIATE;
    this.mapService.addPlaceOnMap(firstI);

    let secondI: PlaceOnMap = stations.at(1);
    secondI.markerType = MarkerType.SECOND_INTERMEDIATE;
    this.mapService.addPlaceOnMap(secondI);

    if (stations.length == 3) {
      let thirdI: PlaceOnMap = stations.at(2);
      thirdI.markerType = MarkerType.THIRD_INTERMEDIATE;
      this.mapService.addPlaceOnMap(thirdI);
    }
  }

  removeOldIntermediateStations(stations: PlaceOnMap[]) {
    let firstI: PlaceOnMap = stations.at(0);
    firstI.markerType = MarkerType.FIRST_INTERMEDIATE;
    this.mapService.removePlaceFromMap(firstI);

    let secondI: PlaceOnMap = stations.at(1);
    secondI.markerType = MarkerType.SECOND_INTERMEDIATE;
    this.mapService.removePlaceFromMap(secondI);

    if (stations.length == 3) {
      let thirdI: PlaceOnMap = stations.at(2);
      thirdI.markerType = MarkerType.THIRD_INTERMEDIATE;
      this.mapService.removePlaceFromMap(thirdI);
    }
  }
}
