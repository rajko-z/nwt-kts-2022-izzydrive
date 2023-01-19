import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Driving } from 'src/app/model/driving/driving';
import { DrivingState } from 'src/app/model/driving/drivingState';
import { User } from 'src/app/model/user/user';
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { NotificationService } from 'src/app/services/notificationService/notification.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ConfirmCancelReservationComponent } from '../notifications/confirm-cancel-reservation/confirm-cancel-reservation.component';

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

  constructor(private drivingService:DrivingService, 
              private userService: UserService,  
              public datepipe: DatePipe, 
              public snackbar : MatSnackBar, 
              private notificationService :NotificationService) {
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

  onCancel(driving : Driving){
    if(driving.drivingState === DrivingState.WAITING){
      this.snackbar.openFromComponent(ConfirmCancelReservationComponent, {
        data: {
          drivingId: driving.id,
          preClose: () => {
            this.snackbar.dismiss()
          }
        },
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      });
    }
    else{
      this.doCancel(driving);
    }
  }

  doCancel(driving : Driving){
    this.drivingService.cancelReservation(driving.id).subscribe({
      next: (response) =>{
         this.setDrivings();
      },
      error: (error) => {
       this.snackbar.open(error.error.message, "OK")
      }
   })
  }
}
