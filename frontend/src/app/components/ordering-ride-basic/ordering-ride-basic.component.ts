import { Component } from '@angular/core';
import {ReviewRideTableComponent} from "../review-ride-table/review-ride-table.component";
import {MatDialog} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {FavoriteRouteDialogComponent} from "../favorite-route-dialog/favorite-route-dialog.component";

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent  {

  routeForm = new FormGroup({
    startLocation: new FormControl(''),
    endLocation: new FormControl('')
  }) //ne znam da li ce ovde trebati neka provera

  constructor(public dialog: MatDialog) { }

  onSubmit(){}

}
