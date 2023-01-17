import {Component, Inject, Input, OnInit, SimpleChanges} from '@angular/core';
import {MatDialog, MAT_DIALOG_DATA} from "@angular/material/dialog";
import { Driving } from 'src/app/model/driving/driving';
import { Role } from 'src/app/model/user/role';
import {Sort} from '@angular/material/sort';;
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { EvaluationComponent } from '../../driving-history/evaluation/evaluation.component';


@Component({
  selector: 'app-review-ride-table',
  templateUrl: './review-ride-table.component.html',
  styleUrls: ['./review-ride-table.component.scss']
})
export class ReviewRideTableComponent implements OnInit {

  displayedColumns: string[] = ['startAddress', 'endAddress', 'startTime', "endTime", 'price', 'evaluate'];
  drivings : Driving[] = [];
  sortedData: Driving[];
  isAdmin: boolean;
  isPassenger: boolean;

  constructor(private drivingService:DrivingService, @Inject(MAT_DIALOG_DATA) public data, private userService: UserService, public dialog: MatDialog) {

  } 

  ngOnInit(): void {
    if(Object.keys(this.data).length == 0){
      this.userService.getCurrentUserData().subscribe({
        next : (user) => {
          this.data = user;
          this.isAdmin = this.data?.role === Role.ROLE_ADMIN;
          this.isPassenger = this.data?.role === Role.ROLE_PASSENGER
          this.setDrivings();
        },
        error: (error) => {
          console.log(error);
        }
      })
    }
  }

  setDrivings(){
    if(this.data?.role === Role.ROLE_DRIVER){
      this.drivingService.findAllByDriverId(this.data.id).subscribe((res) => {
        this.drivings = res as Driving[];
      });
    }else if(this.data?.role === Role.ROLE_PASSENGER){
      this.drivingService.getDrivingsHistoryForPassenger(this.data.id).subscribe((res) => {
        this.drivings = res as Driving[];
        console.log(this.drivings)
      });
    }
  }

  // ngOnChanges(changes: SimpleChanges) {
        
  //       this.rides = changes.rides.currentValue;
  //       // You can also use categoryId.previousValue and 
  //       // categoryId.firstChange for comparing old and new values
        
  //   }

  sortData(sort: Sort) {
    const data = this.drivings.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'startAddress':
          return this.compare(a.start.name, b.start.name, isAsc);
        case 'endAddress':
          return this.compare(a.end.name, b.end.name, isAsc);
        case 'startTime':
          return this.compare(a.startDate, b.startDate, isAsc);
        case 'endTime':
          return this.compare(a.endDate, b.endDate, isAsc);
        case 'price':
          return this.compare(a.price, b.price, isAsc);
        default:
          return 0;
      }
    });
  }

  
compare(a: number | string | Date, b: number | string | Date, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

  openDialog(driving : Driving): void {
    this.dialog.open(EvaluationComponent, {
      data: {id: driving.id},
    });
  }

}
