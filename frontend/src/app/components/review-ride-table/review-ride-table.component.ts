import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Driving} from "../../model/driving/driving";
import {DrivingService} from "../../services/drivingService/driving.service";

@Component({
  selector: 'app-review-ride-table',
  templateUrl: './review-ride-table.component.html',
  styleUrls: ['./review-ride-table.component.scss']
})
export class ReviewRideTableComponent implements OnInit {

  displayedColumns: string[] = ['position', 'start address', 'end address', 'start time', "end time", 'passengers', 'price'];
  drivings : Driving[];

  constructor(private drivingService:DrivingService, @Inject(MAT_DIALOG_DATA) public data) {}

  ngOnInit(): void {
    this.drivingService.findAllById().subscribe((res) => {
      this.drivings = res as Driving[];
      console.log(this.drivings);
    });
  }
}
