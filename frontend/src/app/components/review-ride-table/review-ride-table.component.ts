import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Driving} from "../../model/driving/driving";
import {DrivingService} from "../../services/drivingService/driving.service";
import {Role} from "../../model/user/role";

@Component({
  selector: 'app-review-ride-table',
  templateUrl: './review-ride-table.component.html',
  styleUrls: ['./review-ride-table.component.scss']
})
export class ReviewRideTableComponent implements OnInit {

  displayedColumns: string[] = ['position', 'start address', 'end address', 'start time', "end time", 'passengers', 'price'];
  drivings : Driving[] = [];

  constructor(private drivingService:DrivingService, @Inject(MAT_DIALOG_DATA) public data) {}

  ngOnInit(): void {
    if(this.data?.role === Role.ROLE_DRIVER){
      this.drivingService.findAllByDriverId(this.data.id).subscribe((res) => {
        this.drivings = res as Driving[];
      });
    }else if(this.data?.role === Role.ROLE_PASSENGER){
      this.drivingService.findAllByPassengerId(this.data.id).subscribe((res) => {
        this.drivings = res as Driving[];
      });
    }

  }
}
