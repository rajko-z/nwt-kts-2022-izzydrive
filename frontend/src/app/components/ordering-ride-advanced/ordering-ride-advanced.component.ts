import { Component } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {
  IntermediateStationsDialogComponent
} from "../intermediate-stations-dialog/intermediate-stations-dialog.component";
import {OtherUsersDialogComponent} from "../other-users-dialog/other-users-dialog.component";
import {FavoriteRouteDialogComponent} from "../favorite-route-dialog/favorite-route-dialog.component";

@Component({
  selector: 'app-ordering-ride-advanced',
  templateUrl: './ordering-ride-advanced.component.html',
  styleUrls: ['./ordering-ride-advanced.component.scss']
})
export class OrderingRideAdvancedComponent {

  routeForm = new FormGroup({
    startLocation: new FormControl(''),
    endLocation: new FormControl(''),
    optimalDriving: new FormControl(''),
    babyOption: new FormControl(false),
    baggageOption: new FormControl(false),
    petOption: new FormControl(false),
    foodOption: new FormControl(false),
  })

  constructor(public dialog: MatDialog) { }

  onSubmit(){
    console.log("Submit");
    console.log(this.routeForm);
  }

  openDialogIntermediateStations() {
    this.dialog.open(IntermediateStationsDialogComponent);
  }

  openDialogOtherUsers() {
    this.dialog.open(OtherUsersDialogComponent);
  }

  openDialog(){
    this.dialog.open(FavoriteRouteDialogComponent, {
      data: { startLocation: this.routeForm.value.startLocation, endLocation: this.routeForm.value.endLocation},
    });
  }
}
