import {Component, Input, OnInit} from '@angular/core';
import {DrivingWithLocations} from "../../model/driving/driving";
import {MatDialog} from "@angular/material/dialog";
import {ExplanationDialogComponent} from "../explanation-dialog/explanation-dialog.component";
import {DrivingService} from "../../services/drivingService/driving.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FinishDrivingCheckComponent} from "../finish-driving-check/finish-driving-check.component";
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-display-driving',
  templateUrl: './display-driving.component.html',
  styleUrls: ['./display-driving.component.scss']
})
export class DisplayDrivingComponent {

  @Input()
  currDrivingStatus?: string;
  userProfilePhotos: SafeResourceUrl[] = [];

  @Input()
  driving?: DrivingWithLocations;

  constructor(
    public dialog: MatDialog,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService,
    private userService: UserService,
    private _sanitizer: DomSanitizer,
    private responseMessage: ResponseMessageService) {
  }

  ngOnChanges(): void {
    if(this.driving){
      this.setProfilePhotos();
    }
  }

  cancelDriving(reservation: boolean) {
    this.dialog.open(ExplanationDialogComponent, {
      data: {
        drivingId: this.driving.id,
        reservation: reservation
      }
    });
  }

  endDriving() {
    this.snackBar.openFromComponent(FinishDrivingCheckComponent, {
      data: {
        preClose: () => {
          this.snackBar.dismiss()
        }
      },
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
    });
  }

  startDriving() {
    this.drivingService.startDriving()
      .subscribe({
          next: (_) => {
            this.currDrivingStatus = 'active';
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

  setProfilePhotos(){
    this.driving.passengers.forEach((passenger) => {
      this.userService.getUserDataWithImage(passenger).subscribe({
        next: (response) => {
          this.userProfilePhotos.push(response.imageName?  this._sanitizer.bypassSecurityTrustResourceUrl(`data:image/png;base64, ${response.imageName}`) : null);
        },
        error: (error) => {
          this.responseMessage.openErrorMessage(error.error.message)
        }
      })
    })
  }
}
