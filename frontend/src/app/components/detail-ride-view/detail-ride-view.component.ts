import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../services/drivingService/driving.service";
import {UserService} from "../../services/userService/user-sevice.service";
import {DrivingDetails} from "../../model/driving/driving";
import {MapService} from "../../services/mapService/map.service";
import {RouteDTO} from "../../model/route/route";
import {RouteService} from "../../services/routeService/route.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-detail-ride-view',
  templateUrl: './detail-ride-view.component.html',
  styleUrls: ['./detail-ride-view.component.scss']
})
export class DetailRideViewComponent implements OnInit {

  driving?: DrivingDetails;

  isDriverView?: boolean;

  isPassengerView?: boolean;

  constructor(
    private dialogRef: MatDialogRef<DrivingDetails>,
    @Inject(MAT_DIALOG_DATA) public drivingId : number,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService,
    private userService: UserService,
    private mapService: MapService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.userService.getRoleCurrentUserRole() === 'ROLE_DRIVER') {
      this.isDriverView = true;
    } else if (this.userService.getRoleCurrentUserRole() === 'ROLE_PASSENGER') {
      this.isPassengerView = true;
    }

    this.drivingService.findDrivingDetailsById(this.drivingId)
      .subscribe({
          next: (response) => {
            this.driving = response;
            console.log(response);
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

  getRide(route : RouteDTO, forNow: boolean){
    if (RouteService.selectedFavouriteRides){
      RouteService.selectedFavouriteRides[this.userService.getCurrentUserId()] = route;
    }
    else {
      let id : number = this.userService.getCurrentUserId();
      RouteService.selectedFavouriteRides = { [id]: route }
    }
    if (forNow) {
      this.router.navigateByUrl("/passenger/order-now");
    } else {
      this.router.navigateByUrl("/passenger/order-for-later");
    }
    this.dialogRef.close();
  }

}
