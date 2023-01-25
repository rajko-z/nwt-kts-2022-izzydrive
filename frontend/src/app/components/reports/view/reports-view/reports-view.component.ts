import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { isThisSecond } from 'date-fns';
import { DrivingDistanceReportItem } from 'src/app/model/reports/DrivingDistanceReportItem';
import { DrivingPriceReportItem } from 'src/app/model/reports/DrivingPriceReportItem';
import { DrivingReport } from 'src/app/model/reports/DrivingReport';
import { DrivingsNumberReportItem } from 'src/app/model/reports/DrivingsNumberReportItem';
import { ReportsService } from 'src/app/services/report/reports.service';

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
  constructor(private reportService :ReportsService, private datepipe: DatePipe) { }

  ngOnInit(): void {
  console.log(this.maxDate);

  }

  onSubmit(){
    console.log(this.dateForm.value);
    this.reportService.getPassengerReportData(this.dateForm.value.startDate, this.dateForm.value.endDate).subscribe({
      next : (response) => {
        console.log(response);
        this.reportData = response;
        this.getChartDataForDrivingNumberReport(response);
        this.reportDataLoaded = true;
        //iz liste uzeti sve elemente na prvom mestu za x osu gde je kljuc date, za y osu drigi kljuc, usput se proverava za svaki datum iz izabranog opsega, ako nije medju vrednostima, dodaje se vrednost 0
      },
      error: (error) => {
        console.log(error)
      }
    });
  }

  getChartDataForDrivingNumberReport(reportData: DrivingReport){
    reportData.drivingPrices.forEach((item : DrivingPriceReportItem) => {
      this.priceReportValues.push(item.price);
      this.reportX.push(item.date);
    })
    
    reportData.drivingsNumber.forEach((item: DrivingsNumberReportItem) => {
      this.drivingNumbersValues.push(item.numberOfDrivings);
    })

    reportData.drivingsDistances.forEach((item: DrivingDistanceReportItem) => {
      this.distancereportValues.push(item.distance);
    })

  }

}
