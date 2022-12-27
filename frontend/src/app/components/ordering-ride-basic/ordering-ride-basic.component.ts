import {Component, OnInit} from '@angular/core';
import {ReviewRideTableComponent} from "../review-ride-table/review-ride-table.component";
import {MatDialog} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {FavoriteRouteDialogComponent} from "../favorite-route-dialog/favorite-route-dialog.component";
import {AngularFireMessaging} from '@angular/fire/compat/messaging';
import {HttpClient} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent implements OnInit {

  routeForm = new FormGroup({
    startLocation: new FormControl(''),
    endLocation: new FormControl('')
  }) //ne znam da li ce ovde trebati neka provera

  constructor(public dialog: MatDialog, private msg: AngularFireMessaging, private http: HttpClient,  public snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.msg.messages.subscribe((message: any) => {
      console.log('Foreground message: ' + message)
    }) //lisen for message
    this.msg.requestToken.subscribe(token => {
      //upload token to server

      console.log(token);
      this.http.post('https://localhost:8443/izzydrive/v1/messages/send-notification', {
        target: token,
      }).subscribe((res) =>
        console.log(res)
      );

      this.http.post('https://localhost:8443/izzydrive/v1/messages/send-notification', {
        subscriber: token
      }).subscribe(() => {
      });

    }, error => {

      console.log(error);

    });

  }

  onSubmit() {
  }

}
