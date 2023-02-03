import {Component, Input} from '@angular/core';
import {DrivingDetails} from "../../../model/driving/driving";
import {MatDialogRef} from "@angular/material/dialog";
import {UserService} from "../../../services/userService/user-sevice.service";
import {Router} from "@angular/router";
import {RouteDTO} from "../../../model/route/route";
import {RouteService} from "../../../services/routeService/route.service";

@Component({
  selector: 'app-detail-ride-info',
  templateUrl: './detail-ride-info.component.html',
  styleUrls: ['./detail-ride-info.component.scss']
})
export class DetailRideInfoComponent {

  @Input()
  driving?: DrivingDetails;

  @Input()
  isDriverView?: boolean;

  @Input()
  isPassengerView?: boolean;

  constructor(
    private dialogRef: MatDialogRef<DrivingDetails>,
    private userService: UserService,
    private router: Router,
  ) { }

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
