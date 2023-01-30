import {Component, Input, OnInit} from '@angular/core';
import {DrivingWithLocations} from "../../model/driving/driving";
import {MatDialog} from "@angular/material/dialog";
import {ExplanationDialogComponent} from "../explanation-dialog/explanation-dialog.component";
import {DrivingService} from "../../services/drivingService/driving.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FinishDrivingCheckComponent} from "../finish-driving-check/finish-driving-check.component";

@Component({
  selector: 'app-display-driving',
  templateUrl: './display-driving.component.html',
  styleUrls: ['./display-driving.component.scss']
})
export class DisplayDrivingComponent implements OnInit {

  @Input()
  currDrivingStatus?: string;

  @Input()
  driving?: DrivingWithLocations;

  constructor(
    public dialog: MatDialog,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService) {
  }

  ngOnInit(): void {
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
}
