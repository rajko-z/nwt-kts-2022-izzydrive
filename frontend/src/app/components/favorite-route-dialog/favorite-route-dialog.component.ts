import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RouteService} from "../../services/routeService/route.service";
import {UserService} from "../../services/userService/user-sevice.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {TextResponse} from 'src/app/model/response/textresponse';
import {FavoriteRoute} from "../../model/route/favoriteRoute";

@Component({
  selector: 'app-favorite-route-dialog',
  templateUrl: './favorite-route-dialog.component.html',
  styleUrls: ['./favorite-route-dialog.component.scss']
})
export class FavoriteRouteDialogComponent {

  constructor(
    private routeService: RouteService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: FavoriteRoute,
    private dialogRef: MatDialogRef<FavoriteRouteDialogComponent>,
    public snackBar: MatSnackBar
  ) {
  }

  saveFavoriteRoute() {
    this.routeService.addFavoriteRoute(this.data.routeId).subscribe((response: TextResponse) => {
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
