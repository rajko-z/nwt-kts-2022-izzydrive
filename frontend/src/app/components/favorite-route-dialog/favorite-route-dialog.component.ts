import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RouteService} from "../../services/routeService/route.service";
import {UserService} from "../../services/userService/user-sevice.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import { TextResponse } from 'src/app/model/response/textresponse';

@Component({
  selector: 'app-favorite-route-dialog',
  templateUrl: './favorite-route-dialog.component.html',
  styleUrls: ['./favorite-route-dialog.component.scss']
})
export class FavoriteRouteDialogComponent {

  constructor(private routeService: RouteService, private userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<FavoriteRouteDialogComponent>,
                public snackBar: MatSnackBar) {}

  saveFavoriteRoute() {
    this.data.passengerId = this.userService.getCurrentUserId();
    this.routeService.addFavoriteRoute(this.data).subscribe((response:TextResponse) => {
        this.snackBar.open(response.text, "OK", {
          duration: 2000,
        })
      }
    );
    this.closeDialog()
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
