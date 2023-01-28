import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {DrivingDistanceReportItem} from 'src/app/model/reports/DrivingDistanceReportItem';
import {DrivingPriceReportItem} from 'src/app/model/reports/DrivingPriceReportItem';
import {DrivingReport} from 'src/app/model/reports/DrivingReport';
import {DrivingsNumberReportItem} from 'src/app/model/reports/DrivingsNumberReportItem';
import {Role} from 'src/app/model/user/role';
import {ReportsService} from 'src/app/services/report/reports.service';
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {LoggedUserService} from "../../../../services/loggedUser/logged-user.service";
import {User} from "../../../../model/user/user";

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

  reportX: string[] = [];
  drivingNumbersY: number[] = [];
  priceReportY: number[] = [];
  distancereportY: number[] = [];
  reportData: DrivingReport;
  drivingNumbersValues: number[] = [];
  priceReportValues: number[] = [];
  distancereportValues: number[] = [];

  isAdmin: boolean = this.userService.getRoleCurrentUserRole() === Role.ROLE_ADMIN;
  users: User[];
  value: User;

  reportDataLoaded: boolean = false;

  drivingNumbersReportTitle: string = "Number of drivings per day for selected period";
  drivingPriceReportTitle: string = "Prices of drivings per day for selected period";
  drivingDistanceReportTitle: string = "Distance of drivings per day for selected period";

  constructor(private reportService: ReportsService, private userService: UserService, private loggedUserService: LoggedUserService) {
  }

  ngOnInit(): void {
    if (this.userService.getRoleCurrentUserRole() === Role.ROLE_ADMIN) {
      this.loadUsers();
    }
  }

  loadUsers() {
    this.loggedUserService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response.filter((user: User) => user.role !== Role.ROLE_ADMIN) as User[];
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  onSubmit() {
    if (this.userService.getRoleCurrentUserRole() === Role.ROLE_PASSENGER) {
      this.getPassengerReport(this.userService.getCurrentUserId());
    } else if (this.userService.getRoleCurrentUserRole() === Role.ROLE_DRIVER) {
      this.getDriverReport(this.userService.getCurrentUserId())
    } else if (this.userService.getRoleCurrentUserRole() === Role.ROLE_ADMIN) {
      this.chooseAdminReport()
    }
  }

  getPassengerReport(userId: number) {
    this.reportService.getPassengerReportData(this.dateForm.value.startDate, this.dateForm.value.endDate, userId).subscribe({
      next: (response) => {
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
      },
      error: (error) => {
        console.log(error)
      }
    });
  }

  getDriverReport(userId: number) {
    this.reportService.getDriverReportData(this.dateForm.value.startDate, this.dateForm.value.endDate, userId).subscribe({
      next: (response) => {
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
      },
      error: (error) => {
        console.log(error)
      }
    });
  }

  getChartDataForDrivingNumberReport(reportData: DrivingReport) {
    this.priceReportValues = []
    this.reportX = []
    reportData.drivingPrices.forEach((item: DrivingPriceReportItem) => {
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

  private chooseAdminReport() {
    console.log(this.value);
    if (this.value) {
      if (this.value.role === Role.ROLE_DRIVER) {
        this.getDriverReport(this.value?.id);
      }
      if (this.value.role === Role.ROLE_PASSENGER) {
        this.getPassengerReport(this.value?.id);
      }
    } else {
      this.getAdminReport();
    }
  }

  private getAdminReport() {
    this.reportService.getAdminReportData(this.dateForm.value.startDate, this.dateForm.value.endDate).subscribe({
      next: (response) => {
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
      },
      error: (error) => {
        console.log(error)
      }
    });
  }
}
