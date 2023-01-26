import { Component, OnInit } from '@angular/core';
import {DriverService} from "../../services/driverService/driver.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-worktime',
  templateUrl: './work-time.component.html',
  styleUrls: ['./work-time.component.scss']
})
export class WorkTimeComponent implements OnInit {

  hours?: number;
  minutes?: number;

  constructor(
    private driverService: DriverService,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.driverService.getWorkingTimeForDriver()
      .subscribe({
          next: (response) => {
            let totalNumOfMinutes: number = parseInt(response.text);
            this.hours = Math.floor(totalNumOfMinutes / 60);
            this.minutes = totalNumOfMinutes % 60;
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
