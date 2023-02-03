import {DatePipe} from '@angular/common';
import {Component, Inject, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {Router} from '@angular/router';
import {Driving, DrivingState} from 'src/app/model/driving/driving';
import {User} from 'src/app/model/user/user';
import {DrivingService} from 'src/app/services/drivingService/driving.service';
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {
  ConfirmCancelReservationComponent
} from '../notifications/confirm-cancel-reservation/confirm-cancel-reservation.component';
import {DetailRideViewComponent} from "../detail-ride-view/detail-ride-view.component";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-reservations-list',
  templateUrl: './reservations-list.component.html',
  styleUrls: ['./reservations-list.component.scss']
})
export class ReservationsListComponent {

  displayedColumns: string[] = ['startAddress', 'endAddress', 'startTime','price', 'state', 'details', 'cancel'];
  gettingDataFinished : boolean =  false;
  dataSource : MatTableDataSource<Driving>;
  curretnUser : User;

  @ViewChild('paginator') paginator: MatPaginator;

  constructor(private drivingService:DrivingService,
              private userService: UserService,
              public datepipe: DatePipe,
              @Inject(MAT_DIALOG_DATA) public data,
              public dialog: MatDialog,
              public snackbar : MatSnackBar,
              private responseMessage: ResponseMessageService,
              private router : Router) {
      this.userService.getCurrentUserData().subscribe({
        next : (user) => {
          this.curretnUser = user;
          this.setDrivings();
          this.gettingDataFinished = true;
        },
        error: (error) => {
          this.responseMessage.openErrorMessage(error.error.message)
        }
      })
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
      this.router.navigateByUrl('/passenger');
    }
  }

  doCancel(driving : Driving){
    this.drivingService.cancelReservation(driving.id).subscribe({
      next: (response) =>{
         this.setDrivings();
      },
      error: (error) => {
       this.responseMessage.openErrorMessage(error.error.message)
      }
   })
  }

  detailsClicked(driving: Driving) {
    this.dialog.open(DetailRideViewComponent, {
      data: driving.id,
    });
  }
}
