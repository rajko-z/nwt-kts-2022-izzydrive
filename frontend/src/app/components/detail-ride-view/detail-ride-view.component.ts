import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../services/drivingService/driving.service";
import {UserService} from "../../services/userService/user-sevice.service";
import {FinishedDrivingDetails} from "../../model/driving/driving";
import {MapService} from "../../services/mapService/map.service";

@Component({
  selector: 'app-detail-ride-view',
  templateUrl: './detail-ride-view.component.html',
  styleUrls: ['./detail-ride-view.component.scss']
})
export class DetailRideViewComponent implements OnInit {

  driving?: FinishedDrivingDetails;

  isDriverView?: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) public drivingId : number,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService,
    private userService: UserService,
    private mapService: MapService
  ) { }

  ngOnInit(): void {
    if (this.userService.getRoleCurrentUserRole() === 'ROLE_DRIVER') {
      this.isDriverView = true;
    }

    this.drivingService.findFinishedDrivingDetailsById(this.drivingId)
      .subscribe({
          next: (response) => {
            this.driving = response;
            this.showOnMap();
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

  showOnMap() {
    this.mapService.resetEverythingOnMap();
    this.mapService.addStartPlace(this.driving.route.start);
    this.mapService.addEndPlace(this.driving.route.end);
    this.mapService.addIntermediateLocations(this.driving.route.intermediateStations);
    this.mapService.drawRoute(this.driving.fromStartToEnd.coordinates, "#d3081f");
  }

}
