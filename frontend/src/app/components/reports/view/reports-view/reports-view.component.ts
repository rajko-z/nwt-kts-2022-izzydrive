import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { isThisSecond } from 'date-fns';
import { DrivingDistanceReportItem } from 'src/app/model/reports/DrivingDistanceReportItem';
import { DrivingPriceReportItem } from 'src/app/model/reports/DrivingPriceReportItem';
import { DrivingReport } from 'src/app/model/reports/DrivingReport';
import { DrivingsNumberReportItem } from 'src/app/model/reports/DrivingsNumberReportItem';
import { Role } from 'src/app/model/user/role';
import { ReportsService } from 'src/app/services/report/reports.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-reports-view',
  templateUrl: './reports-view.component.html',
  styleUrls: ['./reports-view.component.scss']
})
export class ReportsViewComponent implements OnInit {

  maxDate: Date = new Date();
  dateForm = new FormGroup({
    startDate: new FormControl<Date | null>(null),
    endDate: new FormControl<Date | null>(null),
  });

  reportX : string[] = [];
  drivingNumbersY : number[] = [];
  priceReportY: number[] = [];
  distancereportY: number[] = [];
  reportData: DrivingReport;
  drivingNumbersValues: number[] = [];
  priceReportValues: number[] = [];
  distancereportValues: number[] = [];

  reportDataLoaded: boolean = false;

  drivingNumbersReportTitle: string = "Number of drivings per day for selected period";
  drivingPriceReportTitle: string = "Prices of drivings per day for selected period";
  drivingDistanceReportTitle: string = "Distance of drivings per day for selected period";

  constructor(private reportService :ReportsService, private userService: UserService) { }

  ngOnInit(): void {
  console.log(this.maxDate);

  }

  onSubmit(){
    if (this.userService.getRoleCurrentUserRole() === Role.ROLE_PASSENGER){
      this.getPassengerReport();
    }
    else if (this.userService.getRoleCurrentUserRole() === Role.ROLE_DRIVER){
      this.getDriverReport()
    }
   
  }

  getPassengerReport(){
    this.reportService.getPassengerReportData(this.dateForm.value.startDate, this.dateForm.value.endDate).subscribe({
      next : (response) => {
        console.log(response);
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
      },
      error: (error) => {
        console.log(error)
      }
    });
  }

  getDriverReport(){
    this.reportService.getDriverReportData(this.dateForm.value.startDate, this.dateForm.value.endDate).subscribe({
      next : (response) => {
        console.log(response);
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
      },
      error: (error) => {
        console.log(error)
      }
    });
  }

  getChartDataForDrivingNumberReport(reportData: DrivingReport){
    this.priceReportValues = []
    this.reportX = []
    reportData.drivingPrices.forEach((item : DrivingPriceReportItem) => {
      this.priceReportValues.push(item.price);
      this.reportX.push(item.date);
    })
    this.drivingNumbersValues = []
    reportData.drivingsNumber.forEach((item: DrivingsNumberReportItem) => {
      this.drivingNumbersValues.push(item.numberOfDrivings);
    })
    this.distancereportValues = []
    reportData.drivingsDistances.forEach((item: DrivingDistanceReportItem) => {
      this.distancereportValues.push(item.distance);
    })

  }

}
