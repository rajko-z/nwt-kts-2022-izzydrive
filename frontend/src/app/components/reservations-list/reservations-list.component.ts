import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Driving } from 'src/app/model/driving/driving';
import { User } from 'src/app/model/user/user';
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.scss']
})
export class ReservationsListComponent implements OnInit {

  displayedColumns: string[] = ['startAddress', 'endAddress', 'startTime','price', 'state', 'details', 'cancel'];
  gettingDataFinished : boolean =  false;
  dataSource : MatTableDataSource<Driving>;
  curretnUser : User;
  
  @ViewChild('paginator') paginator: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private drivingService:DrivingService, private userService: UserService,  public datepipe: DatePipe) {
      this.userService.getCurrentUserData().subscribe({
        next : (user) => {
          this.curretnUser = user;
          this.setDrivings();
          this.gettingDataFinished = true;
        },
        error: (error) => {
          console.log(error);
        }
      })
  } 

  ngOnInit(): void {
    
  }

  setDrivings(){
      this.drivingService.findPassengerReservations(this.curretnUser.id).subscribe((res) => {
        this.setDataSource(res as Driving[])    
      });
  }

  sortData(sort: Sort) {
    this.setDataSource(this.drivingService.sortData(sort, this.dataSource.data))
  }

  setDataSource(drivingSource : Driving[]){
    this.dataSource = new MatTableDataSource<Driving>(drivingSource);
    this.dataSource.paginator = this.paginator;
  }

  onPaginateChange(event){
    console.log(event)
     let pageIndex = event.pageIndex;
     let pageSize = event.pageSize;
     //ovde se nadovezati na bec ako ce se raditi paginacija na beku
  }
}
