import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog} from "@angular/material/dialog";
import {WorkTimeComponent} from "../../worktime/work-time.component";

@Component({
  selector: 'app-driver-menu',
  templateUrl: './driver-menu.component.html',
  styleUrls: ['./driver-menu.component.scss', '../menu.component.scss']
})
export class DriverMenuComponent {

  constructor(private router: Router, private matDialog: MatDialog) { }

  onDrivingHistory() {
    this.router.navigateByUrl("/user/driving-history");
  }

  onCurrentDrivingsClick() {
    this.router.navigateByUrl("/driver/current-drivings");
  }

  onReservationClick() {
    this.router.navigateByUrl("/driver/reservation");
  }

  onCarInfo(){
    this.router.navigateByUrl("/driver/car-data")
  }

  onWorkTimeClick() {
    this.matDialog.open(WorkTimeComponent);
  }
}
