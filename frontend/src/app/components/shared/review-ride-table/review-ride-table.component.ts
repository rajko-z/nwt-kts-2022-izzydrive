import {AfterViewInit, Component, Inject, Input, ViewChild} from '@angular/core';
import {MatDialog, MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Driving} from 'src/app/model/driving/driving';
import {Role} from 'src/app/model/user/role';
import {Sort} from '@angular/material/sort';
import {DrivingService} from 'src/app/services/drivingService/driving.service';
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {EvaluationComponent} from '../../driving-history/evaluation/evaluation.component';
import {DatePipe} from '@angular/common';
import {MatPaginator} from '@angular/material/paginator';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {FavoriteRoute} from 'src/app/model/route/favoriteRoute';
import {RouteService} from 'src/app/services/routeService/route.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {DetailRideViewComponent} from "../../detail-ride-view/detail-ride-view.component";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';


@Component({
  selector: 'app-review-ride-table',
  templateUrl: './review-ride-table.component.html',
  styleUrls: ['./review-ride-table.component.scss']
})
export class ReviewRideTableComponent implements AfterViewInit {

  displayedColumns: string[] = ['startAddress', 'endAddress', 'startTime', "endTime", 'price', 'details'];
  isAdmin: boolean;
  isPassenger: boolean;
  gettingDataFinished: boolean = false;
  dataSource: MatTableDataSource<Driving>;
  tooltipText: string;

  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatTable) matTable!: MatTable<any>;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private drivingService: DrivingService,
              @Inject(MAT_DIALOG_DATA) public data,
              private userService: UserService,
              public dialog: MatDialog,
              public datepipe: DatePipe,
              private routeService: RouteService,
              private responseMessage: ResponseMessageService) {

    if (Object.keys(this.data).length == 0) {
      this.userService.getCurrentUserData().subscribe({
        next: (user) => {
          this.data = user;
          this.data.message = this.data?.role === Role.ROLE_ADMIN ? this.data.message : 'You do not have any rides yet';
          this.isAdmin = this.data?.role === Role.ROLE_ADMIN;
          this.isPassenger = this.data?.role === Role.ROLE_PASSENGER
          this.setDrivings();
          this.gettingDataFinished = true;
          if (this.isPassenger) {
            this.displayedColumns.push('evaluate')
            this.displayedColumns.push('favorite')
          }
        },
        error: (error) => {
          this.responseMessage.openErrorMessage(error.error.message)
        }
      })
    } else {
      this.isAdmin = this.data?.role === Role.ROLE_ADMIN;
      this.setDrivings();
      this.gettingDataFinished = true;
    }
  }
  setDrivings() {
    if (this.data?.role === Role.ROLE_DRIVER) {
      this.drivingService.findAllByDriverId(this.data.id).subscribe((res) => {
        this.setDataSource(res as Driving[])
        this.sortData({active: "startTime", direction: 'desc'} as Sort)
      });
    } else if (this.data?.role === Role.ROLE_PASSENGER) {
      this.drivingService.getDrivingsHistoryForPassenger(this.data.id).subscribe((res) => {
        this.setDataSource(res as Driving[])
        this.sortData({active: "startTime", direction: 'desc'} as Sort)
      });
    }
  }

  sortData(sort: Sort) {
    this.setDataSource(this.drivingService.sortData(sort, this.dataSource.data))
  }

  openDialog(driving: Driving): void {
    this.dialog.open(EvaluationComponent, {
      data: driving,
    });
  }

  setDataSource(drivingSource: Driving[]) {
    this.dataSource = new MatTableDataSource<Driving>(drivingSource);
    this.dataSource.paginator = this.paginator;
  }

  removeFromFavourite(driving: Driving) {
    this.routeService.removeFromFavoriteRoutes(driving.routeId, this.userService.getCurrentUserId()).subscribe({
      next: (response) => {
        driving.favoriteRoute = false;
        this.responseMessage.openSuccessMessage(response.text)
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }

  addToFavourite(driving: Driving) {
    let newFavoriteRide = new FavoriteRoute(driving.routeId);
    this.routeService.addNewFavoriteRoute(newFavoriteRide).subscribe({
      next: (response) => {
        driving.favoriteRoute = true
        this.matTable.renderRows();
        this.responseMessage.openSuccessMessage(response.text)
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
