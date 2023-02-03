import {Component, OnInit} from '@angular/core';
import {DrivingWithLocations} from "../../../model/driving/driving";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {ResponseMessageService} from "../../../services/response-message/response-message.service";
import {MapService} from "../../../services/mapService/map.service";

@Component({
  selector: 'app-admin-left-part-home',
  templateUrl: './admin-left-part-home.component.html',
  styleUrls: ['./admin-left-part-home.component.scss']
})
export class AdminLeftPartHomeComponent implements OnInit {

  drivings?: DrivingWithLocations[]

  constructor(
    private drivingService: DrivingService,
    private responseMessage: ResponseMessageService,
    private mapService: MapService) { }

  ngOnInit(): void {
    this.loadAllCurrentDrivings();
  }

  loadAllCurrentDrivings() {
    this.drivingService.getAllCurrent()
      .subscribe({
          next: (drivings) => {
            this.drivings = drivings;
          },
          error: (error) => {
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }

  onSearchResult(drivings: DrivingWithLocations[]) {
    this.drivings = drivings
  }

  showDrivingOnMap(id: number) {
    this.drivingService.findDrivingWithLocationsById(id)
      .subscribe({
          next: (driving) => {
            this.mapService.resetEverythingOnMap();
            this.mapService.addAllFromDriving(driving);
          },
          error: (_) => {
            this.responseMessage.openErrorMessage("Can't find this driving. It is possible that this driving is canceled, please refresh page to load new current drivings")
          }
        }
      );
  }

}
