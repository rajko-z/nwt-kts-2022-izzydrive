import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Driving } from 'src/app/model/driving/driving';
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { EvaluationComponent } from './evaluation/evaluation.component';

@Component({
  selector: 'app-driving-history',
  templateUrl: './driving-history.component.html',
  styleUrls: ['./driving-history.component.scss']
})
export class DrivingHistoryComponent implements OnInit {

  constructor(private drivingService : DrivingService, private userService : UserService, public dialog: MatDialog) { }

  drivings : Driving[] = [];

  ngOnInit(): void {
    this.drivingService.getDrivingsHistoryForPassenger(this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
        console.log(response)
        this.drivings = response as Driving[];
      },
      error : (error) =>{
        console.log(error);
      }
    })
  }
}
