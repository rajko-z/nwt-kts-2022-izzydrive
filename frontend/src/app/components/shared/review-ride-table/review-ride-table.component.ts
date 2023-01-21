import {AfterViewInit, Component, Inject, Input, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {MatDialog, MAT_DIALOG_DATA} from "@angular/material/dialog";
import { Driving } from 'src/app/model/driving/driving';
import { Role } from 'src/app/model/user/role';
import {Sort} from '@angular/material/sort';;
import { DrivingService } from 'src/app/services/drivingService/driving.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { EvaluationComponent } from '../../driving-history/evaluation/evaluation.component';
import { DatePipe } from '@angular/common';
import { MatPaginator } from '@angular/material/paginator';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import { FavoriteRoute } from 'src/app/model/route/favoriteRoute';
import { RouteService } from 'src/app/services/routeService/route.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-review-ride-table',
  templateUrl: './review-ride-table.component.html',
  styleUrls: ['./review-ride-table.component.scss']
})
export class ReviewRideTableComponent implements AfterViewInit  {

  displayedColumns: string[] = ['startAddress', 'endAddress', 'startTime', "endTime", 'price', 'details'];
  isAdmin: boolean;
  isPassenger: boolean;
  gettingDataFinished : boolean =  false;
  dataSource : MatTableDataSource<Driving>;
  tooltipText: string;
  
  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatTable) matTable!: MatTable<any>;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private drivingService:DrivingService, 
              @Inject(MAT_DIALOG_DATA) public data, 
              private userService: UserService, 
              public dialog: MatDialog, 
              public datepipe: DatePipe,
              private routeService: RouteService,
              private snackbar : MatSnackBar) {

    if(Object.keys(this.data).length == 0){
      this.userService.getCurrentUserData().subscribe({
        next : (user) => {
          this.data = user;
          this.isAdmin = this.data?.role === Role.ROLE_ADMIN;
          this.isPassenger = this.data?.role === Role.ROLE_PASSENGER
          this.setDrivings();
          this.gettingDataFinished = true;
          if(this.isPassenger){
            this.displayedColumns.push('evaluate')
            this.displayedColumns.push('favorite')
          }
        },
        error: (error) => {
          this.snackbar.open(error.error.message, "OK")
        }
      })
    }
    else{
      this.isAdmin = this.data?.role === Role.ROLE_ADMIN;
      this.setDrivings();
      this.gettingDataFinished = true;
    }
  } 

  ngOnInit(): void {
    
  }

  setDrivings(){
    if(this.data?.role === Role.ROLE_DRIVER){
      this.drivingService.findAllByDriverId(this.data.id).subscribe((res) => {
        this.setDataSource(res as Driving[])  
        this.sortData({active: "startTime", direction:'desc'} as Sort )
      });
    }else if(this.data?.role === Role.ROLE_PASSENGER){
      this.drivingService.getDrivingsHistoryForPassenger(this.data.id).subscribe((res) => {        
        this.setDataSource(res as Driving[])
        this.sortData({active: "startTime", direction:'desc'} as Sort )
      });
    }
  }

  sortData(sort: Sort) {
    this.setDataSource(this.drivingService.sortData(sort, this.dataSource.data))
  }

  openDialog(driving : Driving): void {
      this.dialog.open(EvaluationComponent, {
        data: driving,
      });
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

  removeFromFavourite(driving : Driving){
    this.routeService.removeFromFavoriteRoutes(driving.routeId,  this.userService.getCurrentUserId()).subscribe({
      next : (response) => {
        driving.favoriteRoute = false;
        this.snackbar.open(response.text, "OK")
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "OK")
      }
    })
  }

  addToFavourite(driving : Driving){
    console.log(driving.routeId)
    let newFavoriteRide = new FavoriteRoute(this.userService.getCurrentUserId(),
                                            driving.routeId);
    this.routeService.addNewFavoriteRoute(newFavoriteRide).subscribe({
      next: (response)=> {
          driving.favoriteRoute = true
          this.matTable.renderRows();
          this.snackbar.open(response.text, "OK")
      },
      error: (error) => {
        console.log(error)
        this.snackbar.open(error.error.message, "OK")
      }
    })
  }

}
